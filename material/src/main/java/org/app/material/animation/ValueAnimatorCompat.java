/*
 * Copyright 2016 Michael Bel
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

package org.app.material.animation;

import android.view.animation.Interpolator;

public class ValueAnimatorCompat {

    public static final int INFINITE = -1;

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimatorCompat animator);
    }

    public interface AnimatorListener {
        void onAnimationStart(ValueAnimatorCompat animator);
        void onAnimationEnd(ValueAnimatorCompat animator);
        void onAnimationCancel(ValueAnimatorCompat animator);
        void onAnimationRepeat(ValueAnimatorCompat animator);
    }

    interface Creator {
        ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        interface AnimatorListenerProxy {
            void onAnimationStart();
            void onAnimationEnd();
            void onAnimationCancel();
            void onAnimationRepeat();
        }

        abstract void start();
        abstract boolean isRunning();
        abstract void setInterpolator(Interpolator interpolator);
        abstract void setListener(AnimatorListenerProxy listener);
        abstract void setUpdateListener(AnimatorUpdateListenerProxy updateListener);
        abstract void setIntValues(int from, int to);
        abstract int getAnimatedIntValue();
        abstract void setFloatValues(float from, float to);
        abstract float getAnimatedFloatValue();
        abstract void setDuration(int duration);
        abstract void cancel();
        abstract float getAnimatedFraction();
        abstract void end();
        abstract long getDuration();
        abstract void setRepeatCount(int count);
    }

    private final Impl mImpl;

    ValueAnimatorCompat(Impl impl) {
        mImpl = impl;
    }

    public void start() {
        mImpl.start();
    }

    public boolean isRunning() {
        return mImpl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        mImpl.setInterpolator(interpolator);
    }

    public void setUpdateListener(final AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            mImpl.setUpdateListener(new Impl.AnimatorUpdateListenerProxy() {
                @Override
                public void onAnimationUpdate() {
                    updateListener.onAnimationUpdate(ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.setUpdateListener(null);
        }
    }

    public void setListener(final AnimatorListener listener) {
        if (listener != null) {
            mImpl.setListener(new Impl.AnimatorListenerProxy() {
                @Override
                public void onAnimationStart() {
                    listener.onAnimationStart(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationEnd() {
                    listener.onAnimationEnd(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationCancel() {
                    listener.onAnimationCancel(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationRepeat() {
                    listener.onAnimationRepeat(ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.setListener(null);
        }
    }

    public void setFloatValues(float from, float to) {
        mImpl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValue() {
        return mImpl.getAnimatedFloatValue();
    }

    public void setDuration(int duration) {
        mImpl.setDuration(duration);
    }

    public float getAnimatedFraction() {
        return mImpl.getAnimatedFraction();
    }

    public void end() {
        mImpl.end();
    }

    public long getDuration() {
        return mImpl.getDuration();
    }
}