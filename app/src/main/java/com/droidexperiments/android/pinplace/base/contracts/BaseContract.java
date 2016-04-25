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

package com.droidexperiments.android.pinplace.base.contracts;

import android.app.Dialog;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public interface BaseContract {

    interface BaseView {

        void showToast(@StringRes int msgResId);

        void showSnakeBar(@StringRes int msg, @StringRes int action, View.OnClickListener actionListener);

        void showDialog(Dialog dialog);

        void dismissDialogs(Dialog... dialogs);

        boolean isViewDestroyed();
    }

    interface BasePresenter<T extends BaseView> {

        void attachView(T view);

        void detachView();

        T getView();
    }
}
