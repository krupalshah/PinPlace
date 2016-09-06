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

package com.experiments.core.utility;

import android.content.Context;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 * <p>
 * base class for all utility classes
 */
public abstract class BaseUtils {

    public static final String COMMA_SEPARATOR = ",";

    /**
     * generates readable comma saperated address string from parts
     *
     * @param address address object
     * @return address string
     */
    @DebugLog
    @NonNull
    public static String generateAddressLine(@NonNull Address address) {
        String addressLine = null;
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressLine += address.getAddressLine(i);
        }
        String city = address.getLocality();
        String state = address.getAdminArea();
        String country = address.getCountryName();
        String postalCode = address.getPostalCode();

        //put parts in array
        String[] resultLines = new String[]{addressLine, city, state, country, postalCode};

        //build string from parts
        StringBuilder resultBuilder = new StringBuilder();

        int length = resultLines.length;
        for (int i = 0; i < length; i++) {
            if (resultLines[i] == null) continue;
            if (i == length - 1) { //if last part - append without comma
                resultBuilder.append(resultLines[i]);
            } else { //else append with comma
                resultBuilder.append(resultLines[i]).append(COMMA_SEPARATOR);
            }
        }
        return resultBuilder.toString();
    }

    /**
     * checks if internet is available
     *
     * @return ture if available, false otherwise
     */
    @DebugLog
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
