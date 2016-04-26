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

package com.droidexperiments.android.pinplace.home.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.base.activities.BaseActivity;
import com.droidexperiments.android.pinplace.common.adapters.CommonPagerAdapter;
import com.droidexperiments.android.pinplace.home.fragments.CurrentPlaceFragment;
import com.droidexperiments.android.pinplace.home.fragments.SavedPlacesFragment;
import com.droidexperiments.android.pinplace.home.fragments.TrendingPlacesFragment;
import com.droidexperiments.android.pinplace.home.presenters.HomeActivityPresenter;
import com.droidexperiments.android.pinplace.home.contracts.HomeActivityContract;
import com.droidexperiments.android.pinplace.utilities.PermissionsHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 */
public final class HomeActivity extends BaseActivity implements HomeActivityContract.View {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager_home)
    ViewPager pagerHome;

    private static final String TAG = "HomeActivity";

    private static final int REQUEST_LOCATION_SETTINGS = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private HomeActivityContract.Presenter mHomeActivityPresenter;
    private PermissionsHelper mPermissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPermissionsHelper.askPermissionsIfNotGranted(this, REQUEST_LOCATION_PERMISSION, LOCATION_PERMISSIONS)) {
            mHomeActivityPresenter.requestPlaceUpdates();
        }
    }

    @Override
    public void showTurnOnLocationDialog(Status locationSettingsStatus) {
        try {
            locationSettingsStatus.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION_SETTINGS:
                LocationSettingsStates locationSettingsStates = LocationSettingsStates.fromIntent(data);
                mHomeActivityPresenter.checkTurnOnLocationResult(locationSettingsStates);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (mPermissionsHelper.checkGrantResultsAndShowRationaleIfDenied(this, REQUEST_LOCATION_PERMISSION, grantResults, R.string.rationale_access_location, LOCATION_PERMISSIONS)) {
                    mHomeActivityPresenter.requestPlaceUpdates();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        mHomeActivityPresenter.stopPlaceUpdates();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mHomeActivityPresenter.unregisterPlaceUpdates();
        mHomeActivityPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initComponents() {
        mPermissionsHelper = new PermissionsHelper();
        mHomeActivityPresenter = new HomeActivityPresenter();

        mHomeActivityPresenter.attachView(this);
        mHomeActivityPresenter.registerPlaceUpdates();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void animateToolbarCollapsing() {

    }

    @Override
    public void setToolbarTitle(@StringRes int titleRes) {
        toolbar.setTitle(titleRes);
    }

    @Override
    public void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CurrentPlaceFragment.newInstance());
        fragments.add(SavedPlacesFragment.newInstance());
        fragments.add(TrendingPlacesFragment.newInstance());

        CommonPagerAdapter commonPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        pagerHome.setOffscreenPageLimit(fragments.size());
        pagerHome.setAdapter(commonPagerAdapter);
    }

    @Override
    public void updateAddressText(String address) {

    }
}
