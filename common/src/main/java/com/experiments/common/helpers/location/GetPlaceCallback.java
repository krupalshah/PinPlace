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

package com.experiments.common.helpers.location;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * callback from {@link LocationUpdatesHelper#getCurrentPlace}
 */
public interface GetPlaceCallback {

    //constants for indicating state of operation. see @GetPlaceOperationStatus
    int STATUS_SUCCESS = 1;
    int STATUS_NO_NETWORK = 2;
    int STATUS_UNKNOWN_FAILURE = 4;

    /**
     * called when operating for getting place is completed
     *
     * @param place           place object
     * @param operationStatus operation status defined in {@link GetPlaceOperationStatus}
     */
    void onGotPlace(@Nullable PlaceDataWrapper place, @GetPlaceOperationStatus int operationStatus);

    /**
     * defines following constants for get place operation:
     * <p>
     * STATUS_SUCCESS - if successful operation<br/>
     * STATUS_NO_NETWORK - if network was not available during operation<br/>
     * STATUS_UNKNOWN_FAILURE - unknown failure while getting place (such as address could not be fetched or any other exception)
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_NO_NETWORK, STATUS_SUCCESS, STATUS_UNKNOWN_FAILURE})
    @interface GetPlaceOperationStatus {
    }
}
