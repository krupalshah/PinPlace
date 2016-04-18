package com.droidexperiments.android.pinplace.interfaces.operations;

import android.support.annotation.NonNull;

import com.droidexperiments.android.pinplace.interfaces.callbacks.GetPlaceCallback;
import com.droidexperiments.android.pinplace.interfaces.listeners.PlaceUpdatesListener;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public interface LocationOperations {

    void registerPlaceListener(PlaceUpdatesListener placeUpdatesListener);

    void startLocationUpdates();

    void getCurrentPlace(boolean needsUpdatedAddress, @NonNull GetPlaceCallback callback);

    void stopLocationUpdates();

    void unregisterPlaceListener();
}
