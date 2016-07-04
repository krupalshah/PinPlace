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

package com.experiments.common.base.contracts;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * base class for all contracts defined for MVP structure
 * <p>
 * <b>side note:</b> contracts are easy way to avoid naming and packaging conflicts for MVP views and presenters<br/>
 * they just define views and presenters nad work as a packager between both.
 */
public interface BaseContract {

    /**
     * base class for all MVP views. <br/>
     * do not conflict it with android's view class. it is just view in Model-View-Presenter.
     */
    interface BaseView {

        /**
         * to get context specific to any framework component that implements specified view
         *
         * @return context
         */
        Context getComponentContext();

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
        void removeListeners();
    }

    /**
     * base class for all presenters
     *
     * @param <T> MVP view
     */
    interface BasePresenter<T extends BaseView> {

        /**
         * attaches view to a presenter
         *
         * @param view view
         */
        void attachView(T view);

        /**
         * detaches view from apresenter
         */
        void detachView();

        /**
         * to get view
         *
         * @return view
         */
        T getView();
    }
}
