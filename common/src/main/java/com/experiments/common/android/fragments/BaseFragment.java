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

package com.experiments.common.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.experiments.common.android.activities.BaseActivity;
import com.experiments.common.mvp.views.BaseView;

/**
 * Author : Krupal Shah
 * Date : 09-Apr-16
 * <p>
 * base class for all fragments in app
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    //flag to manage if fragment's onDestroyView() has been called or not
    private boolean isDestroyed;


    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    /**
     * initialize required components here
     */
    protected abstract void initComponents();

    @Override
    public void showToast(@StringRes int msgResId) {
        ((BaseActivity) getActivity()).showToast(msgResId);
    }

    @Override
    public void showToast(@NonNull String msg) {
        ((BaseActivity) getActivity()).showToast(msg);
    }

    @Override
    public void showSnakeBar(@NonNull String msg, @StringRes int action, android.view.View.OnClickListener actionListener) {
        ((BaseActivity) getActivity()).showSnakeBar(msg, action, actionListener);
    }

    @Override
    public void showSnakeBar(@StringRes int msg, @StringRes int action, android.view.View.OnClickListener actionListener) {
        ((BaseActivity) getActivity()).showSnakeBar(msg, action, actionListener);
    }

    @Override
    public void hideKeyBoard() {
        ((BaseActivity) getActivity()).hideKeyBoard();
    }

    @Override
    public void hideSnakeBar() {
        ((BaseActivity) getActivity()).hideSnakeBar();
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
    public Context getContext() {
        return getActivity();
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
