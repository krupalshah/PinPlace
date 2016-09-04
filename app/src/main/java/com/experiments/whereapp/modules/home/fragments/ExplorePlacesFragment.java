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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidexperiments.android.where.R;
import com.experiments.core.android.fragments.BaseFragment;
import com.experiments.whereapp.modules.home.presenters.ExplorePlacesPresenter;
import com.experiments.whereapp.modules.home.views.ExplorePlacesView;

import butterknife.ButterKnife;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class ExplorePlacesFragment extends BaseFragment implements ExplorePlacesView {

    private ExplorePlacesPresenter explorePlacesPresenter;

    @NonNull
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    @Override
    protected void initComponents() {
        explorePlacesPresenter = ExplorePlacesPresenter.create();
        explorePlacesPresenter.attachView(this);
    }

    @Override
    public void removeListeners() {

    }

    @Override
    public void onDestroyView() {
        explorePlacesPresenter.detachView();
        super.onDestroyView();
    }

}
