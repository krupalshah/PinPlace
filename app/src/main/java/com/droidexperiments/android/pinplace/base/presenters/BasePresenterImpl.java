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

package com.droidexperiments.android.pinplace.base.presenters;

import android.support.annotation.CallSuper;
import android.util.Log;

import com.droidexperiments.android.pinplace.base.contracts.BaseContract;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public abstract class BasePresenterImpl<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {
    private static final String TAG = "BasePresenterImpl";
    
    protected T mView;

    @Override
    @CallSuper
    public void attachView(T view) {
        Log.d(TAG, "attachView ");
        mView = view;
    }

    @Override
    @CallSuper
    public void detachView() {
        Log.d(TAG, "detachView ");
        mView = null;
    }

    @Override
    public T getView() {
        return mView;
    }
}
