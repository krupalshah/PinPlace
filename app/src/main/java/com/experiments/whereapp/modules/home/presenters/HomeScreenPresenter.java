/*
 *   Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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
import com.experiments.common.helpers.location.GetPlaceCallback;
import com.experiments.common.helpers.location.LocationUpdatesHelper;
import com.experiments.common.helpers.location.LocationUpdatesListener;
import com.experiments.common.helpers.location.PlaceDataWrapper;
import com.experiments.common.mvp.presenters.BasePresenter;
import com.experiments.whereapp.events.OnCurrentPlaceUpdated;
import com.experiments.whereapp.modules.home.views.HomeView;
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
public class HomeScreenPresenter extends BasePresenter<HomeView> implements LocationUpdatesListener {

    private static final String TAG = "HomeScreenPresenter";

    //minimum distance from old location for which address text should be refreshed
    private static final double MIN_DISTANCE_IN_METERS = 100.00;

    //helper for location updates
    private LocationUpdatesHelper locationUpdatesHelper;

    //temporary location to check whether new updated location has not more distance from old than {@link #MIN_DISTANCE_IN_METERS}
    private Location tempLocation;

    private HomeScreenPresenter() {
        //private constructor to avoid direct instances. use factory method instead.
    }

    public static HomeScreenPresenter create() {
        return new HomeScreenPresenter();
    }

    @Override
    @DebugLog
    public void attachView(HomeView view) {
        super.attachView(view);
        view.makeStatusBarTransparent();
        view.setupViewPager();
    }

    @Override
    @DebugLog
    public void detachView() {
        getView().removeListeners();
        super.detachView();
    }

    @DebugLog
    public void registerPlaceUpdates() {
        locationUpdatesHelper = new LocationUpdatesHelper(getView().getContext());
        locationUpdatesHelper.registerUpdateCallbacks(this);
    }

    @DebugLog
    public void requestPlaceUpdates() {
        locationUpdatesHelper.connectApiClient();
    }

    @DebugLog
    public void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates) {
        if (locationSettingsStates.isLocationUsable()) {
            locationUpdatesHelper.retrieveLastKnownPlace(false);
            locationUpdatesHelper.scheduleLocationUpdates();
        } else {
            getView().showSnakeBar(R.string.unable_to_get_location, R.string.dismiss, null);
        }
    }

    @DebugLog
    public void stopPlaceUpdates() {
        locationUpdatesHelper.removeLocationUpdates();
        locationUpdatesHelper.disconnectApiClient();
    }

    @DebugLog
    public void unregisterPlaceUpdates() {
        locationUpdatesHelper.unregisterUpdateCallbacks();
    }

    @Override
    @DebugLog
    public void onApiClientConnected() {
        locationUpdatesHelper.checkLocationSettings();
    }

    @Override
    @DebugLog
    public void onLocationSettingsResult(LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS: //if location is on
                locationUpdatesHelper.retrieveLastKnownPlace(false);
                locationUpdatesHelper.scheduleLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED://if to ask through dialog
                getView().showTurnOnLocationDialog(status);
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: //if settings can not be changed
                getView().showSnakeBar(R.string.unable_to_get_location, R.string.dismiss, null);
                break;
        }
    }

    @DebugLog
    public void checkPlacePickerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                Place place = PlacePicker.getPlace(getView().getContext(), data);
                if (place == null) return;
                Log.e(TAG, "got place from place picker: " + place.getName());
                break;

            case PlacePicker.RESULT_ERROR:
                Status status = PlacePicker.getStatus(getView().getContext(), data);
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
    public void onLocationUpdated(Location newLocation) {
        //if new location is not more than just : say 50<n<100 meters away - do not refresh view
        if (tempLocation != null && newLocation.distanceTo(tempLocation) <= MIN_DISTANCE_IN_METERS) {
            Log.e(TAG, "onLocationUpdated: newLocation : " + newLocation + " tempLocation: " + tempLocation + "\n distance between: " + newLocation.distanceTo(tempLocation) + "is less than threshold : " + MIN_DISTANCE_IN_METERS + " meters");
            return;
        }
        tempLocation = newLocation;
        //otherwise get place with updated address and refresh text on view
        locationUpdatesHelper.getCurrentPlace(true, (place, operationStatus) -> {
            if (place != null && operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                postUpdateCurrentPlaceEvent(place);
            }
        });
    }

    private void postUpdateCurrentPlaceEvent(PlaceDataWrapper currentPlace) {
        EventBus.getDefault().post(new OnCurrentPlaceUpdated(currentPlace));
    }

}
