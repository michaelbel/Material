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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import org.app.material.AndroidUtilities;
import org.app.material.Logger;
import org.app.material.R;

import java.lang.reflect.Method;

public class RadioButton extends View {
    private static final int ANIMATION_DURATION = 200;

    private int mAccentColor;
    private int mBorderColor;
    private int mDisabledColor;

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private static Paint paint;
    private static Paint eraser;
    private static Paint checkedPaint;
    private float progress;
    private ObjectAnimator mAnimator;
    private boolean attachedToWindow;
    private boolean isChecked;
    private int size;

    private boolean darkTheme;

    public RadioButton(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        AndroidUtilities.bind(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioButton, defStyleAttr, 0);
        darkTheme = a.getBoolean(R.styleable.RadioButton_radioButton_darkTheme, false);
        mAccentColor = a.getColor(R.styleable.RadioButton_radioButton_accentColor, 0xFF009688);
        mBorderColor =  darkTheme ? 0xB3FFFFFF : 0x8A000000;
        mDisabledColor =  darkTheme ? 0x4DFFFFFF : 0x42000000;
        a.recycle();

        size = AndroidUtilities.dp(22);

        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(AndroidUtilities.dp(2));
            paint.setStyle(Paint.Style.STROKE);
            checkedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
            eraser.setColor(0);
            eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        try {
            bitmap = Bitmap.createBitmap(AndroidUtilities.dp(size), AndroidUtilities.dp(size), Bitmap.Config.ARGB_4444);

            if (ImageLoader.getInstance().runtimeHack != null) {
                ImageLoader.getInstance().runtimeHack.trackFree(bitmap.getRowBytes() * bitmap.getHeight());
            }

            bitmapCanvas = new Canvas(bitmap);
        } catch (Throwable e) {
            Logger.e("message", e);
        }
    }

    public void setProgress(float value) {
        if (progress == value) {
            return;
        }

        progress = value;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setSize(int value) {
        if (size == value) {
            return;
        }

        size = value;
    }

    public void setColor(int color1, int color2) {
        invalidate();
    }

    private void cancelCheckAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState) {
        mAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;

        if (bitmap != null && ImageLoader.getInstance().runtimeHack != null) {
            ImageLoader.getInstance().runtimeHack.trackAlloc(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) {
            return;
        }

        isChecked = checked;

        if (attachedToWindow && animated) {
            animateToCheckedState(checked);
        } else {
            cancelCheckAnimator();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null || bitmap.getWidth() != getMeasuredWidth()) {
            if (bitmap != null) {
                if (ImageLoader.getInstance().runtimeHack != null) {
                    ImageLoader.getInstance().runtimeHack.trackAlloc(bitmap.getRowBytes() * bitmap.getHeight());
                }

                bitmap.recycle();
            }
            try {
                bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                if (ImageLoader.getInstance().runtimeHack != null) {
                    ImageLoader.getInstance().runtimeHack.trackFree(bitmap.getRowBytes() * bitmap.getHeight());
                }

                bitmapCanvas = new Canvas(bitmap);
            } catch (Throwable e) {
                Logger.e("message", e);
            }
        }

        float circleProgress;

        if (progress <= 0.5f) {
            paint.setColor(mBorderColor);
            checkedPaint.setColor(mBorderColor);
            circleProgress = progress / 0.5f;
        } else {
            circleProgress = 2.0f - progress / 0.5f;
            int r1 = Color.red(mBorderColor);
            int rD = (int) ((Color.red(mAccentColor) - r1) * (1.0f - circleProgress));
            int g1 = Color.green(mBorderColor);
            int gD = (int) ((Color.green(mAccentColor) - g1) * (1.0f - circleProgress));
            int b1 = Color.blue(mBorderColor);
            int bD = (int) ((Color.blue(mAccentColor) - b1) * (1.0f - circleProgress));
            int c = Color.rgb(r1 + rD, g1 + gD, b1 + bD);
            paint.setColor(c);
            checkedPaint.setColor(c);
        }

        if (bitmap != null) {
            bitmap.eraseColor(0);
            float rad = size / 2 - (1 + circleProgress) * AndroidUtilities.getDensity();
            bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad, paint);

            if (progress <= 0.5f) {
                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, (rad - AndroidUtilities.dp(1)), checkedPaint);
                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, (rad - AndroidUtilities.dp(1)) * (1.0f - circleProgress), eraser);
            } else {
                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, size / 4 + (rad - AndroidUtilities.dp(1) - size / 4) * circleProgress, checkedPaint);
            }

            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    public static class ImageLoader {

        public VMRuntimeHack runtimeHack = null;

        public class VMRuntimeHack {
            private Object runtime = null;
            private Method trackAllocation = null;
            private Method trackFree = null;

            public boolean trackAlloc(long size) {
                if (runtime == null) {
                    return false;
                }

                try {
                    Object res = trackAllocation.invoke(runtime, size);
                    return (res instanceof Boolean) ? (Boolean) res : true;
                } catch (Exception e) {
                    Logger.e("message", e);
                    return false;
                }
            }

            public boolean trackFree(long size) {
                if (runtime == null) {
                    return false;
                }

                try {
                    Object res = trackFree.invoke(runtime, size);
                    return (res instanceof Boolean) ? (Boolean) res : true;
                } catch (Exception e) {
                    Logger.e("message", e);
                    return false;
                }
            }
        }

        private static volatile ImageLoader Instance = null;

        public static ImageLoader getInstance() {
            ImageLoader localInstance = Instance;

            if (localInstance == null) {
                synchronized (ImageLoader.class) {
                    localInstance = Instance;
                    if (localInstance == null) {
                        Instance = localInstance = new ImageLoader();
                    }
                }
            }
            return localInstance;
        }
    }
}