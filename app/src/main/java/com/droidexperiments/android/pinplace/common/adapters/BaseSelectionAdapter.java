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
import android.view.View;

import com.droidexperiments.android.pinplace.common.interfaces.ISelection;
import com.droidexperiments.android.pinplace.common.interfaces.ItemSelectionListener;

import java.util.List;

/**
 * Author : Krupal Shah
 * Date : 08-May-16
 */
public abstract class BaseSelectionAdapter<S extends ISelection, T extends BaseRecyclerAdapter.BaseViewHolder> extends BaseRecyclerAdapter<S, T> implements ItemSelectionListener<S> {

    private ItemSelectionListener<S> itemSelectionListener;

    public BaseSelectionAdapter(Context context, List<S> models) {
        super(context, models);
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        super.onBindViewHolder(holder, position);
        final S model = getModels().get(position);
        if (model.isSelected()) {
            handleSelectedModelView(model, holder.itemView);
        } else {
            handleUnSelectedModelView(model, holder.itemView);
        }
        if (itemSelectionListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    model.setSelected(!model.isSelected());
                    itemSelectionListener.onItemSelectionChanged(model);
                    return true;
                }
            });
        }
    }

    protected abstract void handleSelectedModelView(S model, View itemView);

    protected abstract void handleUnSelectedModelView(S model, View itemView);

    public void setItemSelectionListener(ItemSelectionListener<S> itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
    }

}
