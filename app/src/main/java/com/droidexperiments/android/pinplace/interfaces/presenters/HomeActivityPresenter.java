package com.droidexperiments.android.pinplace.interfaces.presenters;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface HomeActivityPresenter extends ActivityPresenter {

    void setTransparentStatusBar();

    void animateToolbarCollapsing();

    void setupViewPager();

    void onPageChanged(int position);

}
