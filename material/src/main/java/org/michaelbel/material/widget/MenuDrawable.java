package org.michaelbel.material.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;

import org.michaelbel.material.Utils;

public class MenuDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean reverseAngle = false;
    private long lastFrameTime;
    private boolean animationInProgress;
    private float finalRotation;
    private float currentRotation;
    private int currentAnimationTime;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();

    public MenuDrawable(Context context) {
        super();

        Utils.bind(context);

        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(Utils.dp(context, 2));
    }

    public void setRotation(float rotation, boolean animated) {
        lastFrameTime = 0;
        if (currentRotation == 1) {
            reverseAngle = true;
        } else if (currentRotation == 0) {
            reverseAngle = false;
        }
        lastFrameTime = 0;
        if (animated) {
            if (currentRotation < rotation) {
                currentAnimationTime = (int) (currentRotation * 300);
            } else {
                currentAnimationTime = (int) ((1.0f - currentRotation) * 300);
            }
            lastFrameTime = System.currentTimeMillis();
            finalRotation = rotation;
        } else {
            finalRotation = currentRotation = rotation;
        }
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (currentRotation != finalRotation) {
            if (lastFrameTime != 0) {
                long dt = System.currentTimeMillis() - lastFrameTime;

                currentAnimationTime += dt;
                if (currentAnimationTime >= 300) {
                    currentRotation = finalRotation;
                } else {
                    if (currentRotation < finalRotation) {
                        currentRotation = interpolator.getInterpolation(currentAnimationTime / 300.0f) * finalRotation;
                    } else {
                        currentRotation = 1.0f - interpolator.getInterpolation(currentAnimationTime / 300.0f);
                    }
                }
            }
            lastFrameTime = System.currentTimeMillis();
            invalidateSelf();
        }

        canvas.save();
        canvas.translate(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
        canvas.rotate(currentRotation * (reverseAngle ? -180 : 180));
        canvas.drawLine(-Utils.dp(9), 0, Utils.dp(9) - Utils.dp(3.0f) * currentRotation, 0, paint);
        float endYDiff = Utils.dp(5) * (1 - Math.abs(currentRotation)) - Utils.dp(0.5f) * Math.abs(currentRotation);
        float endXDiff = Utils.dp(9) - Utils.dp(2.5f) * Math.abs(currentRotation);
        float startYDiff = Utils.dp(5) + Utils.dp(2.0f) * Math.abs(currentRotation);
        float startXDiff = -Utils.dp(9) + Utils.dp(7.5f) * Math.abs(currentRotation);
        canvas.drawLine(startXDiff, -startYDiff, endXDiff, -endYDiff, paint);
        canvas.drawLine(startXDiff, startYDiff, endXDiff, endYDiff, paint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return Utils.dp(24);
    }

    @Override
    public int getIntrinsicHeight() {
        return Utils.dp(24);
    }
}
