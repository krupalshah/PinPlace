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

package com.experiments.common.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.experiments.common.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Author : Krupal Shah
 * Date : 01-Mar-16
 */
public class FontFactory {

    private static FontFactory instance;
    private final Map<String, Typeface> mapFonts;

    private FontFactory() {
        mapFonts = new HashMap<>();
    }

    public synchronized static FontFactory getInstance() {
        if (instance == null) {
            instance = new FontFactory();
        }
        return instance;
    }

    @Nullable
    public Typeface getTypeFace(Context context, String fontFileNameWithExt) {
        Typeface typeface;
        Context mContext = context.getApplicationContext();
        if (!mapFonts.containsKey(fontFileNameWithExt)) {
            typeface = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.fonts_folder_path_without_trailing_separator) + File.separator + fontFileNameWithExt);
            mapFonts.put(fontFileNameWithExt, typeface);
            return typeface;
        }
        return mapFonts.get(fontFileNameWithExt);
    }
}
