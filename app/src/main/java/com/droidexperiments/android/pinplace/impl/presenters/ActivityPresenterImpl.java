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

package com.droidexperiments.android.pinplace.impl.presenters;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.droidexperiments.android.pinplace.activities.base.BaseActivity;
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
