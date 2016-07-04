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

package com.experiments.common.base.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.experiments.common.base.contracts.BaseContract;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * base class for all activities in project
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    private boolean isViewDestroyed = false;

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

    /**
     * initializes requires components
     */
    protected abstract void initComponents();

    @Override
    public void showToast(@NonNull String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int msgResId) {
        showToast(getString(msgResId));
    }

    @Override
    public void showSnakeBar(@NonNull String msg, @StringRes int action, View.OnClickListener actionListener) {
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_INDEFINITE);
        if (actionListener != null) {
            snackbar.setAction(action, actionListener);
        } else {
            snackbar.setAction(action, view -> snackbar.dismiss());
        }
        snackbar.show();
    }

    @Override
    public void showSnakeBar(@StringRes int msg, @StringRes int action, @Nullable View.OnClickListener actionListener) {
        showSnakeBar(getString(msg), action, actionListener);
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (dialog == null || dialog.isShowing()) return;
        dialog.show();
    }

    @Override
    public void dismissDialogs(Dialog... dialogs) {
        for (Dialog dialog : dialogs) {
            if (dialog == null || !dialog.isShowing()) continue;
            dialog.dismiss();
        }
    }

    @Override
    public Context getComponentContext() {
        return this;
    }

    @Override
    public boolean isComponentDestroyed() {
        return isViewDestroyed;
    }

}
