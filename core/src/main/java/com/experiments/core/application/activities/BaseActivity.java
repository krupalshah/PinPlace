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

package com.experiments.core.application.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.experiments.core.R;
import com.experiments.core.presentation.views.BaseView;

/**
 * Author : Krupal Shah
 * Date : 02-Apr-16
 * <p>
 * base class for all activities in app
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, android.view.View.OnClickListener {

    //flag to manage if activity's onDestroy() has been called or not (since not available for minSDK < 17)
    private boolean isDestroyed;
    private Snackbar snackbar;


    @CallSuper
    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }

    /**
     * initialize required components here
     */
    protected abstract void initComponents();

    public void showToast(@NonNull String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int msgResId) {
        showToast(getString(msgResId));
    }

    public void showSnakeBar(@NonNull String msg, @StringRes int action, android.view.View.OnClickListener actionListener) {
        hideSnakeBar();
        snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.md_light_blue_A400));
        snackbar.setAction(action, view -> {
            snackbar.dismiss();
            if (actionListener != null) {
                actionListener.onClick(view);
            }
        });
        snackbar.show();
    }

    @CallSuper
    @Override
    public void onClick(android.view.View view) {
        hideKeyBoard();
        hideSnakeBar();
        // TODO: 06-Sep-16 hideLoader();
    }

    public void hideSnakeBar() {
        if (snackbar != null && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
    }

    public void hideKeyBoard() {
        android.view.View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnakeBar(@StringRes int msg, @StringRes int action, @Nullable android.view.View.OnClickListener actionListener) {
        showSnakeBar(getString(msg), action, actionListener);
    }

    public void showDialog(Dialog dialog) {
        if (dialog == null || dialog.isShowing()) return;
        dialog.show();
    }

    public void dismissDialogs(Dialog... dialogs) {
        for (Dialog dialog : dialogs) {
            if (dialog == null || !dialog.isShowing()) continue;
            dialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean isComponentDestroyed() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return super.isDestroyed();
        } else {
            return isDestroyed;
        }
    }

    @Override
    public void showMessage(@StringRes int msgResId) {
        String message = getString(msgResId);
        showSnakeBar(message, R.string.dismiss, null);
    }

    @Override
    public void showError(@StringRes int msgResId) {
        String message = getString(msgResId);
        showSnakeBar(message, R.string.dismiss, null);
    }
}
