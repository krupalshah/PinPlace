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

package com.experiments.whereapp.modules.home.presenters;

import android.location.Location;

import com.droidexperiments.android.where.R;
import com.experiments.common.base.presenters.BasePresenterImpl;
import com.experiments.common.location.GetPlaceCallback;
import com.experiments.common.location.LocationOperations;
import com.experiments.common.location.LocationOperationsImpl;
import com.experiments.common.location.Place;
import com.experiments.common.location.PlaceUpdatesListener;
import com.experiments.whereapp.modules.home.contracts.HomeActivityContract;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public class HomeActivityPresenter extends BasePresenterImpl<HomeActivityContract.View> implements HomeActivityContract.Presenter, PlaceUpdatesListener {

    private LocationOperations mLocationOperations;
    private Location tempLocation;

    @Override
    @DebugLog
    public void attachView(HomeActivityContract.View view) {
        super.attachView(view);
        view.setTransparentStatusBar();
        view.animateToolbarCollapsing();
        view.setToolbarTitle(R.string.home);
        view.setupViewPager();
    }

    @Override
    @DebugLog
    public void detachView() {
        getView().removeListeners();
        super.detachView();
    }

    @Override
    @DebugLog
    public void registerPlaceUpdates() {
        mLocationOperations = new LocationOperationsImpl(getView().getComponentContext());
        mLocationOperations.registerUpdateCallbacks(this);
    }

    @Override
    @DebugLog
    public void requestPlaceUpdates() {
        mLocationOperations.connectApiClient();
    }

    @Override
    @DebugLog
    public void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates) {
        if (locationSettingsStates.isLocationUsable()) {
            mLocationOperations.retrieveLastKnownPlace();
            mLocationOperations.scheduleLocationUpdates();
        } else {
            getView().showSnakeBar(R.string.unable_to_get_location, R.string.dismiss, null);
        }
    }

    @Override
    @DebugLog
    public void stopPlaceUpdates() {
        mLocationOperations.removeLocationUpdates();
        mLocationOperations.disconnectApiClient();
    }

    @Override
    @DebugLog
    public void unregisterPlaceUpdates() {
        mLocationOperations.unregisterUpdateCallbacks();
        mLocationOperations = null;
    }

    @Override
    @DebugLog
    public void onApiClientConnected() {
        mLocationOperations.checkLocationSettings();
    }

    @Override
    @DebugLog
    public void onLocationSettingsResult(LocationSettingsResult locationSettingsResult) {
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
                getView().showSnakeBar(R.string.unable_to_get_location, R.string.dismiss, null);
                break;
        }
    }

    @Override
    @DebugLog
    public void onGotLastKnownPlace(Place lastKnownPlace) {
        getView().updateAddressText(lastKnownPlace.getAddress());
    }

    @Override
    @DebugLog
    public void onLocationUpdated(Location newLocation) {
        if (tempLocation != null && newLocation.distanceTo(tempLocation) < 100) {
            return;
        }
        tempLocation = newLocation;
        mLocationOperations.getCurrentPlace(true, (place, operationStatus) -> {
            if (place != null && operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                getView().updateAddressText(place.getAddress());
            }
        });
    }
}
