/*
 *   Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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

package com.experiments.common.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.experiments.common.listeners.AsyncTaskListener;
import com.experiments.common.utilities.BaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 * <p>
 * fetches address using {@link Geocoder} from given latitude and longitude<br/>
 * gives address in callback. address can be null
 */
public class FetchAddressTask extends AsyncTask<Void, Void, String> {

    private final double latitude;
    private final double longitude;
    private final AsyncTaskListener<String> asyncTaskListener;
    private final Context context;
    private Geocoder geoCoder;
    private String strAddress;

    @DebugLog
    public FetchAddressTask(Context context, double latitude, double longitude, @NonNull AsyncTaskListener<String> asyncTaskListener) {
        this.context = context.getApplicationContext();
        this.latitude = latitude;
        this.longitude = longitude;
        this.asyncTaskListener = asyncTaskListener;
    }

    @Override
    @DebugLog
    protected void onPreExecute() {
        super.onPreExecute();
        geoCoder = new Geocoder(context, Locale.ENGLISH);
    }

    @Override
    @DebugLog
    protected String doInBackground(Void... params) {
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null || addresses.isEmpty()) return strAddress;
            strAddress = BaseUtils.generateAddressLine(addresses.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAddress;
    }

    @Override
    @DebugLog
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        asyncTaskListener.onAsyncOperationCompleted(result);
    }
}
