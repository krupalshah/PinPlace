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

package com.experiments.core.config;

/**
 * Author : Krupal Shah
 * Date : 23-Jun-16
 * <p>
 * base class for all configuration classes
 */
public abstract class BaseConfig {

    public static final boolean DEBUG = true;

    /**
     * repository for defining location update intervals
     */
    public interface LocationUpdates {
        long NORMAL_LOCATION_UPDATE_INTERVAL = 20 * 1000;
        long FASTEST_LOCATION_UPDATE_INTERVAL = 10 * 1000;
    }
}
