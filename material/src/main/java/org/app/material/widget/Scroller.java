/*
 * Copyright 2015 Michael Bel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.app.material.widget;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class Scroller  {

    private int mMode;
    private int mStartX;
    private int mStartY;
    private int mFinalX;
    private int mFinalY;
    private int mMinX;
    private int mMaxX;
    private int mMinY;
    private int mMaxY;
    private int mCurrY;
    private int mDuration;
    private long mStartTime;
    private float mDurationReciprocal;
    private float mVelocity;
    private float mDeltaY;
    private float mDeceleration;
    private boolean finished;
    private boolean flywheel;
    private final float ppi;
    private static float sViscousFluidScale;
    private static float sViscousFluidNormalize;
    private static final int SCROLL_MODE = 0;
    private static final int FLING_MODE = 1;
    private static final int NB_SAMPLES = 100;
    private static float DECELERATION_RATE = (float) (Math.log(0.75) / Math.log(0.9));
    private static float START_TENSION = 0.4f;
    private static float END_TENSION = 1.0f - START_TENSION;
    private static final float[] SPLINE = new float[NB_SAMPLES + 1];

    private Interpolator interpolator;

    static {
        float x_min = 0.0f;

        for (int i = 0; i <= NB_SAMPLES; i++) {
            final float t = (float) i / NB_SAMPLES;
            float x_max = 1.0f;
            float x, tx, coef;

            while (true) {
                x = x_min + (x_max - x_min) / 2.0f;
                coef = 3.0f * x * (1.0f - x);
                tx = coef * ((1.0f - x) * START_TENSION + x * END_TENSION) + x * x * x;

                if (Math.abs(tx - t) < 1E-5) {
                    break;
                }

                if (tx > t) {
                    x_max = x;
                } else {
                    x_min = x;
                }
            }

            final float d = coef + x * x * x;
            SPLINE[i] = d;
        }

        SPLINE[NB_SAMPLES] = 1.0f;
        sViscousFluidScale = 8.0f;
        sViscousFluidNormalize = 1.0f;
        sViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }

    public Scroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public Scroller(Context context, Interpolator interpolator, boolean flywheel) {
        finished = true;
        this.interpolator = interpolator;
        ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
        this.flywheel = flywheel;
    }
    
    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH * 39.37f * ppi * friction;
    }

    public final boolean isFinished() {
        return finished;
    }

    public final void forceFinished(boolean finished) {
        this.finished = finished;
    }

    public final int getCurrY() {
        return mCurrY;
    }

    public float getCurrVelocity() {
        return mVelocity - mDeceleration * timePassed() / 2000.0f;
    }

    public final int getStartY() {
        return mStartY;
    }

    public final int getFinalY() {
        return mFinalY;
    }

    public boolean computeScrollOffset() {
        if (finished) {
            return false;
        }

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
        int mCurrX;

        if (timePassed < mDuration) {
            switch (mMode) {
            case SCROLL_MODE:
                float x = timePassed * mDurationReciprocal;
    
                if (interpolator == null) {
                    x = viscousFluid(x);
                } else {
                    x = interpolator.getInterpolation(x);
                }

                mCurrY = mStartY + Math.round(x * mDeltaY);
                break;
            case FLING_MODE:
                final float t = (float) timePassed / mDuration;
                final int index = (int) (NB_SAMPLES * t);
                final float t_inf = (float) index / NB_SAMPLES;
                final float t_sup = (float) (index + 1) / NB_SAMPLES;
                final float d_inf = SPLINE[index];
                final float d_sup = SPLINE[index + 1];
                final float distanceCoef = d_inf + (t - t_inf) / (t_sup - t_inf) * (d_sup - d_inf);

                mCurrX = mStartX + Math.round(distanceCoef * (mFinalX - mStartX));
                mCurrX = Math.min(mCurrX, mMaxX);
                mCurrX = Math.max(mCurrX, mMinX);

                mCurrY = mStartY + Math.round(distanceCoef * (mFinalY - mStartY));
                mCurrY = Math.min(mCurrY, mMaxY);
                mCurrY = Math.max(mCurrY, mMinY);

                if (mCurrX == mFinalX && mCurrY == mFinalY) {
                    finished = true;
                }

                break;
            }
        } else {
            mCurrY = mFinalY;
            finished = true;
        }

        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mMode = SCROLL_MODE;
        finished = false;
        this.mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        mFinalX = startX + dx;
        mFinalY = startY + dy;
        mDeltaY = dy;
        mDurationReciprocal = 1.0f / (float) this.mDuration;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        if (flywheel && !finished) {
            float oldVel = getCurrVelocity();

            float dx = (float) (mFinalX - this.mStartX);
            float dy = (float) (mFinalY - this.mStartY);
            float hyp = (float) Math.sqrt(dx * dx + dy * dy);

            float ndx = dx / hyp;
            float ndy = dy / hyp;

            float oldVelocityX = ndx * oldVel;
            float oldVelocityY = ndy * oldVel;

            if (Math.signum(velocityX) == Math.signum(oldVelocityX) && Math.signum(velocityY) == Math.signum(oldVelocityY)) {
                velocityX += oldVelocityX;
                velocityY += oldVelocityY;
            }
        }

        mMode = FLING_MODE;
        finished = false;
        float velocity = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        this.mVelocity = velocity;
        float ALPHA = 800;
        final double l = Math.log(START_TENSION * velocity / ALPHA);

        mDuration = (int) (1000.0 * Math.exp(l / (DECELERATION_RATE - 1.0)));
        mStartTime = AnimationUtils.currentAnimationTimeMillis();

        this.mStartX = startX;
        this.mStartY = startY;

        float coeffX = velocity == 0 ? 1.0f : velocityX / velocity;
        float coeffY = velocity == 0 ? 1.0f : velocityY / velocity;

        int totalDistance = (int) (ALPHA * Math.exp(DECELERATION_RATE / (DECELERATION_RATE - 1.0) * l));

        this.mMinX = minX;
        this.mMaxX = maxX;
        this.mMinY = minY;
        this.mMaxY = maxY;

        mFinalX = startX + Math.round(totalDistance * coeffX);
        mFinalX = Math.min(mFinalX, this.mMaxX);
        mFinalX = Math.max(mFinalX, this.mMinX);
        mFinalY = startY + Math.round(totalDistance * coeffY);
        mFinalY = Math.min(mFinalY, this.mMaxY);
        mFinalY = Math.max(mFinalY, this.mMinY);
    }
    
    static float viscousFluid(float x) {
        x *= sViscousFluidScale;

        if (x < 1.0f) {
            x -= (1.0f - (float)Math.exp(-x));
        } else {
            float start = 0.36787944117f;
            x = 1.0f - (float)Math.exp(1.0f - x);
            x = start + x * (1.0f - start);
        }

        x *= sViscousFluidNormalize;
        return x;
    }

    public int timePassed() {
        return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    }
}