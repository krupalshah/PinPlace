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

import android.location.Address;
import android.support.annotation.NonNull;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public abstract class BaseUtils {

    public static final String COMMA_SEPARATOR = ",";

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

        String[] resultLines = new String[]{addressLine, city, state, country, postalCode};
        StringBuilder resultBuilder = new StringBuilder();

        int length = resultLines.length;
        for (int i = 0; i < length; i++) {
            if (resultLines[i] == null) continue;
            if (i == length - 1) {
                resultBuilder.append(resultLines[i]);
            } else {
                resultBuilder.append(resultLines[i]).append(COMMA_SEPARATOR);
            }
        }
        return resultBuilder.toString();
    }
}
