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

package com.droidexperiments.android.pinplace.utilities;

import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.base.activities.BaseActivity;

/**
 * Author : Krupal Shah
 * Date : 03-Apr-16
 */
public final class PermissionsHelper {

    /**
     * checks permission and asks if not granted
     *
     * @param baseActivity       activity
     * @param requestCode        permission request code
     * @param permissionsToCheck permissions to check and ask
     * @return true if all asked permissionsToCheck are granted; false otherwise
     */
    public final boolean askPermissionsIfNotGranted(BaseActivity baseActivity, int requestCode, String... permissionsToCheck) {
        boolean hasPermissionGranted = true;
        for (String permission : permissionsToCheck) {
            if (ContextCompat.checkSelfPermission(baseActivity, permission) == PermissionChecker.PERMISSION_GRANTED) {
                continue;
            }
            ActivityCompat.requestPermissions(baseActivity, permissionsToCheck, requestCode);
            hasPermissionGranted = false;
            break;
        }
        return hasPermissionGranted;
    }

    /**
     * checks grant results from {@link ActivityCompat.OnRequestPermissionsResultCallback}.
     * <br> Shows snake bar with rationale message if permission denied.
     * <br> Asks again if user allow on showing rationale
     *
     * @param baseActivity     activity
     * @param requestCode      permission request code
     * @param grantResults     grant results from {@link ActivityCompat.OnRequestPermissionsResultCallback}
     * @param rationaleMessage message resource id for rationale
     * @param permissionsAsked asked permissions for given request code
     * @return true if all permissions asked have been granted; false otherwise
     */
    public final boolean checkGrantResultsAndShowRationaleIfDenied(final BaseActivity baseActivity, final int requestCode, int[] grantResults, @StringRes int rationaleMessage, final String... permissionsAsked) {
        boolean allPermissionGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult == PermissionChecker.PERMISSION_GRANTED) {
                continue;
            }
            for (String permission : permissionsAsked) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, permission)) {
                    continue;
                }
                baseActivity.showSnakeBarAtBottom(rationaleMessage, R.string.allow, view -> {
                    askPermissionsIfNotGranted(baseActivity, requestCode, permissionsAsked);
                });
                break;
            }
            allPermissionGranted = false;
            break;
        }

        return allPermissionGranted;
    }

}
