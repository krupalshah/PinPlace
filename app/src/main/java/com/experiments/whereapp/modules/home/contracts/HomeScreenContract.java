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

package com.experiments.whereapp.modules.home.contracts;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.StringRes;

import com.experiments.common.base.contracts.BaseContract;
import com.experiments.common.location.LocationOperations;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * contract that defines home screen view and its presenter
 */
public interface HomeScreenContract extends BaseContract {

    interface View extends BaseView {

        /**
         * sets transparent status bar for kitkat & above
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        void makeStatusBarTransparent();

        /**
         * for collapsing animation on toolbar
         */
        void animateToolbarCollapsing();

        /**
         * sets title on toolbar
         *
         * @param titleRes resource id of title
         */
        void setToolbarTitle(@StringRes int titleRes);

        /**
         * setup viewpager on home screen
         */
        void setupViewPager();


        /**
         * shows default location turn on dialog if got resolution required in {@link LocationSettingsResult}
         *
         * @param locationStatus status got in settings result
         */
        void showTurnOnLocationDialog(Status locationStatus);

        /**
         * refreshes address text on home screen whenever location is updated
         *
         * @param address address
         */
        void updateAddressText(String address);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * registers place update callbacks via {@link LocationOperations}
         */
        void registerPlaceUpdates();

        /**
         * requests connecting api client and requests place updates
         */
        void requestPlaceUpdates();

        /**
         * checks location turn on dialog result if invoked from {@link View#showTurnOnLocationDialog(Status)}
         *
         * @param locationSettingsStates got result state {@link LocationSettingsStates}
         */
        void checkTurnOnLocationResult(LocationSettingsStates locationSettingsStates);

        /**
         * removes location updates and disconnects api client
         */
        void stopPlaceUpdates();

        /**
         * unregisters all place update callbacks
         */
        void unregisterPlaceUpdates();
    }
}
