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

package com.experiments.core.mvp.views;

import android.content.Context;

import com.experiments.core.mvp.presenters.BasePresenter;

/**
 * base class for all MVP views. <br/>
 * do not confuse it with android's view class. it is just view in Model-View-Presenter.
 */
public interface BaseMvpView {

    /**
     * to get context specific to any framework component that implements specified view
     *
     * @return context
     */
    Context getContext();

    /**
     * returns checks if component has reached its destroy state in its life cycle<br/>
     * for example for activity; it will return true if activity's onDestory() has been called. similar applies to fragment for onDestroyView()
     *
     * @return true if component has been destroyed in its lifecycle; false otherwise
     */
    boolean isComponentDestroyed();

    /**
     * removes all callbacks and listeners on view. to be called from {@link BasePresenter#detachView()}
     */
    void removeCallbacks();
}
