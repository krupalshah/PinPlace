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

package com.experiments.whereapp;

import com.crashlytics.android.Crashlytics;
import com.droidexperiments.android.where.BuildConfig;
import com.experiments.common.Common;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * application class
 */
public class WhereApp extends Common {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) LeakCanary.install(this);
        Fabric.with(this, new Crashlytics());
        FlowManager.init(this);
    }

    @Override
    @DebugLog
    public void onLowMemory() {
        super.onLowMemory();
    }
}
