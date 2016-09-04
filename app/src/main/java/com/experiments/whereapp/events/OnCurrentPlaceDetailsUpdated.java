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

package com.experiments.whereapp.events;

import com.experiments.core.helpers.location.PlaceDataWrapper;

/**
 * Created by Krupal Shah on 01-Sep-16.
 * <p>
 * this event will be posted in event bus whenever current place information gets updated
 */
public class OnCurrentPlaceDetailsUpdated {

    private final PlaceDataWrapper currentPlace;

    public OnCurrentPlaceDetailsUpdated(PlaceDataWrapper currentPlace) {
        this.currentPlace = currentPlace;
    }

    public PlaceDataWrapper getCurrentPlace() {
        return currentPlace;
    }

    @Override
    public String toString() {
        return "OnCurrentPlaceUpdated{" +
                "currentPlace=" + currentPlace +
                '}';
    }
}
