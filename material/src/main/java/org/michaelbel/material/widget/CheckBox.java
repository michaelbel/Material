package org.michaelbel.material.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.michaelbel.material.util.Utils;
import org.michaelbel.material.R;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CheckBox extends View {

    private static final int ANIMATION_DURATION = 300;

    private float progress;
    private static Paint mEraser;
    private static Paint mCheckPaint;
    private static Paint mBackgroundPaint;
    private static RectF mRectF;
    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private ObjectAnimator animator;
    private boolean attachedToWindow;
    private boolean isChecked;
    private boolean isDisabled;

    private int mAccentColor;
    private int mBorderColor;
    private int mCheckColor;
    private int mDisabledColor;
    private boolean darkTheme;

    public CheckBox(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckBox, defStyleAttr, 0);
        darkTheme = a.getBoolean(R.styleable.CheckBox_checkBox_darkTheme, false);
        mAccentColor = a.getColor(R.styleable.CheckBox_checkBox_accentColor, 0xFF009688);
        a.recycle();

        mCheckColor = darkTheme ? 0xFF424242 : 0xFFFFFFFF;
        mBorderColor = darkTheme ? 0xB3FFFFFF : 0x8A000000;
        mDisabledColor = darkTheme ? 0x4DFFFFFF : 0x42000000;

        if (mCheckPaint == null) {
            mCheckPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCheckPaint.setColor(mCheckColor);
            mCheckPaint.setStyle(Paint.Style.STROKE);
            mCheckPaint.setStrokeWidth(Utils.dp(context, 2));
            mEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEraser.setColor(0);
            mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectF = new RectF();
        }

        drawBitmap = Bitmap.createBitmap(Utils.dp(context, 18), Utils.dp(context, 18), Bitmap.Config.ARGB_4444);
        drawCanvas = new Canvas(drawBitmap);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
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

    private void cancelCheckAnimator() {
        if (animator != null) {
            animator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState) {
        animator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
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

    public void setChecked(boolean checked) {
        setChecked(checked, true);
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
            int rD = (int) ((Color.red(mAccentColor) - 0x73) * checkProgress);
            int gD = (int) ((Color.green(mAccentColor) - 0x73) * checkProgress);
            int bD = (int) ((Color.blue(mAccentColor) - 0x73) * checkProgress);
            int c = Color.rgb(0x73 + rD, 0x73 + gD, 0x73 + bD);
            mBackgroundPaint.setColor(c);
        } else {
            bounceProgress = 2.0f - progress / 0.5f;
            checkProgress = 1.0f;
            mBackgroundPaint.setColor(mAccentColor);
        }

        if (isDisabled) {
            mBackgroundPaint.setColor(mBorderColor);
        }

        float bounce = Utils.dp(getContext(), 1) * bounceProgress;
        mRectF.set(bounce, bounce, Utils.dp(getContext(), 18) - bounce, Utils.dp(getContext(), 18) - bounce);

        drawBitmap.eraseColor(0);
        drawCanvas.drawRoundRect(mRectF, Utils.dp(getContext(), 2), Utils.dp(getContext(), 2), mBackgroundPaint);

        if (checkProgress != 1) {
            float rad = Math.min(Utils.dp(getContext(), 7), Utils.dp(getContext(), 7) * checkProgress + bounce);
            mRectF.set(Utils.dp(getContext(), 2) + rad, Utils.dp(getContext(), 2) + rad, Utils.dp(getContext(), 16) - rad, Utils.dp(getContext(), 16) - rad);
            drawCanvas.drawRect(mRectF, mEraser);
        }

        if (progress > 0.5f) {
            int endX = (int) (Utils.dp(getContext(), 7.5f) - Utils.dp(getContext(), 5) * (1.0f - bounceProgress));
            int endY = (int) (Utils.dpf2(13.5f) - Utils.dp(getContext(), 5) * (1.0f - bounceProgress));
            drawCanvas.drawLine(Utils.dp(getContext(), 7.5f), (int) Utils.dpf2(13.5f), endX, endY, mCheckPaint);
            endX = (int) (Utils.dpf2(6.5f) + Utils.dp(getContext(), 9) * (1.0f - bounceProgress));
            endY = (int) (Utils.dpf2(13.5f) - Utils.dp(getContext(), 9) * (1.0f - bounceProgress));
            drawCanvas.drawLine((int) Utils.dpf2(6.5f), (int) Utils.dpf2(13.5f), endX, endY, mCheckPaint);
        }

        canvas.drawBitmap(drawBitmap, 0, 0, null);
    }
}