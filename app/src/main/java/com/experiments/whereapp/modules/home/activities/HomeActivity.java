/*
 *   Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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

package com.experiments.whereapp.modules.home.activities;

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
import android.widget.ImageView;

import com.droidexperiments.android.where.R;
import com.experiments.common.android.activities.BaseActivity;
import com.experiments.common.android.adapters.CommonPagerAdapter;
import com.experiments.common.android.views.CustomTextView;
import com.experiments.common.utilities.PermissionsChecker;
import com.experiments.whereapp.modules.home.fragments.ExplorePlacesFragment;
import com.experiments.whereapp.modules.home.fragments.RecommendedPlacesFragment;
import com.experiments.whereapp.modules.home.fragments.SavedPlacesFragment;
import com.experiments.whereapp.modules.home.presenters.HomeScreenPresenter;
import com.experiments.whereapp.modules.home.views.HomeView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
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
public class HomeActivity extends BaseActivity implements HomeView, ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeActivity";

    //request code to ask for location settings if not on for the app
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 100;
    //request code for google place picker
    private static final int PLACE_PICKER_REQUEST_CODE = 101;

    //request code for asking location permissions
    private static final int LOCATION_UPDATES_PERMISSION_REQUEST_CODE = 200;

    //request code for asking location permissions
    private static final int PLACE_PICKER_PERMISSION_REQUEST_CODE = 201;


    //array of permissions to be asked for getting location access
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //tab positions in view pager
    private static final int TAB_POSITION_HOME = 0;
    private static final int TAB_POSITION_EXPLORE = 1;
    private static final int TAB_POSITION_BOOKMARKS = 2;

    @BindView(R.id.tv_title_toolbar)
    CustomTextView tvTitleToolbar;
    @BindView(R.id.iv_search_toolbar)
    ImageView ivSearchToolbar;
    @BindView(R.id.tabs_home)
    TabLayout tabsHome;
    @BindView(R.id.pager_home)
    ViewPager pagerHome;

    private HomeScreenPresenter homeScreenPresenter;
    private PermissionsChecker permissionsChecker;
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
        permissionsChecker = new PermissionsChecker();

        homeScreenPresenter = new HomeScreenPresenter();
        homeScreenPresenter.attachView(this);
        homeScreenPresenter.registerPlaceUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!permissionsChecker.askPermissionsIfNotGranted(this, LOCATION_UPDATES_PERMISSION_REQUEST_CODE, LOCATION_PERMISSIONS)) {
            return;
        }
        homeScreenPresenter.requestPlaceUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTINGS_REQUEST_CODE:
                //got result from resolution dialog
                LocationSettingsStates locationSettingsStates = LocationSettingsStates.fromIntent(data);
                homeScreenPresenter.checkTurnOnLocationResult(locationSettingsStates);
                break;

            case PLACE_PICKER_REQUEST_CODE:
                homeScreenPresenter.checkPlacePickerResult(resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_UPDATES_PERMISSION_REQUEST_CODE:
                //requesting location updates if permissions granted
                if (permissionsChecker.checkGrantResults(this, requestCode, grantResults, R.string.allow_location_updates, LOCATION_PERMISSIONS)) {
                    homeScreenPresenter.requestPlaceUpdates();
                }
                break;
            case PLACE_PICKER_PERMISSION_REQUEST_CODE:
                //opening place picker if permissions granted
                if (permissionsChecker.checkGrantResults(this, requestCode, grantResults, R.string.allow_place_search, LOCATION_PERMISSIONS)) {
                    openPlacePicker();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        homeScreenPresenter.stopPlaceUpdates();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        homeScreenPresenter.unregisterPlaceUpdates();
        homeScreenPresenter.detachView();
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
        fragments.add(RecommendedPlacesFragment.newInstance());
        fragments.add(ExplorePlacesFragment.newInstance());
        fragments.add(SavedPlacesFragment.newInstance());

        //setting pager adapter
        CommonPagerAdapter commonPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        pagerHome.setOffscreenPageLimit(fragments.size());
        pagerHome.setAdapter(commonPagerAdapter);
        pagerHome.addOnPageChangeListener(this);

        //setting up tabs having only icons
        tabsHome.setupWithViewPager(pagerHome);
        for (int tabPos = 0; tabPos < tabsHome.getTabCount(); tabPos++) {
            TabLayout.Tab tab = tabsHome.getTabAt(tabPos);
            if (tab == null) continue;
            switch (tabPos) {
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

        //creating toolbar titles for equivalent tabs
        toolbarTitles = new ArrayList<>(tabsHome.getTabCount());
        toolbarTitles.add(TAB_POSITION_HOME, R.string.home);
        toolbarTitles.add(TAB_POSITION_EXPLORE, R.string.explore);
        toolbarTitles.add(TAB_POSITION_EXPLORE, R.string.bookmarks);

        //setting current title to home
        setToolbarTitle(toolbarTitles.get(tabsHome.getSelectedTabPosition()));
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
        setToolbarTitle(toolbarTitles.get(position));
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
                openPlacePicker();
                break;
            case R.id.iv_settings_toolbar:
                navigateToSettings();
                break;
        }
    }

    @Override
    public void navigateToSettings() {

    }

    @Override
    public void openPlacePicker() {
        //place picker requires location permissions
        if (!permissionsChecker.askPermissionsIfNotGranted(this, PLACE_PICKER_REQUEST_CODE, LOCATION_PERMISSIONS)) {
            return;
        }

        //building place picker intent and starting activity for result
        try {
            PlacePicker.IntentBuilder placeIntentBuilder = new PlacePicker.IntentBuilder();
            startActivityForResult(placeIntentBuilder.build(this), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            showSnakeBar(R.string.err_play_services_disabled, android.R.string.ok, null);
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            showSnakeBar(R.string.err_play_services_not_available, android.R.string.ok, null);
            e.printStackTrace();
        }
    }
}
