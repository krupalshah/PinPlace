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

package com.experiments.whereapp.application;

import com.crashlytics.android.Crashlytics;
import com.experiments.core.application.Core;
import com.experiments.whereapp.GeneratedEventBusIndex;
import com.experiments.whereapp.config.AppConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * application class
 */
public class Where extends Core {

    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
        if (AppConfig.DEBUG) {
            LeakCanary.install(this);
        }
        Fabric.with(this, new Crashlytics());
        FlowManager.init(this);
        EventBus.builder().addIndex(new GeneratedEventBusIndex()).installDefaultEventBus();
    }

    @Override
    @DebugLog
    public void onLowMemory() {
        super.onLowMemory();
    }
}
