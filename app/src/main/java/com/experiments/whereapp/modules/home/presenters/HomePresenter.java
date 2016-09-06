/*
 *
 *  Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.experiments.whereapp.modules.home.presenters;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.droidexperiments.android.where.R;
import com.experiments.core.exceptions.NoInternetException;
import com.experiments.core.helpers.location.GetPlaceCallback;
import com.experiments.core.helpers.location.LocationUpdatesListener;
import com.experiments.core.helpers.location.LocationUpdatesManager;
import com.experiments.core.helpers.location.PlaceDataWrapper;
import com.experiments.core.mvp.presenters.BasePresenter;
import com.experiments.whereapp.events.places.GetPlaceErrorEvent;
import com.experiments.whereapp.events.places.UpdateCurrentPlaceEvent;
import com.experiments.whereapp.modules.home.views.HomeScreenView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * presenter for home screen
 */
public class HomePresenter extends BasePresenter<HomeScreenView> implements LocationUpdatesListener {

    private static final String TAG = "HomeScreenPresenter";

    //minimum distance from old location for which address text should be refreshed
    private static final double MIN_DISTANCE_IN_METERS = 100.00;
    private final EventBus eventBus;
    //helper for location updates
    private LocationUpdatesManager locationUpdatesManager;
    //temporary location to check whether new updated location has not more distance from old than {@link #MIN_DISTANCE_IN_METERS}
    private Location tempLocation;

    //avoiding direct instances. use factory method instead.
    private HomePresenter() {
        eventBus = EventBus.getDefault();
    }


    public static HomePresenter create() {
        return new HomePresenter();
    }

    @Override
    @DebugLog
    public void attachView(HomeScreenView view) {
        super.attachView(view);
        view.makeStatusBarTransparent();
        view.setupViewPager();
    }

    @Override
    @DebugLog
    public void detachView() {
        locationUpdatesManager = null;
        super.detachView();
    }

    @DebugLog
    public void registerPlaceUpdates() {
        locationUpdatesManager = new LocationUpdatesManager(view.getContext());
        locationUpdatesManager.registerUpdateCallbacks(this);
    }

    @DebugLog
    public void requestPlaceUpdates() {
        locationUpdatesManager.connectApiClient();
    }

    @DebugLog
    @SuppressWarnings({"MissingPermission"})
    public void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates) {
        if (locationSettingsStates.isLocationUsable()) {
            locationUpdatesManager.retrieveLastKnownPlace(false);
            locationUpdatesManager.scheduleLocationUpdates();
        } else {
            view.showError(R.string.unable_to_get_location);
            eventBus.post(new GetPlaceErrorEvent(new Throwable("Permission was denied by user")));
        }
    }

    @DebugLog
    public void stopPlaceUpdates() {
        locationUpdatesManager.removeLocationUpdates();
        locationUpdatesManager.disconnectApiClient();
        tempLocation = null;
    }

    @DebugLog
    public void unregisterPlaceUpdates() {
        locationUpdatesManager.unregisterUpdateCallbacks();
    }

    @Override
    @DebugLog
    public void onApiClientConnected() {
        locationUpdatesManager.checkLocationSettings();
    }

    @Override
    @DebugLog
    @SuppressWarnings({"MissingPermission"})
    public void onLocationSettingsResult(LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS: //if location is on
                locationUpdatesManager.retrieveLastKnownPlace(false);
                locationUpdatesManager.scheduleLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED://if to ask through dialog
                view.showTurnOnLocationDialog(status);
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: //if settings can not be changed
                view.showError(R.string.unable_to_get_location);
                break;
        }
    }

    @DebugLog
    public void checkPlacePickerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                Place place = PlacePicker.getPlace(view.getContext(), data);
                if (place == null) return;
                Log.e(TAG, "got place from place picker: " + place.getName());
                break;

            case PlacePicker.RESULT_ERROR:
                Status status = PlacePicker.getStatus(view.getContext(), data);
                Log.e(TAG, "error from place picker" + status.getStatusMessage());
                break;
        }
    }

    @Override
    @DebugLog
    public void onGotLastKnownPlace(@NonNull PlaceDataWrapper lastKnownPlace) {
        postUpdateCurrentPlaceEvent(lastKnownPlace);
    }

    @Override
    @DebugLog
    @SuppressWarnings({"MissingPermission"})
    public void onLocationUpdated(Location newLocation) {
        //if new location is not more than just : say 50<n<100 meters away - do not refresh view
        if (tempLocation != null && newLocation.distanceTo(tempLocation) <= MIN_DISTANCE_IN_METERS) {
            Log.e(TAG, "onLocationUpdated: newLocation : " + newLocation + " tempLocation: " + tempLocation + "\n distance between: " + newLocation.distanceTo(tempLocation) + "is less than threshold : " + MIN_DISTANCE_IN_METERS + " meters");
            return;
        }
        tempLocation = newLocation;
        //otherwise get place with updated address and refresh text on view
        locationUpdatesManager.getCurrentPlace(true, this::handleCurrentPlaceResult);
    }


    @DebugLog
    @Override
    public void onErrorGettingLastKnownPlace(Throwable t) {
        postErrorGettingPlaceEvent(t);
        t.printStackTrace();
    }

    @DebugLog
    @Override
    public void onErrorUpdatingLocation(Throwable t) {
        postErrorGettingPlaceEvent(t);
        t.printStackTrace();
    }

    private void handleCurrentPlaceResult(PlaceDataWrapper place, @GetPlaceCallback.GetPlaceOperationStatus int operationStatus) {
        if (place == null) {
            onErrorGettingLastKnownPlace(new Throwable("got null place"));
            return;
        }
        switch (operationStatus) {
            case GetPlaceCallback.STATUS_SUCCESS:
                postUpdateCurrentPlaceEvent(place);
                break;
            case GetPlaceCallback.STATUS_NO_NETWORK:
                onErrorGettingLastKnownPlace(new NoInternetException("Home"));
                break;

            case GetPlaceCallback.STATUS_UNKNOWN_FAILURE:
                onErrorGettingLastKnownPlace(new Throwable("unknown failure operation status"));
                break;
        }
    }

    @DebugLog
    public void postUpdateCurrentPlaceEvent(PlaceDataWrapper currentPlace) {
        eventBus.post(new UpdateCurrentPlaceEvent(currentPlace));
    }

    @DebugLog
    public void postErrorGettingPlaceEvent(Throwable t) {
        eventBus.post(new GetPlaceErrorEvent(t));
    }
}
