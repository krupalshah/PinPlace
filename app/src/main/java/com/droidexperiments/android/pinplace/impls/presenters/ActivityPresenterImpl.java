package com.droidexperiments.android.pinplace.impls.presenters;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.droidexperiments.android.pinplace.activities.BaseActivity;
import com.droidexperiments.android.pinplace.interfaces.presenters.ActivityPresenter;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public abstract class ActivityPresenterImpl extends BaseAppPresenterImpl implements ActivityPresenter {

    private boolean isActivityDestroyed;

    public ActivityPresenterImpl(Context context) {
        super(context);
    }

    @Override
    @CallSuper
    public void attachActivity(BaseActivity activity) {
        isActivityDestroyed = false;
    }

    @Override
    @CallSuper
    public void detachActivity(BaseActivity activity) {
        isActivityDestroyed = true;
    }

    @Override
    public final void checkActivityIsAvailable() {
        if (getActivity() == null) {
            throw new IllegalStateException("unable to get activity. Did you attached Activity instance by calling attachActivity() ?");
        }
    }

    @Override
    public final boolean isComponentDestroyed() {
        return isActivityDestroyed;
    }
}
