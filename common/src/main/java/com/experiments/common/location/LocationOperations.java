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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.experiments.common.config.BaseConfig;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * contractor interface for operations related to location updates
 */
public interface LocationOperations {

    /**
     * registers update callbacks and initializes google api client for fused location updates
     *
     * @param placeUpdatesListener place updates listener
     */
    void registerUpdateCallbacks(@NonNull PlaceUpdatesListener placeUpdatesListener);

    /**
     * connects google api client
     */
    void connectApiClient();

    /**
     * checks location settings and gives callback {@link PlaceUpdatesListener#onLocationSettingsResult(LocationSettingsResult)}
     */
    void checkLocationSettings();

    /**
     * fetches the last known place
     *
     * @param needsAddress true if it also need to fetch the address for the place, false if only interested in location(lat-lng).
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void retrieveLastKnownPlace(boolean needsAddress);

    /**
     * schedules location updates accordingly intervals defines in {@link BaseConfig.LocationUpdates}
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void scheduleLocationUpdates();

    /**
     * to get current place
     *
     * @param needsAddress true if it also need to fetch the address for the place, false if only interested in location (lat-lng).
     * @param callback     callback when gets the place
     */
    void getCurrentPlace(boolean needsAddress, @NonNull GetPlaceCallback callback);

    /**
     * removes location updates from fused location api provider<br/>
     * cancels any async operation if running
     */
    void removeLocationUpdates();

    /**
     * disconnects google api client. if connected
     */
    void disconnectApiClient();

    /**
     * unregisters listeners for google api client and for place updates
     */
    void unregisterUpdateCallbacks();
}
