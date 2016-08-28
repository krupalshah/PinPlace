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

package com.experiments.common.mvp.views;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by Krupal Shah on 28-Aug-16.
 */
public interface BaseView extends BaseMvpView {
    /**
     * shows short-length toast
     *
     * @param msgResId message string resource id
     */
    void showToast(@StringRes int msgResId);

    /**
     * shows short-length toast
     *
     * @param msg message string
     */
    void showToast(@NonNull String msg);

    /**
     * hides keyboard if showing
     */
    void hideKeyBoard();


    /**
     * shows snake bar for infinite time
     *
     * @param msg            message string resource id
     * @param action         action string resource id
     * @param actionListener click listener for action
     */
    void showSnakeBar(@StringRes int msg, @StringRes int action, View.OnClickListener actionListener);

    /**
     * shows snake bar for infinite time
     *
     * @param msg            message string
     * @param action         action string resource id
     * @param actionListener click listener for action
     */
    void showSnakeBar(@NonNull String msg, @StringRes int action, View.OnClickListener actionListener);

    /**
     * hides snake bar if showing
     */
    void hideSnakeBar();

    /**
     * shows dialog if not showing already
     *
     * @param dialog dialog
     */
    void showDialog(Dialog dialog);

    /**
     * dismisses all specified dialog if they are showing
     *
     * @param dialogs dialogs
     */
    void dismissDialogs(Dialog... dialogs);
}
