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

package com.droidexperiments.android.pinplace.base.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.droidexperiments.android.pinplace.base.activities.BaseActivity;
import com.droidexperiments.android.pinplace.base.contracts.BaseContract;

/**
 * Author : Krupal Shah
 * Date : 09-Apr-16
 */
public abstract class BaseFragment extends Fragment implements BaseContract.BaseView {

    boolean isViewDestroyed = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewDestroyed = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewDestroyed = true;
    }

    /**
     * initialize components here
     */
    protected abstract void initComponents();

    @Override
    public void showToast(@StringRes int msgResId) {
        ((BaseActivity) getActivity()).showToast(msgResId);
    }

    @Override
    public void showSnakeBar(@StringRes int msg, @StringRes int action, View.OnClickListener actionListener) {
        ((BaseActivity) getActivity()).showSnakeBar(msg, action, actionListener);
    }

    @Override
    public void showDialog(Dialog dialog) {
        ((BaseActivity) getActivity()).showDialog(dialog);
    }

    @Override
    public void dismissDialogs(Dialog... dialogs) {
        ((BaseActivity) getActivity()).dismissDialogs(dialogs);
    }

    @Override
    public boolean isViewDestroyed() {
        return false;
    }
}
