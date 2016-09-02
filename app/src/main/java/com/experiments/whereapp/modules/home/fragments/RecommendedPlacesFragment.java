/*
 *   Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidexperiments.android.where.R;
import com.experiments.common.android.fragments.BaseFragment;
import com.experiments.whereapp.events.OnCurrentPlaceUpdated;
import com.google.android.gms.location.places.Place;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class RecommendedPlacesFragment extends BaseFragment {

    @BindView(R.id.tv_address)
    TextView tvAddress;

    public static RecommendedPlacesFragment newInstance() {
        return new RecommendedPlacesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initComponents() {

    }

    @Override
    public void removeListeners() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrentPlaceUpdatedEvent(OnCurrentPlaceUpdated onCurrentPlaceUpdated) {
        Place place = onCurrentPlaceUpdated.getCurrentPlace().getPlaceData();
        updateAddressText(place);
    }

    private void updateAddressText(Place place) {
        if (place == null || TextUtils.isEmpty(place.getAddress())) {
            return;
        }
        tvAddress.setText(place.getAddress());
    }
}
