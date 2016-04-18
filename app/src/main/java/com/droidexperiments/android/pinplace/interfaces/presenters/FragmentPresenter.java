package com.droidexperiments.android.pinplace.interfaces.presenters;

import com.droidexperiments.android.pinplace.activities.BaseActivity;
import com.droidexperiments.android.pinplace.fragments.base.BaseFragment;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface FragmentPresenter extends BaseAppPresenter {

    void attachFragment(BaseFragment baseFragment);

    void detachFragment(BaseFragment baseFragment);

    void checkFragmentIsAvailable();

    BaseFragment getFragment();

    BaseActivity getActivity();
}
