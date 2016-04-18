package com.droidexperiments.android.pinplace;

import android.app.Application;
import android.util.Log;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 */
public class TimePassApp extends Application {

    private static final String TAG = "TimePassApp";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory ");
    }
}
