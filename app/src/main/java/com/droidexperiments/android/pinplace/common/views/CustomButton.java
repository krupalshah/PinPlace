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

package com.droidexperiments.android.pinplace.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.droidexperiments.android.pinplace.R;
import com.droidexperiments.android.pinplace.utilities.FontFactory;

/**
 * Author : Krupal Shah
 * Date : 25-Apr-16
 */
public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributeSet(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributeSet(context, attrs);
    }

    private void parseAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customview);
        String strTypeFace = typedArray.getString(R.styleable.customview_typeface);
        typedArray.recycle();

        if (TextUtils.isEmpty(strTypeFace)) {
            return;
        }
        String montserratFile = context.getString(R.string.montserrat);
        String sourceSansFile = context.getString(R.string.sourcesans);
        Typeface typeface;
        if (strTypeFace.equals(montserratFile)) {
            typeface = FontFactory.getInstance().getTypeFace(context, montserratFile);
        } else if (strTypeFace.equals(sourceSansFile)) {
            typeface = FontFactory.getInstance().getTypeFace(context, sourceSansFile);
        } else {
            typeface = Typeface.DEFAULT;
        }
        setTypeface(typeface);
    }

}
