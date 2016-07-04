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

package com.experiments.common.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.experiments.common.config.BaseConfig;
import com.experiments.common.network.NetworkOperations;
import com.experiments.common.network.NetworkOperationsImpl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * implementation of {@link LocationOperations}
 */
public class LocationOperationsImpl implements LocationOperations, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final Context mContext;
    private final Place mCurrentPlace;
    private GoogleApiClient mGoogleApiClient;
    private PlaceUpdatesListener mPLacePlaceUpdatesListener;
    private FetchAddressTask mFetchAddressTask;
    private NetworkOperations mNetworkOperations;
    private LocationRequest mLocationRequest;

    public LocationOperationsImpl(Context context) {
        mContext = context;
        mCurrentPlace = new Place();
    }

    @Override
    @DebugLog
    public void registerUpdateCallbacks(@NonNull PlaceUpdatesListener placeUpdatesListener) {
        mPLacePlaceUpdatesListener = placeUpdatesListener;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    @DebugLog
    public void connectApiClient() {
        mGoogleApiClient.connect();
    }

    @Override
    @DebugLog
    public void checkLocationSettings() {
        //creating location request and configuring intervals
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(BaseConfig.LocationUpdates.NORMAL_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(BaseConfig.LocationUpdates.FASTEST_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //checking for location settings
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();
        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);
        pendingResult.setResultCallback(mPLacePlaceUpdatesListener::onLocationSettingsResult);
    }

    @Override
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void retrieveLastKnownPlace(boolean needsAddress) {

        //getting last known location from fused location provider
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastKnownLocation == null) return;

        //setting latlng to place object and calling getCurrentPlace()
        mCurrentPlace.setLatitude(lastKnownLocation.getLatitude());
        mCurrentPlace.setLongitude(lastKnownLocation.getLongitude());

        //giving updated place object from getCurrentPlace in callback
        getCurrentPlace(needsAddress, (place, operationStatus) -> {
            if (place != null && operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                mPLacePlaceUpdatesListener.onGotLastKnownPlace(place);
            }
        });
    }

    @Override
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void scheduleLocationUpdates() {
        //scheduling location updates for created location request
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    @DebugLog
    public void removeLocationUpdates() {
        //reoving location updates from fused location provider
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        //canceling address fetching task if running
        if (mFetchAddressTask != null) {
            if (mFetchAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
                mFetchAddressTask.cancel(true);
            }
            mFetchAddressTask = null;
        }
    }

    @Override
    @DebugLog
    public void disconnectApiClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    @DebugLog
    public void unregisterUpdateCallbacks() {
        mPLacePlaceUpdatesListener = null;
        mNetworkOperations = null;

        //unregistering callbacks associated with google api client
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnectionCallbacksRegistered(this)) {
                mGoogleApiClient.unregisterConnectionCallbacks(this);
            }
            if (mGoogleApiClient.isConnectionFailedListenerRegistered(this)) {
                mGoogleApiClient.unregisterConnectionFailedListener(this);
            }
            mGoogleApiClient = null;
        }
    }

    @Override
    @DebugLog
    public void getCurrentPlace(boolean needsAddress, @NonNull final GetPlaceCallback callback) {
        if (!needsAddress) { //does not need place with updated address - just need location not address
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
            return;
        }
        if (mNetworkOperations == null) mNetworkOperations = new NetworkOperationsImpl(mContext);
        if (!mNetworkOperations.isInternetAvailable()) { //needs address update but no internet
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_NO_NETWORK);
            return;
        }
        double lat = mCurrentPlace.getLatitude(), lng = mCurrentPlace.getLongitude();
        if (mFetchAddressTask != null) { //if previous address fetching task is already running : block executing another task
            if (mFetchAddressTask.getStatus() == AsyncTask.Status.RUNNING || mFetchAddressTask.getStatus() == AsyncTask.Status.PENDING) {
                callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_PREV_TASK_PENDING);
                return;
            }
        }

        //executing address fetching task
        mFetchAddressTask = new FetchAddressTask(mContext, lat, lng, result -> {
            if (TextUtils.isEmpty(result)) {
                callback.onGotPlace(null, GetPlaceCallback.STATUS_UNKNOWN_FAILURE); //got empty address in callback
            } else {
                mCurrentPlace.setAddress(result);
                callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
            }
        });
        mFetchAddressTask.execute();
    }

    @Override
    @DebugLog
    public void onConnected(Bundle bundle) {
        mPLacePlaceUpdatesListener.onApiClientConnected();
    }

    @Override
    @DebugLog
    public void onLocationChanged(Location location) {
        //when location is changed, assigning latlng to current place object
        mCurrentPlace.setLatitude(location.getLatitude());
        mCurrentPlace.setLongitude(location.getLongitude());

        //dispatching callback about location update
        mPLacePlaceUpdatesListener.onLocationUpdated(location);
    }

    @Override
    @DebugLog
    public void onConnectionSuspended(int i) {
    }

    @Override
    @DebugLog
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
