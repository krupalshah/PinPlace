package com.droidexperiments.android.pinplace.interfaces.listeners;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 */
public interface NetworkUpdatesListener {
    /**
     * to be called when network becomes available
     */
    void onInternetConnected();

    /**
     * to be called when network becomes unavailable
     */
    void onInternetDisconnected();

}
