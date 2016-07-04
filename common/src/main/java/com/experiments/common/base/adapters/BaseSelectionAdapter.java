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

package com.experiments.common.base.adapters;

import android.content.Context;

import com.experiments.common.listeners.ItemSelectionListener;
import com.experiments.common.listeners.Selection;

import java.util.List;

/**
 * Author : Krupal Shah
 * Date : 08-May-16
 * <p>
 * base class for selection related recyclerivew adapter<br/>
 * given dataset model must have to implement {@link Selection} interface to retain state information with them
 */
public abstract class BaseSelectionAdapter<A extends Selection, B extends BaseRecyclerAdapter.BaseViewHolder> extends BaseRecyclerAdapter<A, B> {

    private ItemSelectionListener<A> itemSelectionListener;

    public BaseSelectionAdapter(Context context, List<A> models) {
        super(context, models);
    }

    @Override
    public void onBindViewHolder(B holder, int position) {
        super.onBindViewHolder(holder, position);
        final A model = getModels().get(position);
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

    /**
     * to handle view when item item selected
     *
     * @param model  dataset model
     * @param holder view holder
     */
    protected abstract void handleSelectedModelView(A model, B holder);


    /**
     * to handle view when item item unselected
     *
     * @param model  dataset model
     * @param holder view holder
     */
    protected abstract void handleUnSelectedModelView(A model, B holder);

    public void setItemSelectionListener(ItemSelectionListener<A> itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
    }

}
