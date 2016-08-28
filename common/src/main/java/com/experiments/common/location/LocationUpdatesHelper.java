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
import com.experiments.common.network.NetworkUpdatesHelper;
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
 * manages location updates
 */
public class LocationUpdatesHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final Context context;
    private final Place currentPlace;
    private GoogleApiClient googleApiClient;
    private LocationUpdatesListener locationUpdatesListener;
    private FetchAddressTask fetchAddressTask;
    private NetworkUpdatesHelper networkUpdatesHelper;
    private LocationRequest locationRequest;

    public LocationUpdatesHelper(Context context) {
        this.context = context;
        currentPlace = new Place();
    }

    /**
     * registers update callbacks and initializes google api client for fused location updates
     *
     * @param locationUpdatesListener place updates listener
     */
    @DebugLog
    public void registerUpdateCallbacks(@NonNull LocationUpdatesListener locationUpdatesListener) {
        this.locationUpdatesListener = locationUpdatesListener;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * connects google api client
     */
    @DebugLog
    public void connectApiClient() {
        googleApiClient.connect();
    }

    /**
     * checks location settings and gives callback {@link LocationUpdatesListener#onLocationSettingsResult(LocationSettingsResult)}
     */
    @DebugLog
    public void checkLocationSettings() {
        //creating location request and configuring intervals
        locationRequest = new LocationRequest();
        locationRequest.setInterval(BaseConfig.LocationUpdates.NORMAL_LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(BaseConfig.LocationUpdates.FASTEST_LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //checking for location settings
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest);
        pendingResult.setResultCallback(locationUpdatesListener::onLocationSettingsResult);
    }

    /**
     * fetches the last known place
     *
     * @param needsAddress true if it also need to fetch the address for the place, false if only interested in location(lat-lng).
     */
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void retrieveLastKnownPlace(boolean needsAddress) {

        //getting last known location from fused location provider
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnownLocation == null) return;

        //setting latlng to place object and calling getCurrentPlace()
        currentPlace.setLatitude(lastKnownLocation.getLatitude());
        currentPlace.setLongitude(lastKnownLocation.getLongitude());

        //giving updated place object from getCurrentPlace in callback
        getCurrentPlace(needsAddress, (place, operationStatus) -> {
            if (place != null && operationStatus == GetPlaceCallback.STATUS_SUCCESS) {
                locationUpdatesListener.onGotLastKnownPlace(place);
            }
        });
    }

    /**
     * schedules location updates accordingly intervals defines in {@link BaseConfig.LocationUpdates}
     */
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void scheduleLocationUpdates() {
        //scheduling location updates for created location request
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    /**
     * removes location updates from fused location api provider<br/>
     * cancels any async operation if running
     */
    @DebugLog
    public void removeLocationUpdates() {
        //removing location updates from fused location provider
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
        //canceling address fetching task if running
        if (fetchAddressTask != null) {
            if (fetchAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
                fetchAddressTask.cancel(true);
            }
            fetchAddressTask = null;
        }
    }

    /**
     * disconnects google api client if connected
     */
    @DebugLog
    public void disconnectApiClient() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * unregisters listeners for google api client and for place updates
     */
    @DebugLog
    public void unregisterUpdateCallbacks() {
        locationUpdatesListener = null;
        networkUpdatesHelper = null;

        //unregister callbacks associated with google api client
        if (googleApiClient != null) {
            if (googleApiClient.isConnectionCallbacksRegistered(this)) {
                googleApiClient.unregisterConnectionCallbacks(this);
            }
            if (googleApiClient.isConnectionFailedListenerRegistered(this)) {
                googleApiClient.unregisterConnectionFailedListener(this);
            }
            googleApiClient = null;
        }
    }

    /**
     * to get current place
     *
     * @param needsAddress true if it also need to fetch the address for the place, false if only interested in location (lat-lng).
     * @param callback     callback when gets the place
     */
    @DebugLog
    public void getCurrentPlace(boolean needsAddress, @NonNull final GetPlaceCallback callback) {
        if (!needsAddress) { //does not need place with updated address - just need location not address
            callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_SUCCESS);
            return;
        }
        if (networkUpdatesHelper == null) networkUpdatesHelper = new NetworkUpdatesHelper(context);
        if (!networkUpdatesHelper.isInternetAvailable()) { //needs address update but no internet
            callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_NO_NETWORK);
            return;
        }
        double lat = currentPlace.getLatitude(), lng = currentPlace.getLongitude();
        if (fetchAddressTask != null) { //if previous address fetching task is already running : block executing another task
            if (fetchAddressTask.getStatus() == AsyncTask.Status.RUNNING || fetchAddressTask.getStatus() == AsyncTask.Status.PENDING) {
                callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_PREV_TASK_PENDING);
                return;
            }
        }

        //executing address fetching task
        fetchAddressTask = new FetchAddressTask(context, lat, lng, result -> {
            if (TextUtils.isEmpty(result)) {
                callback.onGotPlace(null, GetPlaceCallback.STATUS_UNKNOWN_FAILURE); //got empty address in callback
            } else {
                currentPlace.setAddress(result);
                callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_SUCCESS);
            }
        });
        fetchAddressTask.execute();
    }

    @Override
    @DebugLog
    public void onConnected(Bundle bundle) {
        locationUpdatesListener.onApiClientConnected();
    }

    @Override
    @DebugLog
    public void onLocationChanged(Location location) {
        //when location is changed, assigning latlng to current place object
        currentPlace.setLatitude(location.getLatitude());
        currentPlace.setLongitude(location.getLongitude());

        //dispatching callback about location update
        locationUpdatesListener.onLocationUpdated(location);
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
