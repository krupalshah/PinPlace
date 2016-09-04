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

package com.experiments.whereapp.config;

import com.experiments.core.config.BaseConfig;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * configuration class specific to server and apis
 */
public class ServerConfig extends BaseConfig {

    private static final String BASE_URL = "https://api.foursquare.com/v2/";

    /**
     * to get api endpoint
     *
     * @return api base url inclusive to trailing slash ('/')
     */
    public static String baseUrl() {
        return BASE_URL;
    }

    /**
     * defines connection, read and write timeout for apis in seconds
     */
    public interface TimeOuts {
        long CONNECT = 10;
        long READ = 15;
        long WRITE = 15;
    }
}
