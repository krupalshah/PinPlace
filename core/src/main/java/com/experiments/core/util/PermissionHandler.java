/*
 *
 *  Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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

package com.experiments.core.util;

import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.experiments.core.R;
import com.experiments.core.application.activities.BaseActivity;
import com.experiments.core.application.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 03-Apr-16
 * <p>
 * handles permission checking for marshmallow and above
 */
public class PermissionHandler {

    /**
     * list of non granted permissions<br/>
     * should be cleared every time checking new permissions
     */
    private final List<String> revokedPermissions = new ArrayList<>();

    private PermissionHandler() {
        //avoiding direct instances. use factory method instead.
    }

    public static PermissionHandler create() {
        return new PermissionHandler();
    }


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
        revokedPermissions.clear();
        for (String permission : permissionsToCheck) { //iterating through permission given in params
            if (ContextCompat.checkSelfPermission(baseActivity, permission) == android.support.v4.content.PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration
                continue;
            }
            revokedPermissions.add(permission); //otherwise add it to list
        }
        if (revokedPermissions.size() > 0) { //request only non granted permissions
            ActivityCompat.requestPermissions(baseActivity, revokedPermissions.toArray(new String[revokedPermissions.size()]), requestCode);
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
        revokedPermissions.clear();
        for (String permission : permissionsToCheck) { //iterating through permission given in params
            if (ContextCompat.checkSelfPermission(baseFragment.getContext(), permission) == android.support.v4.content.PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration
                continue;
            }
            revokedPermissions.add(permission); //otherwise add it to list
        }
        if (revokedPermissions.size() > 0) { //request only non granted permissions
            baseFragment.requestPermissions(revokedPermissions.toArray(new String[revokedPermissions.size()]), requestCode);
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
        //initializing granted flag to false is mandatory since array of grant results can be empty
        boolean allPermissionGranted = false;

        for (int grantResult : grantResults) { //iterating through grant results
            if (grantResult != android.support.v4.content.PermissionChecker.PERMISSION_GRANTED) { //if granted - skip iteration

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
                //if granted, don't forgot to dismiss any previous snake bar showing for rationale
                baseActivity.hideSnakeBar();
                allPermissionGranted = true;
            }
        }
        return allPermissionGranted;
    }

}
