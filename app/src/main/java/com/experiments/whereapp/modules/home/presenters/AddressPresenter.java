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

package com.experiments.whereapp.modules.home.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.droidexperiments.android.where.R;
import com.experiments.core.exceptions.NoInternetException;
import com.experiments.core.exceptions.PermissionDeniedException;
import com.experiments.core.mvp.presenters.BasePresenter;
import com.experiments.whereapp.events.places.GetPlaceErrorEvent;
import com.experiments.whereapp.events.places.UpdateCurrentPlaceEvent;
import com.experiments.whereapp.modules.home.views.AddressView;
import com.google.android.gms.location.places.Place;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hugo.weaving.DebugLog;

/**
 * Created by Krupal Shah on 04-Sep-16.
 */
public class AddressPresenter extends BasePresenter<AddressView> {

    private static final String TAG = "AddressPresenter";

    private final EventBus eventBus;

    //avoiding direct instances. use factory method instead.
    private AddressPresenter() {
        eventBus = EventBus.getDefault();
    }

    public static AddressPresenter create() {
        return new AddressPresenter();
    }

    @Override
    public void attachView(AddressView view) {
        super.attachView(view);
        view.animateAddress();
    }

    @Override
    public void detachView() {
        view.stopAnimatingAddress(true);
        super.detachView();
    }

    @DebugLog
    public void listenEvents() {
        eventBus.register(this);
    }

    @DebugLog
    public void stopListeningEvents() {
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateCurrentPlaceEvent(UpdateCurrentPlaceEvent updateCurrentPlaceEvent) {
        Place place = updateCurrentPlaceEvent.getCurrentPlace().getPlaceData();
        if (place == null) {
            view.stopAnimatingAddress(true);
            view.updateAddress(null);
            return;
        }

        //updating address
        CharSequence address = place.getAddress();
        if (TextUtils.isEmpty(address)) {
            view.stopAnimatingAddress(true);
            view.updateAddress(null);
            return;
        }

        view.animateAddress();
        view.updateAddress(address.toString());
        view.stopAnimatingAddress(false);
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorGettingPlaceDetails(GetPlaceErrorEvent getPlaceErrorEvent) {
        Throwable exception = getPlaceErrorEvent.getException();
        exception.printStackTrace();

        view.stopAnimatingAddress(true);
        view.updateAddress(null);

        if (exception instanceof NoInternetException) {
            view.showError(R.string.err_no_internet);
        } else if (exception instanceof PermissionDeniedException) {
            Log.e(TAG, "onErrorGettingPlaceEvent: got PermissionDeniedException");
        } else {
            view.showError(R.string.err_fetching_place_info);
        }
    }
}
