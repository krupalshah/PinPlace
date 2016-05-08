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

package com.droidexperiments.android.pinplace.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Author : Krupal Shah
 * Date : 08-May-16
 */
public abstract class BaseRecyclerViewAdapter<T, S extends BaseRecyclerViewAdapter.BaseViewHolder> extends RecyclerView.Adapter<S> {

    private Context context;
    private List<T> models;

    public BaseRecyclerViewAdapter(Context context, List<T> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    protected static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected Context getContext() {
        return context;
    }

    protected List<T> getModels() {
        return models;
    }

}
