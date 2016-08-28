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

package com.experiments.common.mvp.models;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 * <p>
 * base class for all responses from apis
 */
public abstract class BaseResponse {

    private int statusCode;
    private String status;

    public BaseResponse() {
        status = "";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"BaseResponse\":{"
                + "\"statusCode\":\"" + statusCode + "\""
                + ", \"status\":\"" + status + "\""
                + "}}";
    }
}
