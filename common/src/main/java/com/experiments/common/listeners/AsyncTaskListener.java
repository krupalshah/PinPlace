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

package com.experiments.common.listeners;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

/**
 * Author : Krupal Shah
 * Date : 09-Apr-16
 * <p>
 * callback for {@link AsyncTaskListener}.<br/>
 * to avoid nested inner classes for async tasks.
 *
 * @param <T> type of result wanted in {@link AsyncTask#onPostExecute}
 */
public interface AsyncTaskListener<T> {
    void onAsyncOperationCompleted(@Nullable T result);
}
