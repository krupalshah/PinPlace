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

package com.droidexperiments.android.pinplace.activities.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.droidexperiments.android.pinplace.interfaces.contracts.base.BaseContract;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    boolean isViewDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isViewDestroyed = false;
    }

    @Override
    protected void onDestroy() {
        isViewDestroyed = true;
        super.onDestroy();
    }

    protected abstract void initComponents();

    @Override
    public void showToast(@StringRes int msgResId) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialogs(Dialog... dialogs) {

    }

    @Override
    public boolean isViewDestroyed() {
        return isViewDestroyed;
    }
}
