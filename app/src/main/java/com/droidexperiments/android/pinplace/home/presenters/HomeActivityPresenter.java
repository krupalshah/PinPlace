/*
 *   Copyright 2016 Krupal Shah, Harsh Bhavsar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package com.droidexperiments.android.pinplace.home.presenters;

import android.location.Location;
import android.util.Log;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.base.presenters.BasePresenterImpl;
import com.droidexperiments.android.pinplace.home.contracts.HomeActivityContract;
import com.droidexperiments.android.pinplace.models.Place;
import com.droidexperiments.android.pinplace.operations.location.GetPlaceCallback;
import com.droidexperiments.android.pinplace.operations.location.LocationOperations;
import com.droidexperiments.android.pinplace.operations.location.LocationOperationsImpl;
import com.droidexperiments.android.pinplace.operations.location.PlaceUpdatesListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public class HomeActivityPresenter extends BasePresenterImpl<HomeActivityContract.View> implements HomeActivityContract.Presenter, PlaceUpdatesListener {

    private static final String TAG = "HomeActivityPresenter";

    private LocationOperations mLocationOperations;

    @Override
    public void attachView(HomeActivityContract.View view) {
        Log.d(TAG, "attachView ");
        super.attachView(view);
        view.setTransparentStatusBar();
        view.animateToolbarCollapsing();
        view.setToolbarTitle(R.string.home);
        view.setupViewPager();
    }

    @Override
    public void detachView() {
        Log.d(TAG, "detachView ");
        super.detachView();
    }

    @Override
    public void registerPlaceUpdates() {
        Log.d(TAG, "registerPlaceUpdates ");
        mLocationOperations = new LocationOperationsImpl(getView().getComponentContext());
        mLocationOperations.registerPlaceUpdateCallbacks(this);
    }

    @Override
    public void requestPlaceUpdates() {
        Log.d(TAG, "requestPlaceUpdates ");
        mLocationOperations.connectApiClient();
    }

    @Override
    public void stopPlaceUpdates() {
        Log.d(TAG, "stopPlaceUpdates ");
        mLocationOperations.removeLocationUpdates();
        mLocationOperations.disconnectApiClient();
    }

    @Override
    public void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates) {
        Log.d(TAG, "checkTurnOnLocationResult() called with " + "locationSettingsStates = [" + locationSettingsStates + "]");
        if (locationSettingsStates.isLocationUsable()) {
            mLocationOperations.retrieveLastKnownPlace();
            mLocationOperations.scheduleLocationUpdates();
        } else {
            getView().showSnakeBarAtBottom(R.string.unable_to_get_location, R.string.dismiss, null);
        }
    }

    @Override
    public void unregisterPlaceUpdates() {
        Log.d(TAG, "unregisterPlaceUpdates ");
        mLocationOperations.unregisterPlaceUpdateCallbacks();
        mLocationOperations = null;
    }

    @Override
    public void onApiClientConnected() {
        Log.d(TAG, "onApiClientConnected ");
        mLocationOperations.checkLocationSettings();
    }

    @Override
    public void onLocationSettingsResult(LocationSettingsResult locationSettingsResult) {
        Log.d(TAG, "onLocationSettingsResult() called with " + "status = [" + locationSettingsResult.getStatus() + "]");
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS: //if location is on
                mLocationOperations.retrieveLastKnownPlace();
                mLocationOperations.scheduleLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED://if to ask through dialog
                getView().showTurnOnLocationDialog(status);
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: //if settings can not be changed
                getView().showSnakeBarAtBottom(R.string.unable_to_get_location, R.string.dismiss, null);
                break;
        }
    }

    @Override
    public void onGotLastKnownPlace(Place lastKnownPlace) {
        Log.d(TAG, "onGotLastKnownPlace() called with " + "lastKnownPlace = [" + lastKnownPlace + "]");
        getView().updateAddressText(lastKnownPlace.getAddress());
    }

    @Override
    public void onLocationUpdated(Location newLocation) {
        Log.d(TAG, "onLocationUpdated() called with " + "newLocation = [" + newLocation + "]");
        mLocationOperations.getCurrentPlace(true, new GetPlaceCallback() {
            @Override
            public void onGotPlace(Place place, @GetPlaceOperationStatus int operationStatus) {
                Log.d(TAG, "onGotPlace() called with " + "place = [" + place + "], operationStatus = [" + operationStatus + "]");
                if (operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                    mView.updateAddressText(place.getAddress());
                }
            }
        });
    }
}
