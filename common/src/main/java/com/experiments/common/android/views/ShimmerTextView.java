/*
 *   Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.experiments.common.android.views;

/**
 * Created by Krupal Shah on 02-Sep-16.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import hugo.weaving.DebugLog;


public class ShimmerTextView extends CustomTextView {

    private static final String TAG = "ShinnyTextView";
    private float mGradientDiameter = 0.3f;
    private ValueAnimator mAnimator;
    private float mGradientCenter;
    private PaintDrawable mShineDrawable;

    public ShimmerTextView(final Context context) {
        this(context, null);
    }

    public ShimmerTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShimmerTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mAnimator = ValueAnimator.ofFloat(0, 1);
    }

    @DebugLog
    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged() called with: " + "w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");

        if (isInEditMode()) return;

        mShineDrawable = new PaintDrawable();
        mShineDrawable.setBounds(0, 0, w, h);
        Shader shader = generateGradientShader(getWidth(), 0, 0, 0);
        mShineDrawable.getPaint().setShader(shader);
        mShineDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mAnimator.setDuration(w); // custom duration
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> updateView(animation, w));
    }

    private void updateView(ValueAnimator animation, int width) {
        final float value = animation.getAnimatedFraction();
        mGradientCenter = (1 + 2 * mGradientDiameter) * value - mGradientDiameter;
        final float gradientStart = mGradientCenter - mGradientDiameter;
        final float gradientEnd = mGradientCenter + mGradientDiameter;
        Shader shader = generateGradientShader(width, gradientStart, mGradientCenter, gradientEnd);
        mShineDrawable.getPaint().setShader(shader);
        invalidate();
    }

    @DebugLog
    public void startAnimation() {
        if (mAnimator == null) {
            Log.e(TAG, "startAnimation: animator is null");
            return;
        }
        mAnimator.start();
    }

    @DebugLog
    public void stopAnimation() {
        if (mAnimator == null) {
            Log.e(TAG, "stopAnimation: animator is null");
            return;
        }

        mAnimator.end();
        mAnimator.removeAllUpdateListeners();
        //removing gray color
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && mShineDrawable != null) {
            mShineDrawable.draw(canvas);
        }
    }

    private Shader generateGradientShader(int width, float... positions) {
        int[] colorRepartition = {Color.GRAY, Color.WHITE, Color.GRAY};
        return new LinearGradient(
                0,
                0,
                width,
                0,
                colorRepartition,
                positions,
                Shader.TileMode.REPEAT
        );
    }
}
