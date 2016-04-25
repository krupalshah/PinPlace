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

package com.droidexperiments.android.pinplace.config;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Author : Krupal Shah
 * Date : 17-Apr-16
 */
public class SqliteConfig {

    @Database(name = OriginalConfig.NAME, version = OriginalConfig.VERSION)
    public interface OriginalConfig {
        String NAME = "PinPlaceDb";
        int VERSION = 1;
    }

   /* @Migration(version = MigrationConfig.VERSION, databaseName = OriginalConfig.NAME)
    public class MigrationConfig extends BaseMigration {
        public static final int VERSION = 2;

        @Override
        public void migrate(SQLiteDatabase sqLiteDatabase) {

        }
    }*/
}
