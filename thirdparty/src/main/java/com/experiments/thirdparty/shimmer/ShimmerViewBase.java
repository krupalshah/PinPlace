/*
 *
 *  Copyright  (c) 2016 Krupal Shah, Harsh Bhavsar
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

package com.experiments.thirdparty.shimmer;

/**
 * Shimmer
 * User: romainpiel
 * Date: 10/03/2014
 * Time: 17:33
 */
public interface ShimmerViewBase {

    public float getGradientX();

    public void setGradientX(float gradientX);

    public boolean isShimmering();

    public void setShimmering(boolean isShimmering);

    public boolean isSetUp();

    public void setAnimationSetupCallback(ShimmerViewHelper.AnimationSetupCallback callback);

    public int getPrimaryColor();

    public void setPrimaryColor(int primaryColor);

    public int getReflectionColor();

    public void setReflectionColor(int reflectionColor);
}
