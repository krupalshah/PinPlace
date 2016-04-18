package com.droidexperiments.android.pinplace.api;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class ApiController {

    public static ApiController instance;

    public static ApiController getInstance() {
        if (instance == null) {
            instance = new ApiController();
        }
        return instance;
    }
}
