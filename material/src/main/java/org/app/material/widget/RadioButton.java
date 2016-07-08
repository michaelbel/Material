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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import org.app.material.AndroidUtilities;

import java.lang.reflect.Method;

public class RadioButton extends View {

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private static Paint paint;
    private static Paint eraser;
    private static Paint checkedPaint;

    private int checkedColor = 0xFFFF5252;
    private int color = 0xFF737373;

    private float progress;
    private ObjectAnimator mAnimator;

    private boolean attachedToWindow;
    private boolean isChecked;
    private int size;

    public RadioButton(Context context) {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        AndroidUtilities.bind(context);

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
        } catch (Throwable ignored) {}
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
        color = color1;
        checkedColor = color2;
        invalidate();
    }

    private void cancelCheckAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState) {
        mAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        mAnimator.setDuration(200);
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
            } catch (Throwable ignored) {}
        }

        float circleProgress;

        if (progress <= 0.5f) {
            paint.setColor(color);
            checkedPaint.setColor(color);
            circleProgress = progress / 0.5f;
        } else {
            circleProgress = 2.0f - progress / 0.5f;
            int r1 = Color.red(color);
            int rD = (int) ((Color.red(checkedColor) - r1) * (1.0f - circleProgress));
            int g1 = Color.green(color);
            int gD = (int) ((Color.green(checkedColor) - g1) * (1.0f - circleProgress));
            int b1 = Color.blue(color);
            int bD = (int) ((Color.blue(checkedColor) - b1) * (1.0f - circleProgress));
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