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

package com.droidexperiments.android.pinplace.operations.location;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.droidexperiments.android.pinplace.AppConfig;
import com.droidexperiments.android.pinplace.models.Place;
import com.droidexperiments.android.pinplace.operations.network.NetworkOperations;
import com.droidexperiments.android.pinplace.operations.network.NetworkOperationsImpl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public final class LocationOperationsImpl implements LocationOperations, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationOperationsImpl";

    private Context mContext;
    private Place mCurrentPlace;
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
    public void registerUpdateCallbacks(@NonNull PlaceUpdatesListener placeUpdatesListener) {
        Log.d(TAG, "registerUpdateCallbacks ");
        mPLacePlaceUpdatesListener = placeUpdatesListener;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void connectApiClient() {
        Log.d(TAG, "connectApiClient ");
        mGoogleApiClient.connect();
    }

    @Override
    public void checkLocationSettings() {
        Log.d(TAG, "checkLocationSettings ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(AppConfig.LocationUpdates.NORMAL_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(AppConfig.LocationUpdates.FASTEST_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();

        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);
        pendingResult.setResultCallback(mPLacePlaceUpdatesListener::onLocationSettingsResult);
    }

    @Override
    public void retrieveLastKnownPlace() {
        Log.d(TAG, "retrieveLastKnownPlace ");
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastKnownLocation != null) {
            mCurrentPlace.setLatitude(lastKnownLocation.getLatitude());
            mCurrentPlace.setLongitude(lastKnownLocation.getLongitude());

            getCurrentPlace(true, (place, operationStatus) -> {
                Log.d(TAG, "onGotPlace() called with " + "place = [" + place + "], operationStatus = [" + operationStatus + "]");
                if (place != null && operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                    mPLacePlaceUpdatesListener.onGotLastKnownPlace(place);
                }
            });
        }
    }

    @Override
    public void scheduleLocationUpdates() {
        Log.d(TAG, "scheduleLocationUpdates ");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void removeLocationUpdates() {
        Log.d(TAG, "removeLocationUpdates ");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (mFetchAddressTask != null) {
            if (mFetchAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
                mFetchAddressTask.cancel(true);
            }
            mFetchAddressTask = null;
        }
    }

    @Override
    public void disconnectApiClient() {
        Log.d(TAG, "disconnectApiClient ");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void unregisterUpdateCallbacks() {
        mPLacePlaceUpdatesListener = null;
        mNetworkOperations = null;
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
    public void getCurrentPlace(boolean needsUpdatedAddress, @NonNull final GetPlaceCallback callback) {
        Log.d(TAG, "getCurrentPlace() called with " + "needsUpdatedAddress = [" + needsUpdatedAddress + "], callback = [" + callback + "]");
        if (!needsUpdatedAddress) { //does not need place with updated address - just need location not address
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
            return;
        }
        if (mNetworkOperations == null) mNetworkOperations = new NetworkOperationsImpl(mContext);
        if (!mNetworkOperations.isInternetAvailable()) { //needs address update but no internet
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_NO_NETWORK);
            return;
        }
        double lat = mCurrentPlace.getLatitude(), lng = mCurrentPlace.getLongitude();
        if (mFetchAddressTask != null) {
            if (mFetchAddressTask.getStatus() == AsyncTask.Status.RUNNING || mFetchAddressTask.getStatus() == AsyncTask.Status.PENDING) {
                callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_PREV_TASK_PENDING);
                return;
            }
        }
        mFetchAddressTask = new FetchAddressTask(mContext, lat, lng, result -> {
            Log.d(TAG, "onAsyncOperationCompleted() called with " + "result = [" + result + "]");
            if (TextUtils.isEmpty(result)) {
                callback.onGotPlace(null, GetPlaceCallback.STATUS_UNKNOWN_FAILURE);
            } else {
                mCurrentPlace.setAddress(result);
                callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
            }
        });
        mFetchAddressTask.execute();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called with " + "bundle = [" + bundle + "]");
        mPLacePlaceUpdatesListener.onApiClientConnected();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged() called with " + "location = [" + location + "]");
        mCurrentPlace.setLatitude(location.getLatitude());
        mCurrentPlace.setLongitude(location.getLongitude());
        mPLacePlaceUpdatesListener.onLocationUpdated(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended() called with " + "i = [" + i + "]");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed() called with " + "connectionResult = [" + connectionResult + "]");
    }

}
