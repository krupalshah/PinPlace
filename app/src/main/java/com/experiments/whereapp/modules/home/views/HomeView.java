package com.experiments.whereapp.modules.home.views;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.StringRes;

import com.experiments.common.mvp.views.BaseView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Created by Krupal Shah on 28-Aug-16.
 * mvp view for home screen
 */
public interface HomeView extends BaseView {

    /**
     * sets transparent status bar for kitkat & above
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    void makeStatusBarTransparent();

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

    void onSettingsClick();

    void onSearchPlacesClick();
}
