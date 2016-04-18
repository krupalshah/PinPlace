package com.droidexperiments.android.pinplace.interfaces.presenters;

import android.app.Dialog;
import android.support.annotation.StringRes;

import com.droidexperiments.android.pinplace.utilities.PermissionsHelper;

/**
 * Author : Krupal Shah
 * Date : 10-Apr-16
 */
public interface BaseAppPresenter {

    void showToast(@StringRes int msgResId);

    void showToast(String msg);

    void dismissDialogs(Dialog... dialogs);

    PermissionsHelper providePermissionsHelper();

    boolean isComponentDestroyed();

}
