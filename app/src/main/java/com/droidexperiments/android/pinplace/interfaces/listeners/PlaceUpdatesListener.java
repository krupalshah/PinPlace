package com.droidexperiments.android.pinplace.interfaces.listeners;

import android.location.Location;

import com.droidexperiments.android.pinplace.models.Place;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface PlaceUpdatesListener {

    void onGotLastKnownPlace(Place lastKnownPlace);

    void onLocationUpdated(Location newLocation);
}
