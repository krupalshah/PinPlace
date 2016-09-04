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

package com.experiments.core.helpers.network;

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
 * <p>
 * manages network connectivity changes
 */
public class NetworkUpdatesManger {

    private final Context context;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver connectivityChangeReceiver;

    public NetworkUpdatesManger(Context context) {
        this.context = context;
    }

    /**
     * registers network receiver.
     *
     * @param networkUpdatesListener callback for network updates
     */
    @DebugLog
    public void registerNetworkReceiver(final NetworkUpdatesListener networkUpdatesListener) {
        //registering receiver on connectivity action
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isInternetAvailable()) {
                    networkUpdatesListener.onInternetConnected();
                } else {
                    networkUpdatesListener.onInternetDisconnected();
                }
            }
        };
        context.registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * unregisters network receiver<br/>
     * also unregisters network update listener
     */
    @DebugLog
    public void unregisterNetworkReceiver() {
        context.unregisterReceiver(connectivityChangeReceiver);
        connectivityChangeReceiver = null;
        connectivityManager = null;
    }


    /**
     * checks if internet is available
     *
     * @return ture if available, false otherwise
     */
    @DebugLog
    private boolean isInternetAvailable() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
