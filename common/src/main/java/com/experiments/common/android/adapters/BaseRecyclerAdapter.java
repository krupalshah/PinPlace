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

package com.experiments.common.android.adapters;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.experiments.common.listeners.ItemClickListener;
import com.experiments.common.listeners.ItemLongClickListener;

import java.util.List;

/**
 * Author : Krupal Shah
 * Date : 08-May-16
 * <p>
 * base class for all recycler view adapters in project.<br/>
 * sets click and long click listeners if specified.
 *
 * @param <A> item model
 * @param <B> item view holder
 */
public abstract class BaseRecyclerAdapter<A, B extends BaseRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<B> {

    private final Context context;
    private final List<A> models;

    private ItemClickListener<A> itemClickListener;
    private ItemLongClickListener<A> itemLongClickListener;


    public BaseRecyclerAdapter(Context context, List<A> models) {
        this.context = context;
        this.models = models;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(B holder, final int position) {

        final A model = models.get(position);

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(view -> itemClickListener.onItemClicked(model));
        }

        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(view -> {
                itemLongClickListener.onItemLongClicked(model);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    protected Context getContext() {
        return context;
    }

    protected List<A> getModels() {
        return models;
    }

    public void setItemClickListener(ItemClickListener<A> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(ItemLongClickListener<A> itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    protected static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

}
