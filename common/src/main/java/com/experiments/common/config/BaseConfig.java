package com.experiments.common.config;

/**
 * Author : Krupal Shah
 * Date : 23-Jun-16
 * <p>
 * base class for all configuration classes
 */
public abstract class BaseConfig {

    /**
     * repository for defining location update intervals
     */
    public interface LocationUpdates {
        long NORMAL_LOCATION_UPDATE_INTERVAL = 20 * 1000;
        long FASTEST_LOCATION_UPDATE_INTERVAL = 10 * 1000;
    }
}
