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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidexperiments.android.where.R;
import com.experiments.thirdparty.shimmer.Shimmer;
import com.experiments.thirdparty.shimmer.ShimmerTextView;
import com.experiments.whereapp.modules.home.views.ExplorePlacesView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class ExplorePlacesFragment extends BaseHomePagerFragment implements ExplorePlacesView {

    public static final long DELAY_STOP_ANIMATION = 1000;
    @BindView(R.id.txt_current_place_address)
    ShimmerTextView txtCurrentPlaceAddress;
    private Shimmer shimmer;
    private Handler animationHandler;
    private Runnable stopAnimationRunnable;

    public static Fragment newInstance() {
        return new ExplorePlacesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        shimmer = new Shimmer();
        animationHandler = new Handler();
        stopAnimationRunnable = () -> {
            if (shimmer.isAnimating()) {
                shimmer.cancel();
            }
        };
        if (isVisible()) {
            shimmer.start(txtCurrentPlaceAddress);
        }
    }

    @Override
    public void removeListeners() {
        animationHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void updateAddress(CharSequence address) {
        if (isVisible() && !shimmer.isAnimating()) {
            shimmer.start(txtCurrentPlaceAddress);
        }
        txtCurrentPlaceAddress.setText(address);
        if (shimmer.isAnimating()) {
            animationHandler.postDelayed(stopAnimationRunnable, DELAY_STOP_ANIMATION);
        }
    }
}
