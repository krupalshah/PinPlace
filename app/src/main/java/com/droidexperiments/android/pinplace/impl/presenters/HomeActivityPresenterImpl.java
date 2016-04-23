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

package com.droidexperiments.android.pinplace.impl.presenters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.droidexperiments.android.pinplace.activities.base.BaseActivity;
import com.droidexperiments.android.pinplace.activities.home.HomeActivity;
import com.droidexperiments.android.pinplace.interfaces.presenters.home.HomeActivityPresenter;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public final class HomeActivityPresenterImpl extends ActivityPresenterImpl implements HomeActivityPresenter {

    private HomeActivity homeActivity;

    public HomeActivityPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void attachActivity(BaseActivity activity) {
        super.attachActivity(activity);
        homeActivity = (HomeActivity) activity;
    }

    @Override
    public void animateToolbarCollapsing() {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = getActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setupViewPager() {
    }

    @Override
    public void detachActivity(BaseActivity activity) {
        super.detachActivity(activity);
        homeActivity = null;
    }

    @Override
    public BaseActivity getActivity() {
        return homeActivity;
    }

}
