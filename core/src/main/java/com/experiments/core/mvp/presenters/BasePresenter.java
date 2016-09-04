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

package com.experiments.core.mvp.presenters;

import android.support.annotation.CallSuper;

import com.experiments.core.mvp.views.BaseMvpView;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * base class for all presenters
 *
 * @param <T> MVP view
 */
public abstract class BasePresenter<T extends BaseMvpView> {

    protected T view;

    @CallSuper
    @DebugLog
    public void attachView(T view) {
        this.view = view;
    }

    @CallSuper
    @DebugLog
    public void detachView() {
        view = null;
    }

}
