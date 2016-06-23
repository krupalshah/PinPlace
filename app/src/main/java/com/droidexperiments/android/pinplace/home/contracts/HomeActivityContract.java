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

package com.droidexperiments.android.pinplace.home.contracts;

import android.support.annotation.StringRes;

import com.experiments.commonlib.base.contracts.BaseContract;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public interface HomeActivityContract extends BaseContract {

    interface View extends BaseView {

        void setTransparentStatusBar();

        void animateToolbarCollapsing();

        void setToolbarTitle(@StringRes int titleRes);

        void setupViewPager();

        void showTurnOnLocationDialog(Status locationStatus);

        void updateAddressText(String address);
    }

    interface Presenter extends BasePresenter<View> {

        void registerPlaceUpdates();

        void requestPlaceUpdates();

        void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates);

        void stopPlaceUpdates();

        void unregisterPlaceUpdates();
    }
}
