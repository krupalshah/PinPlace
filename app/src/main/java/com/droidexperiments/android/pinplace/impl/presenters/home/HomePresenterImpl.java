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

package com.droidexperiments.android.pinplace.impl.presenters.home;

import android.content.Context;
import android.location.Location;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.impl.operations.LocationOperationsImpl;
import com.droidexperiments.android.pinplace.impl.presenters.base.BasePresenterImpl;
import com.droidexperiments.android.pinplace.interfaces.callbacks.GetPlaceCallback;
import com.droidexperiments.android.pinplace.interfaces.contracts.home.HomeActivityContract;
import com.droidexperiments.android.pinplace.interfaces.listeners.PlaceUpdatesListener;
import com.droidexperiments.android.pinplace.interfaces.operations.LocationOperations;
import com.droidexperiments.android.pinplace.models.Place;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public class HomePresenterImpl extends BasePresenterImpl<HomeActivityContract.View> implements HomeActivityContract.Presenter, PlaceUpdatesListener {

    private static final String TAG = "HomePresenterImpl";

    private Context mContext;
    private LocationOperations mLocationOperations;

    public HomePresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

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
        mLocationOperations = new LocationOperationsImpl(mContext);
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
