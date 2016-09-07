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

package com.experiments.whereapp.presentation.home.views;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.StringRes;

import com.experiments.core.presentation.views.BaseView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Created by Krupal Shah on 28-Aug-16.
 * <p>
 * view for home screen
 */
public interface HomeScreenView extends BaseView {

    /**
     * sets transparent status bar above lolipop and translucent above jelly bean
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    void makeStatusBarTransparent();

    /**
     * sets toolbar title
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

}
