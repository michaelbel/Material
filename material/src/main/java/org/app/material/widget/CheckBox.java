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
import android.graphics.RectF;
import android.view.View;

import org.app.material.AndroidUtilities;

public class CheckBox extends View {

    private static Paint mEraser;
    private static Paint mCheckPaint;
    private static Paint mBackgroundPaint;
    private static RectF mRectF;
    private Bitmap mDrawBitmap;
    private Canvas mDrawCanvas;
    private ObjectAnimator mAnimator;

    private float mProgress;
    private boolean mAttachedToWindow;
    private boolean isChecked;
    private boolean isDisabled;

    private int checkColor = 0xFFFFFFFF;
    private int borderColor = 0xFFB0B0B0;
    private int colorAccent = 0xFFFF5252;

    public CheckBox(Context context) {
        super(context);

        if (mCheckPaint == null) {
            mCheckPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCheckPaint.setColor(checkColor);
            mCheckPaint.setStyle(Paint.Style.STROKE);
            mCheckPaint.setStrokeWidth(AndroidUtilities.dp(context, 2));
            mEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEraser.setColor(0);
            mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectF = new RectF();
        }

        mDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(context, 18), AndroidUtilities.dp(context, 18), Bitmap.Config.ARGB_4444);
        mDrawCanvas = new Canvas(mDrawBitmap);
    }

    public void setProgress(float value) {
        if (mProgress == value) {
            return;
        }

        mProgress = value;
        invalidate();
    }

    public void setColor(int value) {
        colorAccent = value;
    }

    private void cancelCheckAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState) {
        mAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        mAnimator.setDuration(300);
        mAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) {
            return;
        }

        isChecked = checked;

        if (mAttachedToWindow && animated) {
            animateToCheckedState(checked);
        } else {
            cancelCheckAnimator();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
        invalidate();
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }

        float checkProgress;
        float bounceProgress;

        if (mProgress <= 0.5f) {
            bounceProgress = checkProgress = mProgress / 0.5f;
            int rD = (int) ((Color.red(colorAccent) - 0x73) * checkProgress);
            int gD = (int) ((Color.green(colorAccent) - 0x73) * checkProgress);
            int bD = (int) ((Color.blue(colorAccent) - 0x73) * checkProgress);
            int c = Color.rgb(0x73 + rD, 0x73 + gD, 0x73 + bD);
            mBackgroundPaint.setColor(c);
        } else {
            bounceProgress = 2.0f - mProgress / 0.5f;
            checkProgress = 1.0f;
            mBackgroundPaint.setColor(colorAccent);
        }

        if (isDisabled) {
            mBackgroundPaint.setColor(borderColor);
        }

        float bounce = AndroidUtilities.dp(getContext(), 1) * bounceProgress;
        mRectF.set(bounce, bounce, AndroidUtilities.dp(getContext(), 18) - bounce, AndroidUtilities.dp(getContext(), 18) - bounce);
        mDrawBitmap.eraseColor(0);
        mDrawCanvas.drawRoundRect(mRectF, AndroidUtilities.dp(getContext(), 2), AndroidUtilities.dp(getContext(), 2), mBackgroundPaint);

        if (checkProgress != 1) {
            float rad = Math.min(AndroidUtilities.dp(getContext(), 7), AndroidUtilities.dp(getContext(), 7) * checkProgress + bounce);
            mRectF.set(AndroidUtilities.dp(getContext(), 2) + rad, AndroidUtilities.dp(getContext(), 2) + rad, AndroidUtilities.dp(getContext(), 16) - rad, AndroidUtilities.dp(getContext(), 16) - rad);
            mDrawCanvas.drawRect(mRectF, mEraser);
        }

        if (mProgress > 0.5f) {
            int endX = (int) (AndroidUtilities.dp(getContext(), 7.5f) - AndroidUtilities.dp(getContext(), 5) * (1.0f - bounceProgress));
            int endY = (int) (AndroidUtilities.dpf2(getContext(), 13.5f) - AndroidUtilities.dp(getContext(), 5) * (1.0f - bounceProgress));
            mDrawCanvas.drawLine(AndroidUtilities.dp(getContext(), 7.5f), (int) AndroidUtilities.dpf2(getContext(), 13.5f), endX, endY, mCheckPaint);
            endX = (int) (AndroidUtilities.dpf2(getContext(), 6.5f) + AndroidUtilities.dp(getContext(), 9) * (1.0f - bounceProgress));
            endY = (int) (AndroidUtilities.dpf2(getContext(), 13.5f) - AndroidUtilities.dp(getContext(), 9) * (1.0f - bounceProgress));
            mDrawCanvas.drawLine((int) AndroidUtilities.dpf2(getContext(), 6.5f), (int) AndroidUtilities.dpf2(getContext(), 13.5f), endX, endY, mCheckPaint);
        }

        canvas.drawBitmap(mDrawBitmap, 0, 0, null);
    }
}