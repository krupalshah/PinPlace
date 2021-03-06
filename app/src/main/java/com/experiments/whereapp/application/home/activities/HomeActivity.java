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

package com.experiments.whereapp.application.home.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.droidexperiments.android.where.R;
import com.experiments.core.application.activities.BaseActivity;
import com.experiments.core.application.adapters.CommonPagerAdapter;
import com.experiments.core.application.views.CustomTextView;
import com.experiments.core.domain.exceptions.PermissionDeniedException;
import com.experiments.core.util.PermissionDispatcher;
import com.experiments.whereapp.application.home.fragments.ExplorePlacesFragment;
import com.experiments.whereapp.application.home.fragments.RecommendedPlacesFragment;
import com.experiments.whereapp.application.home.fragments.SavedPlacesFragment;
import com.experiments.whereapp.presentation.home.presenters.HomePresenter;
import com.experiments.whereapp.presentation.home.routers.HomeScreenRouter;
import com.experiments.whereapp.presentation.home.views.HomeScreenView;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * home screen activity after launch screen ends
 */
public class HomeActivity extends BaseActivity implements HomeScreenView, HomeScreenRouter, ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeActivity";

    //request code to ask for location settings if not on for the app
    private static final int REQUEST_LOCATION_SETTINGS = 100;
    //request code for google place picker
    private static final int REQUEST_PLACE_PICKER = 101;

    //request code for asking location permissions for updating location
    private static final int PERMISSION_LOCATION_UPDATES = 200;

    //request code for asking location permissions for place picker
    private static final int PERMISSION_PLACE_PICKER = 201;


    //array of permissions to be asked for getting location access
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //tab positions in view pager
    private static final int TAB_POSITION_HOME = 0;
    private static final int TAB_POSITION_EXPLORE = 1;
    private static final int TAB_POSITION_BOOKMARKS = 2;

    @BindView(R.id.txt_title_toolbar)
    CustomTextView txtTitleToolbar;
    @BindView(R.id.tabs_home)
    TabLayout tabsHome;
    @BindView(R.id.pager_home)
    ViewPager pagerHome;


    private HomePresenter homePresenter;
    private PermissionDispatcher permissionDispatcher;
    private List<Integer> toolbarTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initComponents();
    }

    @Override
    protected void initComponents() {
        permissionDispatcher = PermissionDispatcher.create();
        homePresenter = HomePresenter.create();
        homePresenter.attachView(this);
        homePresenter.registerPlaceUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!permissionDispatcher.askPermissionsIfNotGranted(this, PERMISSION_LOCATION_UPDATES, LOCATION_PERMISSIONS)) {
            return;
        }
        homePresenter.requestPlaceUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION_SETTINGS:
                //got result from resolution dialog
                LocationSettingsStates locationSettingsStates = LocationSettingsStates.fromIntent(data);
                homePresenter.checkTurnOnLocationResult(locationSettingsStates);
                break;

            case REQUEST_PLACE_PICKER:
                //got result from place picker
                homePresenter.checkPlacePickerResult(resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION_UPDATES:
                //requesting location updates if permissions granted
                if (permissionDispatcher.checkGrantResults(this, requestCode, grantResults, R.string.allow_location_updates, LOCATION_PERMISSIONS)) {
                    homePresenter.requestPlaceUpdates();
                } else {
                    homePresenter.postErrorGettingPlaceEvent(new PermissionDeniedException(LOCATION_PERMISSIONS));
                }
                break;
            case PERMISSION_PLACE_PICKER:
                //opening place picker if permissions granted
                if (permissionDispatcher.checkGrantResults(this, requestCode, grantResults, R.string.allow_place_search, LOCATION_PERMISSIONS)) {
                    navigateToPlacePicker();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        homePresenter.stopPlaceUpdates();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        homePresenter.unregisterPlaceUpdates();
        homePresenter.detachView();
        super.onDestroy();
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
        txtTitleToolbar.setText(titleRes);
    }

    @Override
    public void setupViewPager() {
        //preparing fragments
        List<Fragment> fragments = new ArrayList<>();
        prepareTabFragments(fragments);

        //setting pager adapter
        setPagerAdapter(fragments);

        //setting up tabs with view pager
        tabsHome.setupWithViewPager(pagerHome);

        //setting tab icons according to view pager
        setTabIcons();

        //creating toolbar titles for equivalent tabs & setting current title to home
        toolbarTitles = new ArrayList<>(tabsHome.getTabCount());
        prepareToolbarTitles(toolbarTitles);
        setToolbarTitle(toolbarTitles.get(tabsHome.getSelectedTabPosition()));
    }

    @Override
    public void showTurnOnLocationDialog(Status locationSettingsStatus) {
        try {
            //this will show location dialog to user
            locationSettingsStatus.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            homePresenter.postErrorGettingPlaceEvent(e);
        }
    }

    @DebugLog
    @Override
    public void onPageSelected(int position) {
        setToolbarTitle(toolbarTitles.get(position));
    }

    @DebugLog
    @Override
    public void removeCallbacks() {
        pagerHome.clearOnPageChangeListeners();
    }

    @OnClick({R.id.img_search_toolbar, R.id.img_settings_toolbar})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.img_search_toolbar:
                navigateToPlacePicker();
                break;
            case R.id.img_settings_toolbar:
                navigateToSettings();
                break;
        }
    }

    @Override
    public void navigateToSettings() {

    }

    @Override
    public void navigateToPlacePicker() {
        //place picker requires location permissions
        if (!permissionDispatcher.askPermissionsIfNotGranted(this, PERMISSION_PLACE_PICKER, LOCATION_PERMISSIONS)) {
            return;
        }
        //building place picker intent and starting activity for result
        try {
            PlacePicker.IntentBuilder placeIntentBuilder = new PlacePicker.IntentBuilder();
            startActivityForResult(placeIntentBuilder.build(this), REQUEST_PLACE_PICKER);
        } catch (Exception e) {
            handleErrorOpeningPlacePicker(e);
        }
    }


    private void prepareTabFragments(List<Fragment> fragments) {
        fragments.add(RecommendedPlacesFragment.newInstance());
        fragments.add(ExplorePlacesFragment.newInstance());
        fragments.add(SavedPlacesFragment.newInstance());
    }

    private void prepareToolbarTitles(List<Integer> toolbarTitles) {
        toolbarTitles.add(TAB_POSITION_HOME, R.string.home);
        toolbarTitles.add(TAB_POSITION_EXPLORE, R.string.explore);
        toolbarTitles.add(TAB_POSITION_BOOKMARKS, R.string.bookmarks);
    }

    private void setTabIcons() {
        for (int tabPosition = 0; tabPosition < tabsHome.getTabCount(); tabPosition++) {
            TabLayout.Tab tab = tabsHome.getTabAt(tabPosition);
            if (tab == null) continue;

            switch (tabPosition) {
                case TAB_POSITION_HOME:
                    tab.setIcon(R.drawable.ic_home_white_24dp);
                    break;
                case TAB_POSITION_EXPLORE:
                    tab.setIcon(R.drawable.ic_explore_white_24dp);
                    break;
                case TAB_POSITION_BOOKMARKS:
                    tab.setIcon(R.drawable.ic_bookmark_white_24dp);
                    break;
            }
        }
    }

    private void setPagerAdapter(List<Fragment> fragments) {
        CommonPagerAdapter commonPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        pagerHome.setOffscreenPageLimit(fragments.size());
        pagerHome.setAdapter(commonPagerAdapter);
        pagerHome.addOnPageChangeListener(this);
    }

    @DebugLog
    private void handleErrorOpeningPlacePicker(Exception e) {
        e.printStackTrace();
        if (e instanceof GooglePlayServicesRepairableException) {
            showSnakeBar(R.string.err_play_services_disabled, android.R.string.ok, null);
        } else {
            showSnakeBar(R.string.err_play_services_not_available, android.R.string.ok, null);
        }
    }


    @DebugLog
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @DebugLog
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
