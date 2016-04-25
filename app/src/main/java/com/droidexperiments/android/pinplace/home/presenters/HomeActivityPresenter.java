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

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.base.presenters.BasePresenterImpl;
import com.droidexperiments.android.pinplace.home.contracts.HomeActivityContract;
import com.droidexperiments.android.pinplace.models.Place;
import com.droidexperiments.android.pinplace.operations.location.GetPlaceCallback;
import com.droidexperiments.android.pinplace.operations.location.LocationOperations;
import com.droidexperiments.android.pinplace.operations.location.LocationOperationsImpl;
import com.droidexperiments.android.pinplace.operations.location.PlaceUpdatesListener;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public class HomeActivityPresenter extends BasePresenterImpl<HomeActivityContract.View> implements HomeActivityContract.Presenter, PlaceUpdatesListener {

    private static final String TAG = "HomeActivityPresenter";

    private LocationOperations mLocationOperations;

    @Override
    public void attachView(HomeActivityContract.View view) {
        super.attachView(view);
        view.setTransparentStatusBar();
        view.animateToolbarCollapsing();
        view.setToolbarTitle(R.string.home);
        view.setupViewPager();
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void registerPlaceUpdates() {
        mLocationOperations = new LocationOperationsImpl(getView().getComponentContext());
        mLocationOperations.registerPlaceUpdateCallbacks(this);
    }

    @Override
    public void startPlaceUpdates() {
        checkPlaceUpdatesRegistered();
        mLocationOperations.startLocationUpdates();
    }

    @Override
    public void stopPlaceUpdates() {
        checkPlaceUpdatesRegistered();
        mLocationOperations.stopLocationUpdates();
    }

    @Override
    public void unregisterPlaceUpdates() {
        checkPlaceUpdatesRegistered();
        mLocationOperations.unregisterPlaceUpdateCallbacks();
        mLocationOperations = null;
    }

    @Override
    public void onGotLastKnownPlace(Place lastKnownPlace) {
        getView().updateAddressText(lastKnownPlace.getAddress());
    }

    @Override
    public void onLocationUpdated(Location newLocation) {
        mLocationOperations.getCurrentPlace(true, (place, operationStatus) -> {
            if (operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                mView.updateAddressText(place.getAddress());
            }
        });
    }

    private void checkPlaceUpdatesRegistered() {
        if (mLocationOperations == null) {
            throw new IllegalStateException("Place updates not registered! Did you forgot to call registerPlaceUpdates() before?");
        }
    }

}
