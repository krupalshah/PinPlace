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

package com.droidexperiments.android.pinplace.activities.home;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.activities.base.BaseActivity;
import com.droidexperiments.android.pinplace.impl.operations.LocationOperationsImpl;
import com.droidexperiments.android.pinplace.impl.presenters.HomeActivityPresenterImpl;
import com.droidexperiments.android.pinplace.interfaces.listeners.PlaceUpdatesListener;
import com.droidexperiments.android.pinplace.interfaces.operations.LocationOperations;
import com.droidexperiments.android.pinplace.interfaces.presenters.home.HomeActivityPresenter;
import com.droidexperiments.android.pinplace.models.Place;
import com.droidexperiments.android.pinplace.utilities.PermissionsHelper;

import java.util.Arrays;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 */
public final class HomeActivity extends BaseActivity implements PlaceUpdatesListener {

    private static final String TAG = "HomeActivity";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private HomeActivityPresenter mHomePresenter;
    private LocationOperations mLocationOperations;
    private PermissionsHelper mPermissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPermissionsHelper.askPermissionsIfNotGranted(this, LOCATION_PERMISSION_REQUEST_CODE, LOCATION_PERMISSIONS)) {
            mLocationOperations.startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (mPermissionsHelper.checkGrantResultsAndShowRationale(this, LOCATION_PERMISSION_REQUEST_CODE, grantResults, R.string.rationale_access_location, LOCATION_PERMISSIONS)) {
                    mLocationOperations.startLocationUpdates();
                }
                break;

            default:
                Log.e(TAG, "onRequestPermissionsResult() : switch fell under default case " + "requestCode = [" + requestCode + "], permissions = [" + Arrays.toString(permissions) + "], grantResults = [" + Arrays.toString(grantResults) + "]");
                break;
        }
    }

    @Override
    protected void onStop() {
        mLocationOperations.stopLocationUpdates();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocationOperations.unregisterPlaceListener();
        mHomePresenter.detachActivity(this);
        super.onDestroy();
    }

    @Override
    protected void initComponents() {
        mHomePresenter = new HomeActivityPresenterImpl(this);
        mHomePresenter.attachActivity(this);
        mHomePresenter.animateToolbarCollapsing();
        mHomePresenter.setTransparentStatusBar();
        mHomePresenter.setupViewPager();

        mPermissionsHelper = mHomePresenter.providePermissionsHelper();

        mLocationOperations = new LocationOperationsImpl(this);
        mLocationOperations.registerPlaceListener(this);
    }


    @Override
    public void onGotLastKnownPlace(Place lastKnownPlace) {
        Log.d(TAG, "onGotLastKnownPlace() called with " + "lastKnownPlace = [" + lastKnownPlace + "]");
    }

    @Override
    public void onLocationUpdated(Location newLocation) {
        Log.d(TAG, "onLocationUpdated() called with " + "newLocation = [" + newLocation + "]");
    }
}
