package com.experiments.common.mvp.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.experiments.common.mvp.presenters.BasePresenter;

/**
 * base class for all MVP views. <br/>
 * do not conflict it with android's view class. it is just view in Model-View-Presenter.
 */
public interface BaseView {
    /**
     * shows short-length toast
     *
     * @param msgResId message string resource id
     */
    void showToast(@StringRes int msgResId);

    /**
     * shows short-length toast
     *
     * @param msg message string
     */
    void showToast(@NonNull String msg);

    /**
     * shows snake bar for infinite time
     *
     * @param msg            message string resource id
     * @param action         action string resource id
     * @param actionListener click listener for action
     */
    void showSnakeBar(@StringRes int msg, @StringRes int action, View.OnClickListener actionListener);

    /**
     * shows snake bar for infinite time
     *
     * @param msg            message string
     * @param action         action string resource id
     * @param actionListener click listener for action
     */
    void showSnakeBar(@NonNull String msg, @StringRes int action, View.OnClickListener actionListener);

    /**
     * shows dialog if not showing already
     *
     * @param dialog dialog
     */
    void showDialog(Dialog dialog);

    /**
     * dismisses all specified dialog if they are showing
     *
     * @param dialogs dialogs
     */
    void dismissDialogs(Dialog... dialogs);


    /**
     * to get context specific to any framework component that implements specified view
     *
     * @return context
     */
    Context getContext();

    /**
     * returns checks if component has reached its destroy state in its life cycle<br/>
     * for example for activity; it will return true if activity's onDestory() has been called. similar applies to fragment for onDestroyView()
     *
     * @return true if component has been destroyed in its lifecycle; false otherwise
     */
    boolean isDestroyed();

    /**
     * removes all callbacks and listeners on view. to be called from {@link BasePresenter#detachView()}
     */
    void removeListeners();
}
