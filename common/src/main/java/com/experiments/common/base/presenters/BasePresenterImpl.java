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

package com.experiments.common.base.presenters;

import android.support.annotation.CallSuper;

import com.experiments.common.base.contracts.BaseContract;
import com.experiments.common.base.contracts.BaseContract.BasePresenter;

import hugo.weaving.DebugLog;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * implementation of {@link BasePresenter}
 */
public abstract class BasePresenterImpl<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;

    @Override
    @CallSuper
    @DebugLog
    public void attachView(T view) {
        mView = view;
    }

    @Override
    @CallSuper
    @DebugLog
    public void detachView() {
        mView = null;
    }

    @Override
    public T getView() {
        return mView;
    }
}
