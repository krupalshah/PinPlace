package com.droidexperiments.android.pinplace.interfaces.operations;

import com.droidexperiments.android.pinplace.interfaces.listeners.NetworkUpdatesListener;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface NetworkOperations {

    void registerNetworkReceiver(NetworkUpdatesListener networkUpdatesListener);

    boolean isInternetAvailable();

    void unregisterNetworkReceiver();
}
