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

package com.experiments.common.utilities;

import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.experiments.common.R;
import com.experiments.common.android.activities.BaseActivity;
import com.experiments.common.android.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 03-Apr-16
 * <p>
 * handles permission checking for marshmallow and above
 */
public class PermissionsChecker {

    /**
     * list of non granted permissions<br/>
     * should be cleared every time checking new permissions
     */
    private final List<String> nonGrantedPermissions = new ArrayList<>();

    /**
     * checks permission and asks if not granted
     *
     * @param baseActivity       activity
     * @param requestCode        permission request code
     * @param permissionsToCheck permissions to check and ask
     * @return true if all asked permissionsToCheck are granted; false otherwise
     * @see #askPermissionsIfNotGranted(BaseFragment, int, String...) for asking permissions from fragment
     */
    @DebugLog
    public final boolean askPermissionsIfNotGranted(BaseActivity baseActivity, int requestCode, String... permissionsToCheck) {
        nonGrantedPermissions.clear();
        for (String permission : permissionsToCheck) { //iterating through permission given in params
            if (ContextCompat.checkSelfPermission(baseActivity, permission) == PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration
                continue;
            }
            nonGrantedPermissions.add(permission); //otherwise add it to list
        }
        if (nonGrantedPermissions.size() > 0) { //request only non granted permissions
            ActivityCompat.requestPermissions(baseActivity, nonGrantedPermissions.toArray(new String[nonGrantedPermissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * checks permission and asks if not granted - same as its activity variant but intended for fragments
     *
     * @param baseFragment       fragment
     * @param requestCode        permission request code
     * @param permissionsToCheck permissions to check and ask
     * @return true if all asked permissionsToCheck are granted; false otherwise
     */
    @DebugLog
    public final boolean askPermissionsIfNotGranted(BaseFragment baseFragment, int requestCode, String... permissionsToCheck) {
        nonGrantedPermissions.clear();
        for (String permission : permissionsToCheck) { //iterating through permission given in params
            if (ContextCompat.checkSelfPermission(baseFragment.getContext(), permission) == PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration
                continue;
            }
            nonGrantedPermissions.add(permission); //otherwise add it to list
        }
        if (nonGrantedPermissions.size() > 0) { //request only non granted permissions
            baseFragment.requestPermissions(nonGrantedPermissions.toArray(new String[nonGrantedPermissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
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
    @DebugLog
    public final boolean checkGrantResults(final BaseActivity baseActivity, final int requestCode, int[] grantResults, @StringRes int rationaleMessage, final String... permissionsAsked) {
        boolean allPermissionGranted = false;
        for (int grantResult : grantResults) { //iterating through grant results
            if (grantResult != PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration

                for (String permission : permissionsAsked) { //if not granted - checking if can show rationale
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, permission)) {
                        continue;
                    }
                    baseActivity.showSnakeBar(rationaleMessage, R.string.allow, view -> { //show rationale snake bar for non-granted permissions and break the rationale checking loop
                        askPermissionsIfNotGranted(baseActivity, requestCode, permissionsAsked);
                    });
                    break;
                }
                allPermissionGranted = false; //otherwise break loop and return false
                break;
            } else {
                allPermissionGranted = true;
            }
        }
        return allPermissionGranted;
    }

}
