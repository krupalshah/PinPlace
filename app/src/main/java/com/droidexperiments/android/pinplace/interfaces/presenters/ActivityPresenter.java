package com.droidexperiments.android.pinplace.interfaces.presenters;

import com.droidexperiments.android.pinplace.activities.BaseActivity;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface ActivityPresenter extends BaseAppPresenter {

    void attachActivity(BaseActivity activity);

    void detachActivity(BaseActivity activity);

    void checkActivityIsAvailable();

    BaseActivity getActivity();

}
