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

package com.experiments.common.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import hugo.weaving.DebugLog;


/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public class NetworkOperationsImpl implements NetworkOperations {

    private final Context mContext;
    private ConnectivityManager mConnectivityManager;
    private BroadcastReceiver mConnectivityChangeReceiver;

    public NetworkOperationsImpl(Context context) {
        mContext = context;
    }

    @Override
    @DebugLog
    public void registerNetworkReceiver(final NetworkUpdatesListener networkUpdatesListener) {
        //registering receiver on connectivity action
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mConnectivityChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isInternetAvailable()) {
                    networkUpdatesListener.onInternetConnected();
                } else {
                    networkUpdatesListener.onInternetDisconnected();
                }
            }
        };
        mContext.registerReceiver(mConnectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    @DebugLog
    public boolean isInternetAvailable() {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    @DebugLog
    public void unregisterNetworkReceiver() {
        mContext.unregisterReceiver(mConnectivityChangeReceiver);
        mConnectivityChangeReceiver = null;
        mConnectivityManager = null;
    }
}
