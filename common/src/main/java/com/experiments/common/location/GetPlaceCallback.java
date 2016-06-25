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

package com.experiments.common.location;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.experiments.common.models.Place;


/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public interface GetPlaceCallback {

    int STATUS_SUCCESS = 1;
    int STATUS_NO_NETWORK = 2;
    int STATUS_PREV_TASK_PENDING = 3;
    int STATUS_UNKNOWN_FAILURE = 4;

    void onGotPlace(@Nullable Place place, @GetPlaceOperationStatus int operationStatus);

    @IntDef({STATUS_NO_NETWORK, STATUS_PREV_TASK_PENDING, STATUS_SUCCESS, STATUS_UNKNOWN_FAILURE})
    @interface GetPlaceOperationStatus {
    }
}
