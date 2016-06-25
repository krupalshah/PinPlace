package com.experiments.common.config;

/**
 * Author : Krupal Shah
 * Date : 23-Jun-16
 */
public abstract class BaseConfig {

    public interface LocationUpdates {
        long NORMAL_LOCATION_UPDATE_INTERVAL = 20 * 1000;
        long FASTEST_LOCATION_UPDATE_INTERVAL = 10 * 1000;
    }
}
