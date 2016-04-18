package com.droidexperiments.android.pinplace.impls.presenters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.droidexperiments.android.pinplace.activities.BaseActivity;
import com.droidexperiments.android.pinplace.activities.HomeActivity;
import com.droidexperiments.android.pinplace.interfaces.presenters.HomeActivityPresenter;

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
        checkActivityIsAvailable();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void setTransparentStatusBar() {
        checkActivityIsAvailable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            homeActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                homeActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    @Override
    public void setupViewPager() {
        checkActivityIsAvailable();
    }

    @Override
    public void onPageChanged(int position) {
        checkActivityIsAvailable();
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
