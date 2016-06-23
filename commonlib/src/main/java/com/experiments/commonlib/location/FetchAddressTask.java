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

package com.experiments.commonlib.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.experiments.commonlib.R;
import com.experiments.commonlib.interfaces.AsyncTaskCallback;
import com.experiments.commonlib.utilities.BaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public class FetchAddressTask extends AsyncTask<Void, Void, String> {

    private Geocoder mGeocoder;
    private double mLatitude, mLongitude;
    private AsyncTaskCallback<String> mCallback;
    private String strAddress;
    private Context mContext;

    public FetchAddressTask(Context context, double latitude, double longitude, @NonNull AsyncTaskCallback<String> asyncTaskCallback) {
        mContext = context.getApplicationContext();
        mLatitude = latitude;
        mLongitude = longitude;
        mCallback = asyncTaskCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mGeocoder = new Geocoder(mContext, Locale.getDefault());
        strAddress = mContext.getString(R.string.unknown);
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses != null) {
                strAddress = BaseUtils.generateAddressLine(addresses.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAddress;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mCallback.onAsyncOperationCompleted(result);
    }
}