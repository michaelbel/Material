/*
 * Copyright (C) 2015 Thomas Robert Altstidl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.app.material.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class MediaControlDrawable extends Drawable {

    public enum State {
        PLAY, PAUSE, STOP
    }

    private float mPadding = 0f;
    private float mRotation;
    private float mCenter;
    private float mSize;
    private float mPlayTipOffset;
    private float mPlayBaseOffset;
    private Path mPrimaryPath = new Path();
    private Path mSecondaryPath = new Path();
    private RectF mInternalBounds = new RectF();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private State mCurrentState = State.PLAY;
    private State mTargetState = State.PLAY;
    private ValueAnimatorCompat mAnimator;

    public MediaControlDrawable(Context context, @ColorInt int color, float padding, State state, Interpolator interpolator, int duration) {
        mPadding = padding;
        mCurrentState = state;
        mTargetState = state;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        mAnimator = AnimationUtils.createAnimator();
        mAnimator.setFloatValues(0F, 90F);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(interpolator);
        mAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimatorCompat animator) {
                setTransitionState(animator.getAnimatedFloatValue(), animator.getAnimatedFraction());
            }
        });
        mAnimator.setListener(new ValueAnimatorCompat.AnimatorListener() {
            @Override
            public void onAnimationStart(ValueAnimatorCompat animator) {}

            @Override
            public void onAnimationEnd(ValueAnimatorCompat animator) {
                mCurrentState = mTargetState;
                setTransitionState(0F, 0F);
            }

            @Override
            public void onAnimationCancel(ValueAnimatorCompat animator) {}

            @Override
            public void onAnimationRepeat(ValueAnimatorCompat animator) {}
        });
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int saveCount = canvas.save();
        canvas.rotate(mRotation, bounds.centerX(), bounds.centerY());
        canvas.drawPath(mPrimaryPath, mPaint);
        canvas.drawPath(mSecondaryPath, mPaint);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setBounds(Rect bounds) {
        calculateTrimArea(bounds);
        super.setBounds(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        calculateTrimArea(new Rect(left, top, right, bottom));
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    private void setTransitionState(float rotation, float fraction) {
        if (mCurrentState == mTargetState) {
            rotation = fraction = 0f;
        }

        mRotation = rotation;
        mPrimaryPath.reset();
        mSecondaryPath.reset();

        if (mCurrentState == State.PLAY && (mTargetState == State.STOP || mTargetState == State.PLAY)) {
            float offset = (1f - fraction) * mPlayTipOffset;
            float offsetBase = (1f - fraction) * mPlayBaseOffset;

            mPrimaryPath.moveTo(mInternalBounds.right + offset, interpolate(mCenter, mInternalBounds.bottom, fraction));
            mPrimaryPath.lineTo(mInternalBounds.left + offset, mInternalBounds.bottom + offsetBase);
            mPrimaryPath.lineTo(mInternalBounds.left + offset, interpolate(mCenter, mInternalBounds.top, fraction));
            mPrimaryPath.lineTo(interpolate(mInternalBounds.left, mInternalBounds.right, fraction) + offset, mInternalBounds.top - offsetBase);
        } else if (mCurrentState == State.STOP && mTargetState == State.PAUSE) {
            float primaryBottom = mCenter - fraction * 3F / 20F * mSize;
            float secondaryTop = mCenter + fraction * 3F / 20F * mSize;

            mPrimaryPath.moveTo(mInternalBounds.right, mInternalBounds.top);
            mPrimaryPath.lineTo(mInternalBounds.right, primaryBottom);
            mPrimaryPath.lineTo(mInternalBounds.left, primaryBottom);
            mPrimaryPath.lineTo(mInternalBounds.left, mInternalBounds.top);
            mSecondaryPath.moveTo(mInternalBounds.right, mInternalBounds.bottom);
            mSecondaryPath.lineTo(mInternalBounds.right, secondaryTop);
            mSecondaryPath.lineTo(mInternalBounds.left, secondaryTop);
            mSecondaryPath.lineTo(mInternalBounds.left, mInternalBounds.bottom);
        } else if (mCurrentState == State.PAUSE && mTargetState == State.PLAY) {
            float offset = fraction * mPlayTipOffset;
            float offsetBase = fraction * mPlayBaseOffset;

            if (fraction < 0.5f) {
                float primaryRight = mCenter - (-2f * fraction + 1f) * 3f / 20f * mSize;
                float primaryBottomLeft = mInternalBounds.left + fraction * (mSize / 2f);
                float secondaryLeft = mCenter + (-2f * fraction + 1f) * 3f / 20f * mSize;
                float secondaryBottomRight = mInternalBounds.right - fraction * (mSize / 2f);

                mPrimaryPath.moveTo(mInternalBounds.left - offsetBase, mInternalBounds.bottom - offset);
                mPrimaryPath.lineTo(primaryRight, mInternalBounds.bottom - offset);
                mPrimaryPath.lineTo(primaryRight, mInternalBounds.top - offset);
                mPrimaryPath.lineTo(primaryBottomLeft, mInternalBounds.top - offset);
                mSecondaryPath.moveTo(mInternalBounds.right + offsetBase, mInternalBounds.bottom - offset);
                mSecondaryPath.lineTo(secondaryLeft, mInternalBounds.bottom - offset);
                mSecondaryPath.lineTo(secondaryLeft, mInternalBounds.top - offset);
                mSecondaryPath.lineTo(secondaryBottomRight, mInternalBounds.top - offset);
            } else {
                float primaryBottomLeft = mInternalBounds.left + fraction * (mSize / 2f);
                float secondaryBottomRight = mInternalBounds.right - fraction * (mSize / 2f);

                mPrimaryPath.moveTo(mInternalBounds.left - offsetBase, mInternalBounds.bottom - offset);
                mPrimaryPath.lineTo(primaryBottomLeft, mInternalBounds.top - offset);
                mPrimaryPath.lineTo(secondaryBottomRight, mInternalBounds.top - offset);
                mPrimaryPath.lineTo(mInternalBounds.right + offsetBase, mInternalBounds.bottom - offset);
            }
        } else if (mCurrentState == State.PLAY && mTargetState == State.PAUSE) {
            float offset = (1f - fraction) * mPlayTipOffset;
            float offsetBase = (1f - fraction) * mPlayBaseOffset;

            if (fraction > 0.5f) {
                float primaryBottom = mCenter - (2f * fraction - 1f) * 3f / 20f * mSize;
                float primaryLeftTop = mInternalBounds.left + (1f - fraction) * (mSize / 2f);
                float secondaryTop = mCenter + (2f * fraction - 1f) * 3f / 20f * mSize;
                float secondaryLeftBottom = mInternalBounds.right - (1f - fraction) * (mSize / 2f);

                mPrimaryPath.moveTo(mInternalBounds.left + offset, mInternalBounds.top - offsetBase);
                mPrimaryPath.lineTo(mInternalBounds.left + offset, primaryBottom);
                mPrimaryPath.lineTo(mInternalBounds.right + offset, primaryBottom);
                mPrimaryPath.lineTo(mInternalBounds.right + offset, primaryLeftTop);
                mSecondaryPath.moveTo(mInternalBounds.left + offset, mInternalBounds.bottom + offsetBase);
                mSecondaryPath.lineTo(mInternalBounds.left + offset, secondaryTop);
                mSecondaryPath.lineTo(mInternalBounds.right + offset, secondaryTop);
                mSecondaryPath.lineTo(mInternalBounds.right + offset, secondaryLeftBottom);
            } else {
                float primaryLeftTop = mInternalBounds.left + (1f - fraction) * (mSize / 2f);
                float secondaryLeftBottom = mInternalBounds.right - (1f - fraction) * (mSize / 2f);

                mPrimaryPath.moveTo(mInternalBounds.left + offset, mInternalBounds.top - offsetBase);
                mPrimaryPath.lineTo(mInternalBounds.right + offset, primaryLeftTop);
                mPrimaryPath.lineTo(mInternalBounds.right + offset, secondaryLeftBottom);
                mPrimaryPath.lineTo(mInternalBounds.left + offset, mInternalBounds.bottom + offsetBase);
            }
        } else if (mCurrentState == State.PAUSE && (mTargetState == State.STOP || mTargetState == State.PAUSE)) {
            float primaryRight = mCenter - (1f - fraction) * 3f / 20f * mSize;
            float secondaryLeft = mCenter + (1f - fraction) * 3f / 20f * mSize;

            mPrimaryPath.moveTo(mInternalBounds.left, mInternalBounds.top);
            mPrimaryPath.lineTo(primaryRight, mInternalBounds.top);
            mPrimaryPath.lineTo(primaryRight, mInternalBounds.bottom);
            mPrimaryPath.lineTo(mInternalBounds.left, mInternalBounds.bottom);
            mSecondaryPath.moveTo(mInternalBounds.right, mInternalBounds.top);
            mSecondaryPath.lineTo(secondaryLeft, mInternalBounds.top);
            mSecondaryPath.lineTo(secondaryLeft, mInternalBounds.bottom);
            mSecondaryPath.lineTo(mInternalBounds.right, mInternalBounds.bottom);
        } else if (mCurrentState == State.STOP && (mTargetState == State.PLAY || mTargetState == State.STOP)) {
            float offset = fraction * mPlayTipOffset;
            float offsetBase = fraction * mPlayBaseOffset;

            mPrimaryPath.moveTo(interpolate(mInternalBounds.left, mCenter, fraction), mInternalBounds.top - offset);
            mPrimaryPath.lineTo(mInternalBounds.left - offsetBase, mInternalBounds.bottom - offset);
            mPrimaryPath.lineTo(interpolate(mInternalBounds.right, mCenter, fraction), mInternalBounds.bottom - offset);
            mPrimaryPath.lineTo(mInternalBounds.right + offsetBase, interpolate(mInternalBounds.top, mInternalBounds.bottom, fraction) - offset);
        }

        invalidateSelf();
    }

    private void calculateTrimArea(Rect bounds) {
        float size = Math.min(bounds.height(), bounds.width());
        float yOffset = (bounds.height() - size) / 2f;
        float xOffset = (bounds.width() - size) / 2f;
        float padding = mPadding + (bounds.height() - 2f * mPadding) * 1f / 6f;

        mInternalBounds.set(bounds.left + padding + xOffset, bounds.top + padding + yOffset, bounds.right - padding - xOffset, bounds.bottom - padding - yOffset);
        mCenter = mInternalBounds.centerX();
        mSize = mInternalBounds.width();
        mPlayTipOffset = 1f / 6f * mSize;
        mPlayBaseOffset = 0.07735f * mSize;
        setTransitionState(0f, 0f);
    }

    private float interpolate(float start, float end, float fraction) {
        return (1f - fraction) * start + fraction * end;
    }

    public void setMediaControlState(State state) {
        if (mAnimator.isRunning()) {
            mAnimator.end();
        }

        mTargetState = state;
        mAnimator.start();
    }

    public State getMediaControlState() {
        return mCurrentState;
    }

    public static class Builder {

        private int mColor;
        private int mAnimationDuration;
        private float mPadding;
        private State mInitialState;
        private Context mContext;
        private Interpolator mAnimationInterpolator;

        public Builder(Context context) {
            mContext = context;
            mColor = 0xFFFFFFFF;
            mPadding = 0f;
            mInitialState = State.PLAY;
            mAnimationInterpolator = AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR;
            mAnimationDuration = 400; // 500
        }

        public Builder setColor(@ColorInt int color) {
            mColor = color;
            return this;
        }

        public Builder setPadding(float padding) {
            mPadding = padding;
            return this;
        }

        public Builder setInitialState(State initialState) {
            mInitialState = initialState;
            return this;
        }

        public Builder setAnimationInterpolator(Interpolator interpolator) {
            mAnimationInterpolator = interpolator;
            return this;
        }

        public Builder setAnimationDuration(int duration) {
            mAnimationDuration = duration;
            return this;
        }

        public MediaControlDrawable build() {
            return new MediaControlDrawable(mContext, mColor, mPadding, mInitialState, mAnimationInterpolator, mAnimationDuration);
        }
    }

    public static class AnimationUtils {

        public static final Interpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();

        public static float lerp(float startValue, float endValue, float fraction) {
            return startValue + (fraction * (endValue - startValue));
        }

        public static int lerp(int startValue, int endValue, float fraction) {
            return startValue + Math.round(fraction * (endValue - startValue));
        }

        static final ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATOR = new ValueAnimatorCompat.Creator() {
            @Override
            public ValueAnimatorCompat createAnimator() {
                return new ValueAnimatorCompat(Build.VERSION.SDK_INT >= 12 ? new ValueAnimatorCompatImplHoneycombMr1() : new ValueAnimatorCompatImplEclairMr1());
            }
        };

        public static ValueAnimatorCompat createAnimator() {
            return DEFAULT_ANIMATOR_CREATOR.createAnimator();
        }

        public static class ValueAnimatorCompatImplEclairMr1 extends ValueAnimatorCompat.Impl {

            private static final int HANDLER_DELAY = 10;
            private static final int DEFAULT_DURATION = 200;
            private static final int DEFAULT_REPEAT_COUNT = 0;

            private static final Handler sHandler = new Handler(Looper.getMainLooper());

            private long mStartTime;
            private boolean mIsRunning;

            private final int[] mIntValues = new int[2];
            private final float[] mFloatValues = new float[2];

            private int mDuration = DEFAULT_DURATION;
            private int mRepeatCount = DEFAULT_REPEAT_COUNT;
            private Interpolator mInterpolator;
            private AnimatorListenerProxy mListener;
            private AnimatorUpdateListenerProxy mUpdateListener;

            private int mCurrentIteration = 0;

            private float mAnimatedFraction;

            @Override
            public void start() {
                if (mIsRunning) {
                    return;
                }

                if (mInterpolator == null) {
                    mInterpolator = new AccelerateDecelerateInterpolator();
                }

                mStartTime = SystemClock.uptimeMillis();
                mIsRunning = true;

                if (mListener != null) {
                    mListener.onAnimationStart();
                }

                sHandler.postDelayed(mRunnable, HANDLER_DELAY);
            }

            @Override
            public boolean isRunning() {
                return mIsRunning;
            }

            @Override
            public void setInterpolator(Interpolator interpolator) {
                mInterpolator = interpolator;
            }

            @Override
            public void setListener(AnimatorListenerProxy listener) {
                mListener = listener;
            }

            @Override
            public void setUpdateListener(AnimatorUpdateListenerProxy updateListener) {
                mUpdateListener = updateListener;
            }

            @Override
            public void setRepeatCount(int count) {
                mRepeatCount = count;
            }

            @Override
            public void setIntValues(int from, int to) {
                mIntValues[0] = from;
                mIntValues[1] = to;
            }

            @Override
            public int getAnimatedIntValue() {
                return AnimationUtils.lerp(mIntValues[0], mIntValues[1], getAnimatedFraction());
            }

            @Override
            public void setFloatValues(float from, float to) {
                mFloatValues[0] = from;
                mFloatValues[1] = to;
            }

            @Override
            public float getAnimatedFloatValue() {
                return AnimationUtils.lerp(mFloatValues[0], mFloatValues[1], getAnimatedFraction());
            }

            @Override
            public void setDuration(int duration) {
                mDuration = duration;
            }

            @Override
            public void cancel() {
                mIsRunning = false;
                sHandler.removeCallbacks(mRunnable);

                if (mListener != null) {
                    mListener.onAnimationCancel();
                }
            }

            @Override
            public float getAnimatedFraction() {
                return mAnimatedFraction;
            }

            @Override
            public void end() {
                if (mIsRunning) {
                    mIsRunning = false;
                    sHandler.removeCallbacks(mRunnable);

                    mAnimatedFraction = 1f;

                    if (mUpdateListener != null) {
                        mUpdateListener.onAnimationUpdate();
                    }

                    if (mListener != null) {
                        mListener.onAnimationEnd();
                    }
                }
            }

            @Override
            public long getDuration() {
                return mDuration;
            }

            private void update() {
                if (mIsRunning) {
                    final long elapsed = SystemClock.uptimeMillis() - mStartTime;
                    final float linearFraction = elapsed / (float) mDuration;
                    mAnimatedFraction = mInterpolator != null ? mInterpolator.getInterpolation(linearFraction) : linearFraction;

                    if (mUpdateListener != null) {
                        mUpdateListener.onAnimationUpdate();
                    }

                    if (SystemClock.uptimeMillis() >= (mStartTime + mDuration)) {
                        if (mRepeatCount < mCurrentIteration || mRepeatCount == ValueAnimatorCompat.INFINITE) {
                            mCurrentIteration += 1;
                            mStartTime += mDuration;

                            if (mListener != null) {
                                mListener.onAnimationRepeat();
                            }
                        } else {
                            mIsRunning = false;

                            if (mListener != null) {
                                mListener.onAnimationEnd();
                            }
                        }
                    }
                }

                if (mIsRunning) {
                    sHandler.postDelayed(mRunnable, HANDLER_DELAY);
                }
            }

            private final Runnable mRunnable = new Runnable() {
                public void run() {
                    update();
                }
            };
        }

        public static class ValueAnimatorCompatImplHoneycombMr1 extends ValueAnimatorCompat.Impl {

            final ValueAnimator mValueAnimator;

            ValueAnimatorCompatImplHoneycombMr1() {
                mValueAnimator = new ValueAnimator();
            }

            @Override
            public void start() {
                mValueAnimator.start();
            }

            @Override
            public boolean isRunning() {
                return mValueAnimator.isRunning();
            }

            @Override
            public void setInterpolator(Interpolator interpolator) {
                mValueAnimator.setInterpolator(interpolator);
            }

            @Override
            public void setUpdateListener(final AnimatorUpdateListenerProxy updateListener) {
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        updateListener.onAnimationUpdate();
                    }
                });
            }

            @Override
            public void setListener(final AnimatorListenerProxy listener) {
                mValueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        listener.onAnimationStart();
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        listener.onAnimationEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        listener.onAnimationCancel();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        listener.onAnimationRepeat();
                    }
                });
            }

            @Override
            public void setRepeatCount(int count) {
                mValueAnimator.setRepeatCount(count);
            }

            @Override
            public void setIntValues(int from, int to) {
                mValueAnimator.setIntValues(from, to);
            }

            @Override
            public int getAnimatedIntValue() {
                return (int) mValueAnimator.getAnimatedValue();
            }

            @Override
            public void setFloatValues(float from, float to) {
                mValueAnimator.setFloatValues(from, to);
            }

            @Override
            public float getAnimatedFloatValue() {
                return (float) mValueAnimator.getAnimatedValue();
            }

            @Override
            public void setDuration(int duration) {
                mValueAnimator.setDuration(duration);
            }

            @Override
            public void cancel() {
                mValueAnimator.cancel();
            }

            @Override
            public float getAnimatedFraction() {
                return mValueAnimator.getAnimatedFraction();
            }

            @Override
            public void end() {
                mValueAnimator.end();
            }

            @Override
            public long getDuration() {
                return mValueAnimator.getDuration();
            }
        }
    }

    public static class ValueAnimatorCompat {

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
}