/*
 *
 *  Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.experiments.whereapp.modules.home.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidexperiments.android.where.R;
import com.experiments.core.android.fragments.BaseFragment;
import com.experiments.thirdparty.shimmer.Shimmer;
import com.experiments.thirdparty.shimmer.ShimmerTextView;
import com.experiments.whereapp.modules.home.presenters.AddressPresenter;
import com.experiments.whereapp.modules.home.views.AddressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by Krupal Shah on 04-Sep-16.
 */
public class AddressFragment extends BaseFragment implements AddressView {

    public static final long DELAY_STOP_ANIMATION = 750;

    protected Shimmer shimmer;
    protected Handler animationHandler;
    protected Runnable stopAnimationRunnable;

    @BindView(R.id.txt_current_place_address)
    ShimmerTextView txtCurrentPlaceAddress;

    private AddressPresenter addressPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    @CallSuper
    @Override
    protected void initComponents() {
        shimmer = new Shimmer();
        animationHandler = new Handler();
        stopAnimationRunnable = () -> stopAnimatingAddress(true);

        addressPresenter = AddressPresenter.create();
        addressPresenter.attachView(this);
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        addressPresenter.listenEvents();
    }

    @CallSuper
    @Override
    public void onStop() {
        addressPresenter.stopListeningEvents();
        super.onStop();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        addressPresenter.detachView();
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void removeListeners() {
        animationHandler.removeCallbacksAndMessages(null);
    }


    @DebugLog
    @Override
    public void updateAddress(@Nullable String address) {
        if (address == null) {
            txtCurrentPlaceAddress.setVisibility(View.GONE);
        } else {
            txtCurrentPlaceAddress.setVisibility(View.VISIBLE);
            txtCurrentPlaceAddress.setText(address);
        }
    }

    @DebugLog
    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) return;
        showSnakeBar(message, android.R.string.ok, null);
    }

    @DebugLog
    @Override
    public void animateAddress() {
        if (shimmer.isAnimating()) return;
        shimmer.start(txtCurrentPlaceAddress);
    }

    @DebugLog
    @Override
    public void stopAnimatingAddress(boolean stopImmediate) {
        if (stopImmediate) {
            if (shimmer.isAnimating()) {
                shimmer.cancel();
            }
        } else {
            animationHandler.postDelayed(stopAnimationRunnable, DELAY_STOP_ANIMATION);
        }
    }
}
