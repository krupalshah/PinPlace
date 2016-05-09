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

package com.droidexperiments.android.pinplace.api;

import com.droidexperiments.android.pinplace.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class ApiProvider {

    private static ApiRepository apiRepository;

    public static ApiRepository getApi() {
        if (apiRepository == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(ServerConfig.TimeOuts.CONNECT, TimeUnit.SECONDS)
                    .readTimeout(ServerConfig.TimeOuts.READ, TimeUnit.SECONDS)
                    .writeTimeout(ServerConfig.TimeOuts.WRITE, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerConfig.getBaseUrl())
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            apiRepository = retrofit.create(ApiRepository.class);
        }
        return apiRepository;
    }
}
