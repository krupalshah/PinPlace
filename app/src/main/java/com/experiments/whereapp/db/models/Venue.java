package com.experiments.whereapp.db.models;

import android.text.TextUtils;

import com.experiments.common.mvp.models.ModelValidator;
import com.experiments.whereapp.config.DbConfig;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Krupal Shah on 26-Jun-16.
 */
@Table(databaseName = DbConfig.NAME)
public class Venue extends BaseModel implements ModelValidator {

    @Column
    @PrimaryKey
    String venueId;

    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                '}';
    }

    @Override
    public boolean isValid() {
        return !TextUtils.isEmpty(venueId);
    }
}
