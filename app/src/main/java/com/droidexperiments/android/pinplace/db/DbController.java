package com.droidexperiments.android.pinplace.db;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class DbController {
    public static DbController instance;

    public static DbController getInstance() {
        if(instance==null){
            instance = new DbController();
        }
        return instance;
    }
}
