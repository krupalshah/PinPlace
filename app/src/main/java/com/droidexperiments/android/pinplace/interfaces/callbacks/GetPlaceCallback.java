package com.droidexperiments.android.pinplace.interfaces.callbacks;

import android.support.annotation.IntDef;

import com.droidexperiments.android.pinplace.models.Place;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public interface GetPlaceCallback {
    int STATUS_NO_NETWORK = 1;
    int STATUS_PREV_TASK_PENDING = 2;
    int STATUS_SUCCESS = 3;

    @IntDef({STATUS_NO_NETWORK, STATUS_PREV_TASK_PENDING, STATUS_SUCCESS})
    @interface GetPlaceOperationStatus {}

    void onGotPlace(Place place, @GetPlaceOperationStatus int operationStatus);
}
