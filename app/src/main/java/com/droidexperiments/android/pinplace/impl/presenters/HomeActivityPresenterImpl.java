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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.activities.base.BaseActivity;
import com.droidexperiments.android.pinplace.activities.home.HomeActivity;
import com.droidexperiments.android.pinplace.adapters.CommonPagerAdapter;
import com.droidexperiments.android.pinplace.fragments.home.CurrentPlaceFragment;
import com.droidexperiments.android.pinplace.fragments.home.SavedPlacesFragment;
import com.droidexperiments.android.pinplace.fragments.home.TrendingPlacesFragment;
import com.droidexperiments.android.pinplace.interfaces.presenters.home.HomeActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public final class HomeActivityPresenterImpl extends ActivityPresenterImpl implements HomeActivityPresenter {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager_home)
    ViewPager pagerHome;

    private HomeActivity homeActivity;
    private CommonPagerAdapter mCommonPagerAdapter;

    public HomeActivityPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void attachActivity(BaseActivity activity) {
        super.attachActivity(activity);
        homeActivity = (HomeActivity) activity;
        ButterKnife.bind(homeActivity);
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
        List<Fragment> mListFragments = new ArrayList<>();
        mListFragments.add(CurrentPlaceFragment.newInstance());
        mListFragments.add(SavedPlacesFragment.newInstance());
        mListFragments.add(TrendingPlacesFragment.newInstance());
        mCommonPagerAdapter = new CommonPagerAdapter(getActivity().getSupportFragmentManager(), mListFragments);

        pagerHome.setOffscreenPageLimit(3);
        pagerHome.setAdapter(mCommonPagerAdapter);

        pagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int title;
                switch (position) {
                    case 0:
                        title = R.string.unknown;
                        break;

                    case 1:
                        title = R.string.err_unable_to_fetch_address;
                        break;

                    case 2:
                        title = R.string.err_no_internet;
                        break;

                    default:
                        title = R.string.app_name;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void updateAddressText(String address) {

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
