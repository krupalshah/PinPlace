package com.droidexperiments.android.pinplace.asynctasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.droidexperiments.android.pinplace.interfaces.callbacks.AsyncTaskCallback;
import com.droidexperiments.android.pinplace.utilities.AppUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public class FetchAddressTask extends AsyncTask<Void, Void, String> {

    private double mLatitude, mLongitude;
    private AsyncTaskCallback mCallback;
    private Context mContext;

    public FetchAddressTask(Context context, double latitude, double longitude, @NonNull AsyncTaskCallback asyncTaskCallback) {
        mContext = context.getApplicationContext();
        mLatitude = latitude;
        mLongitude = longitude;
        mCallback = asyncTaskCallback;
    }

    @Override
    protected String doInBackground(Void... params) {
        String strAddress = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses != null) {
                strAddress = AppUtils.generateAddressLine(addresses.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAddress;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mCallback.onAsyncTaskCompleted(result);
    }
}
