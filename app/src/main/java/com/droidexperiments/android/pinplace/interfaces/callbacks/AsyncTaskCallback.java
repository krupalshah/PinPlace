package com.droidexperiments.android.pinplace.interfaces.callbacks;

/**
 * Author : Krupal Shah
 * Date : 09-Apr-16
 */
public interface AsyncTaskCallback {
    <T> void onAsyncTaskCompleted(T result);
}
