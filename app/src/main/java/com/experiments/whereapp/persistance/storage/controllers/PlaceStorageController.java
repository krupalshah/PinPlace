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

package com.experiments.whereapp.persistance.storage.controllers;

import com.experiments.core.controllers.BaseStorageController;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * controller for all local db related jobs
 */
public class PlaceStorageController extends BaseStorageController {

    private static PlaceStorageController instance;

    //singleton
    public static synchronized PlaceStorageController getInstance() {
        if (instance == null) {
            instance = new PlaceStorageController();
        }
        return instance;
    }
}
