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

package com.experiments.core.exceptions;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created by Krupal Shah on 05-Sep-16.
 */
public class NoInternetException extends Exception {

    private final String screenName;

    public NoInternetException() {
        super();
        screenName = null;
    }

    public NoInternetException(String screenName) {
        super();
        this.screenName = screenName;
    }

    @Nullable
    public String getScreenName() {
        return screenName;
    }

    @Override
    public String toString() {
        return "NoInternetException{" +
                "screenName='" + screenName + '\'' +
                '}' + "\n Stack Trace : \n" + Arrays.toString(getStackTrace());
    }
}
