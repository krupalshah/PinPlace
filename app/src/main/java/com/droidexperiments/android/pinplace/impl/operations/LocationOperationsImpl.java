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

package com.droidexperiments.android.pinplace.impl.operations;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.async.FetchAddressTask;
import com.droidexperiments.android.pinplace.config.AppConfig;
import com.droidexperiments.android.pinplace.interfaces.callbacks.GetPlaceCallback;
import com.droidexperiments.android.pinplace.interfaces.listeners.PlaceUpdatesListener;
import com.droidexperiments.android.pinplace.interfaces.operations.LocationOperations;
import com.droidexperiments.android.pinplace.models.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public final class LocationOperationsImpl implements LocationOperations, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationOperationsImpl";

    private final Context mContext;
    private final Place mCurrentPlace;
    private GoogleApiClient mGoogleApiClient;
    private PlaceUpdatesListener mPLacePlaceUpdatesListener;
    private FetchAddressTask mFetchAddressTask;

    public LocationOperationsImpl(Context context) {
        this.mContext = context.getApplicationContext();
        mCurrentPlace = new Place();
    }

    @Override
    public void registerPlaceListener(PlaceUpdatesListener placeUpdatesListener) {
        mPLacePlaceUpdatesListener = placeUpdatesListener;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void startLocationUpdates() {
        mGoogleApiClient.connect();
    }

    @Override
    public void getCurrentPlace(boolean needsUpdatedAddress, @NonNull GetPlaceCallback callback) {
        Log.d(TAG, "getCurrentPlace() called with " + "needsUpdatedAddress = [" + needsUpdatedAddress + "], callback = [" + callback + "]");
        if (!needsUpdatedAddress) { //does not need place with updated address - just need location not address
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
            return;
        }
        if (!new NetworkOperationsImpl(mContext).isInternetAvailable()) { //needs address update but no internet
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
        mFetchAddressTask = new FetchAddressTask(mContext, lat, lng, (result) -> {
            mCurrentPlace.setAddress(!TextUtils.isEmpty(result) ? result : mContext.getString(R.string.unknown));
            callback.onGotPlace(mCurrentPlace, GetPlaceCallback.STATUS_SUCCESS);
        });
        mFetchAddressTask.execute();
    }

    @Override
    public void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        if (mFetchAddressTask != null) {
            if (mFetchAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
                mFetchAddressTask.cancel(true);
            }
            mFetchAddressTask = null;
        }
    }

    @Override
    public void unregisterPlaceListener() {
        mPLacePlaceUpdatesListener = null;
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
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(AppConfig.LocationIntervals.NORMAL_LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(AppConfig.LocationIntervals.FASTEST_LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastKnownLocation != null) {
            mCurrentPlace.setLatitude(lastKnownLocation.getLatitude());
            mCurrentPlace.setLongitude(lastKnownLocation.getLongitude());

            getCurrentPlace(true, (place, operationStatus) -> {
                if (operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                    mPLacePlaceUpdatesListener.onGotLastKnownPlace(place);
                }
            });
        }
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
