package com.experiments.whereapp.db;

import com.experiments.whereapp.config.DbConfig;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Krupal Shah on 26-Jun-16.
 */
@Table(databaseName = DbConfig.NAME)
public class PlaceId extends BaseModel {

    @Column
    @PrimaryKey
    long placeId;
}
