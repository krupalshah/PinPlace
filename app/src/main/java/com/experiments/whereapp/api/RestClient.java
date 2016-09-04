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

package com.experiments.whereapp.api;

import com.experiments.whereapp.config.AppConfig;
import com.experiments.whereapp.config.ServerConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 * <p>
 * configures OkHttp client and provides {@link WebServices}
 */
public class RestClient {

    private static WebServices webServices;

    /**
     * builds api client and provides reference to {@link WebServices}
     *
     * @return reference to {@link WebServices} created with retrofit
     */
    public static WebServices getWebServices() {

        if (webServices == null) {  //don't build it every time

            //configuring OkHttp client with timeouts defined
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(ServerConfig.TimeOuts.CONNECT, TimeUnit.SECONDS)
                    .readTimeout(ServerConfig.TimeOuts.READ, TimeUnit.SECONDS)
                    .writeTimeout(ServerConfig.TimeOuts.WRITE, TimeUnit.SECONDS);

            //enabling logging interceptor for debug builds
            if (AppConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }

            //configuring retrofit with jackson converter and rxjava call adapter
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerConfig.baseUrl())
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            webServices = retrofit.create(WebServices.class);
        }
        return webServices;
    }
}
