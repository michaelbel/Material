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

    private static Paint eraser;
    private static Paint checkPaint;
    private static Paint backgroundPaint;
    private static RectF rectF;

    private Bitmap drawBitmap;
    private Canvas drawCanvas;

    private float progress;
    private ObjectAnimator checkAnimator;

    private boolean attachedToWindow;
    private boolean isChecked;
    private boolean isDisabled;

    private int checkColor = 0xFFFFFFFF;
    private int colorAccent = 0xFFFF5252;

    public CheckBox(Context context) {
        super(context);
        if (checkPaint == null) {
            checkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            checkPaint.setColor(checkColor);
            checkPaint.setStyle(Paint.Style.STROKE);
            checkPaint.setStrokeWidth(AndroidUtilities.dp(context, 2));
            eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
            eraser.setColor(0);
            eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rectF = new RectF();
        }

        drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(context, 18), AndroidUtilities.dp(context, 18), Bitmap.Config.ARGB_4444);
        drawCanvas = new Canvas(drawBitmap);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && drawBitmap == null) {

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

    public void setColor(int value) {
        colorAccent = value;
    }

    private void cancelCheckAnimator() {
        if (checkAnimator != null) {
            checkAnimator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState) {
        checkAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        checkAnimator.setDuration(300);
        checkAnimator.start();
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
        if (attachedToWindow && animated) {
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
        if (progress <= 0.5f) {
            bounceProgress = checkProgress = progress / 0.5f;
            int rD = (int) ((Color.red(colorAccent) - 0x73) * checkProgress);
            int gD = (int) ((Color.green(colorAccent) - 0x73) * checkProgress);
            int bD = (int) ((Color.blue(colorAccent) - 0x73) * checkProgress);
            int c = Color.rgb(0x73 + rD, 0x73 + gD, 0x73 + bD);
            backgroundPaint.setColor(c);
        } else {
            bounceProgress = 2.0f - progress / 0.5f;
            checkProgress = 1.0f;
            backgroundPaint.setColor(colorAccent);
        }
        if (isDisabled) {
            backgroundPaint.setColor(0xffb0b0b0);
        }
        float bounce = AndroidUtilities.dp(getContext(), 1) * bounceProgress;
        rectF.set(bounce, bounce, AndroidUtilities.dp(getContext(), 18) - bounce, AndroidUtilities.dp(getContext(), 18) - bounce);

        drawBitmap.eraseColor(0);
        drawCanvas.drawRoundRect(rectF, AndroidUtilities.dp(getContext(), 2), AndroidUtilities.dp(getContext(), 2), backgroundPaint);

        if (checkProgress != 1) {
            float rad = Math.min(AndroidUtilities.dp(getContext(), 7), AndroidUtilities.dp(getContext(), 7) * checkProgress + bounce);
            rectF.set(AndroidUtilities.dp(getContext(), 2) + rad, AndroidUtilities.dp(getContext(), 2) + rad, AndroidUtilities.dp(getContext(), 16) - rad, AndroidUtilities.dp(getContext(), 16) - rad);
            drawCanvas.drawRect(rectF, eraser);
        }

        if (progress > 0.5f) {
            int endX = (int) (AndroidUtilities.dp(getContext(), 7.5f) - AndroidUtilities.dp(getContext(), 5) * (1.0f - bounceProgress));
            int endY = (int) (AndroidUtilities.dpf2(getContext(), 13.5f) - AndroidUtilities.dp(getContext(), 5) * (1.0f - bounceProgress));
            drawCanvas.drawLine(AndroidUtilities.dp(getContext(), 7.5f), (int) AndroidUtilities.dpf2(getContext(), 13.5f), endX, endY, checkPaint);
            endX = (int) (AndroidUtilities.dpf2(getContext(), 6.5f) + AndroidUtilities.dp(getContext(), 9) * (1.0f - bounceProgress));
            endY = (int) (AndroidUtilities.dpf2(getContext(), 13.5f) - AndroidUtilities.dp(getContext(), 9) * (1.0f - bounceProgress));
            drawCanvas.drawLine((int) AndroidUtilities.dpf2(getContext(), 6.5f), (int) AndroidUtilities.dpf2(getContext(), 13.5f), endX, endY, checkPaint);
        }
        canvas.drawBitmap(drawBitmap, 0, 0, null);
    }
}
