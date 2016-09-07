/*
 *
 *  Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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

package com.experiments.core.domain.helpers.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.experiments.core.config.BaseConfig;
import com.experiments.core.domain.exceptions.NoInternetException;
import com.experiments.core.util.BaseUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * manages location updates
 */
public class LocationUpdatesManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationUpdatesHelper";
    private final Context context;
    private final PlaceDataWrapper currentPlace;
    private GoogleApiClient googleApiClient;
    private LocationUpdatesListener locationUpdatesListener;
    private LocationRequest locationRequest;

    public LocationUpdatesManager(Context context) {
        this.context = context;
        currentPlace = new PlaceDataWrapper();
    }

    /**
     * registers update callbacks and initializes google api client for fused location updates
     *
     * @param locationUpdatesListener place updates listener
     */
    @DebugLog
    public void registerUpdateCallbacks(@NonNull LocationUpdatesListener locationUpdatesListener) {
        this.locationUpdatesListener = locationUpdatesListener;
        googleApiClient = buildGoogleApiClient();
    }

    @DebugLog
    @NonNull
    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
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
        locationRequest = createLocationRequest();

        //checking for location settings
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest);
        pendingResult.setResultCallback(locationUpdatesListener::onLocationSettingsResult);
    }

    @DebugLog
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(BaseConfig.LocationUpdates.NORMAL_LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(BaseConfig.LocationUpdates.FASTEST_LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    /**
     * fetches the last known place
     *
     * @param needsAddress true if it also need to fetch the address for the place, false if only interested in location(lat-lng).
     */
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @SuppressWarnings({"MissingPermission"})
    public void retrieveLastKnownPlace(boolean needsAddress) {

        //getting last known location from fused location provider
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnownLocation == null) return;

        //setting latlng to place object and calling getCurrentPlace()
        currentPlace.setLocationData(lastKnownLocation);

        //giving updated place object from getCurrentPlace in callback
        getCurrentPlace(needsAddress, (place, operationStatus) -> {
            if (place == null) {
                locationUpdatesListener.onErrorGettingLastKnownPlace(new Throwable("place is null"));
                return;
            }
            switch (operationStatus) {
                case GetPlaceCallback.STATUS_SUCCESS:
                    locationUpdatesListener.onGotLastKnownPlace(place);
                    break;

                case GetPlaceCallback.STATUS_NO_NETWORK:
                    locationUpdatesListener.onErrorGettingLastKnownPlace(new NoInternetException());
                    break;

                case GetPlaceCallback.STATUS_UNKNOWN_FAILURE:
                    locationUpdatesListener.onErrorGettingLastKnownPlace(new Throwable("Unknown failure getting place information"));
                    break;
            }

        });
    }

    /**
     * schedules location updates accordingly intervals defines in {@link BaseConfig.LocationUpdates}
     */
    @DebugLog
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @SuppressWarnings({"MissingPermission"})
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
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @SuppressWarnings({"MissingPermission"})
    public void getCurrentPlace(boolean needsAddress, @NonNull final GetPlaceCallback callback) {
        if (!needsAddress) { //does not need place with updated address - just need location not address
            callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_SUCCESS);
            return;
        }
        if (!BaseUtils.isInternetAvailable(context.getApplicationContext())) { //needs address update but no internet
            callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_NO_NETWORK);
            return;
        }

        PendingResult<PlaceLikelihoodBuffer> currentPlaceData = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
        currentPlaceData.setResultCallback(new ResultCallbacks<PlaceLikelihoodBuffer>() {
            @DebugLog
            @Override
            public void onSuccess(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                Log.d(TAG, "onSuccess() place = [" + placeLikelihoods.get(0).getPlace() + "]");

                currentPlace.setPlaceData(placeLikelihoods.get(0).getPlace());
                callback.onGotPlace(currentPlace, GetPlaceCallback.STATUS_SUCCESS);

                placeLikelihoods.release();
            }

            @DebugLog
            @Override
            public void onFailure(@NonNull Status status) {
                Log.d(TAG, "onFailure() : " + "cause = [" + status.getStatusMessage() + "]");
                callback.onGotPlace(null, GetPlaceCallback.STATUS_UNKNOWN_FAILURE); //got empty address in callback
            }
        });
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
        currentPlace.setLocationData(location);

        //dispatching callback about location update
        locationUpdatesListener.onLocationUpdated(location);
    }

    @Override
    @DebugLog
    public void onConnectionSuspended(int i) {
        locationUpdatesListener.onErrorUpdatingLocation(new Throwable("Google api client connection suspended"));
    }

    @Override
    @DebugLog
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationUpdatesListener.onErrorUpdatingLocation(new Throwable("Google api client connection failed"));
    }

}
