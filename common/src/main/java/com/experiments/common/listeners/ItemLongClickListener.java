package com.experiments.common.listeners;

/**
 * Author : Krupal Shah
 * Date : 23-Jun-16
 * <p>
 * long click listener for item in list
 *
 * @param <T> item model
 */
public interface ItemLongClickListener<T> {
    void onItemLongClicked(T model);
}
