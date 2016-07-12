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

package com.experiments.whereapp.modules.home.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.droidexperiments.android.where.R;
import com.experiments.common.base.activities.BaseActivity;
import com.experiments.common.base.adapters.CommonPagerAdapter;
import com.experiments.common.customviews.CustomTextView;
import com.experiments.common.utilities.PermissionsChecker;
import com.experiments.whereapp.modules.home.contracts.HomeScreenContract;
import com.experiments.whereapp.modules.home.fragments.CurrentPlaceFragment;
import com.experiments.whereapp.modules.home.fragments.SavedPlacesFragment;
import com.experiments.whereapp.modules.home.fragments.TrendingPlacesFragment;
import com.experiments.whereapp.modules.home.presenters.HomeScreenPresenter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * home screen activity after launch screen ends
 */
public class HomeActivity extends BaseActivity implements HomeScreenContract.View, ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeActivity";

    /**
     * request code to ask for location settings if not on for the app
     */
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 1;

    /**
     * request code for asking location permissions
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    /**
     * array of permissions to be asked for getting location access
     */
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;


    @Bind(R.id.pager_home)
    ViewPager pagerHome;
    @Bind(R.id.tabs_home)
    TabLayout tabsHome;
    @Bind(R.id.tv_title_toolbar)
    CustomTextView tvTitleToolbar;
    @Bind(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;

    /**
     * presenter for home activity
     */
    private HomeScreenContract.Presenter mHomeScreenPresenter;

    /**
     * permission checker
     */
    private PermissionsChecker mPermissionsChecker;
    private List<Integer> mToolbarTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initComponents();
    }

    @Override
    protected void initComponents() {
        mPermissionsChecker = new PermissionsChecker();

        mHomeScreenPresenter = new HomeScreenPresenter();
        mHomeScreenPresenter.attachView(this);
        mHomeScreenPresenter.registerPlaceUpdates();
    }

    @Override
    protected void onStart() {
        Debug.waitForDebugger();
        super.onStart();
        if (mPermissionsChecker.askPermissionsIfNotGranted(this, LOCATION_PERMISSION_REQUEST_CODE, LOCATION_PERMISSIONS)) {
            mHomeScreenPresenter.requestPlaceUpdates();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTINGS_REQUEST_CODE:
                //got result from resolution dialog
                LocationSettingsStates locationSettingsStates = LocationSettingsStates.fromIntent(data);
                mHomeScreenPresenter.checkTurnOnLocationResult(locationSettingsStates);
                break;

            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                mHomeScreenPresenter.checkPlaceAutoCompleteResult(resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                //requesting location updates if permissions granted
                if (mPermissionsChecker.checkGrantResults(this, LOCATION_PERMISSION_REQUEST_CODE, grantResults, R.string.rationale_access_location, LOCATION_PERMISSIONS)) {
                    mHomeScreenPresenter.requestPlaceUpdates();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        mHomeScreenPresenter.stopPlaceUpdates();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mHomeScreenPresenter.unregisterPlaceUpdates();
        mHomeScreenPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showTurnOnLocationDialog(Status locationSettingsStatus) {
        try {
            //this will show location dialog to user
            locationSettingsStatus.startResolutionForResult(HomeActivity.this, LOCATION_SETTINGS_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setToolbarTitle(@StringRes int titleRes) {
        tvTitleToolbar.setText(titleRes);
    }

    @Override
    public void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CurrentPlaceFragment.newInstance());
        fragments.add(SavedPlacesFragment.newInstance());
        fragments.add(TrendingPlacesFragment.newInstance());

        //setting pager adapter
        CommonPagerAdapter commonPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        pagerHome.setOffscreenPageLimit(fragments.size());
        pagerHome.setAdapter(commonPagerAdapter);
        pagerHome.addOnPageChangeListener(this);

        //setting up tabs having only icons
        tabsHome.setupWithViewPager(pagerHome);
        for (int i = 0; i < tabsHome.getTabCount(); i++) {
            TabLayout.Tab tab = tabsHome.getTabAt(i);
            if (tab == null) continue;
            switch (i) {
                case 0:
                    tab.setIcon(R.drawable.ic_home_white_24dp);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_explore_white_24dp);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_bookmark_white_24dp);
                    break;
            }
        }

        //creating toolbar titles for equivalent tabs
        mToolbarTitles = new ArrayList<>(tabsHome.getTabCount());
        mToolbarTitles.add(R.string.home);
        mToolbarTitles.add(R.string.explore);
        mToolbarTitles.add(R.string.bookmarks);
        setToolbarTitle(mToolbarTitles.get(0));
    }

    @Override
    public void updateAddressText(String address) {

    }

    @Override
    public void removeListeners() {
        pagerHome.clearOnPageChangeListeners();
    }

    @DebugLog
    @Override
    public void onPageSelected(int position) {
        setToolbarTitle(mToolbarTitles.get(position));
    }

    @DebugLog
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @DebugLog
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.iv_search_toolbar, R.id.iv_settings_toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_toolbar:
                onSearchPlacesClick();
                break;
            case R.id.iv_settings_toolbar:
                onSettingsClick();
                break;
        }
    }

    @Override
    public void onSettingsClick() {

    }

    @Override
    public void onSearchPlacesClick() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            e.printStackTrace();
        }
    }
}
