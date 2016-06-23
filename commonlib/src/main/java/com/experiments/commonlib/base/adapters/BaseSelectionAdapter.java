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

package com.experiments.commonlib.base.adapters;

import android.content.Context;

import com.experiments.commonlib.interfaces.ItemSelectionListener;
import com.experiments.commonlib.interfaces.Selection;

import java.util.List;

/**
 * Author : Krupal Shah
 * Date : 08-May-16
 */
public abstract class BaseSelectionAdapter<S extends Selection, T extends BaseRecyclerAdapter.BaseViewHolder> extends BaseRecyclerAdapter<S, T> {

    private ItemSelectionListener<S> itemSelectionListener;

    public BaseSelectionAdapter(Context context, List<S> models) {
        super(context, models);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        super.onBindViewHolder(holder, position);
        final S model = getModels().get(position);
        if (model.isSelected()) {
            handleSelectedModelView(model, holder);
        } else {
            handleUnSelectedModelView(model, holder);
        }
        if (itemSelectionListener != null) {
            holder.itemView.setOnLongClickListener(view -> {
                model.setSelected(!model.isSelected());
                notifyItemChanged(position);
                itemSelectionListener.onItemSelectionChanged(model);
                return true;
            });
        }
    }

    protected abstract void handleSelectedModelView(S model, T holder);

    protected abstract void handleUnSelectedModelView(S model, T holder);

    public void setItemSelectionListener(ItemSelectionListener<S> itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
    }

}
