/*
 * Copyright 2015 Rey Pham.
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

package org.app.material;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Checkable;
import android.widget.CompoundButton;

public class Switch extends CompoundButton implements Checkable {

    private int thumbRadius = AndroidUtilities.dp(8);
    private int animDuration;
    private int trackSize = AndroidUtilities.dp(2);
    private int shadowOffset = AndroidUtilities.dp(1);
    private int shadowSize = AndroidUtilities.dp(1);
    private long startTime;
    private float memoX;
    private float startX;
    private float flingVelocity;
    private float thumbPosition;
    private boolean checked;
    private boolean running = false;
    private Path shadowPath;
    private Paint shadowPaint;
    private RectF drawRect;
    private RectF tempRect;
    private Path trackPath;
    private Paint paint;
    private Paint.Cap trackCap;
    private RippleManager rippleManager;
    private Interpolator interpolator = new DecelerateInterpolator();
    private OnCheckedChangeListener onCheckedChangeListener;

//--------------------------------------------------------------------------------------------------

    public static final long FRAME_DURATION = 1000 / 100; // 60
	private ColorStateList mTrackColors;
	private ColorStateList mThumbColors;
	private int gravity = Gravity.CENTER_VERTICAL;
	private float mStartPosition;
	private int[] mTempStates = new int[2];
    private static final int COLOR_SHADOW_START = 0x4C000000;
    private static final int COLOR_SHADOW_END = 0x00000000;
    private boolean mIsRtl = true;

    public Switch(Context context) {
        super(context);

        thumbRadius = AndroidUtilities.dp(10);
        animDuration = 150;
        trackSize = AndroidUtilities.dp(14);
        checked = false;
        trackCap = Paint.Cap.ROUND;

        init(context, null, 0, 0);
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged(Switch view, boolean checked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        onCheckedChangeListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public int getSuggestedMinimumWidth() {
        return thumbRadius * 4 + Math.max(shadowSize, getPaddingLeft()) + Math.max(shadowSize, getPaddingRight());
    }

    @Override
    public void toggle() {
        if (isEnabled()) {
            setChecked(!checked);
        }
    }

    @Override
    public int getSuggestedMinimumHeight() {
        return thumbRadius * 2 + Math.max(shadowSize - shadowOffset, getPaddingTop()) + Math.max(shadowSize + shadowOffset, getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        drawRect.left = Math.max(shadowSize, getPaddingLeft());
        drawRect.right = w - Math.max(shadowSize, getPaddingRight());

        int height = thumbRadius * 2;
        int align = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        switch (align) {
            case Gravity.TOP:
                drawRect.top = Math.max(shadowSize - shadowOffset, getPaddingTop());
                drawRect.bottom = drawRect.top + height;
                break;
            case Gravity.BOTTOM:
                drawRect.bottom = h - Math.max(shadowSize + shadowOffset, getPaddingBottom());
                drawRect.top = drawRect.bottom - height;
                break;
            default:
                drawRect.top = (h - height) / 2f;
                drawRect.bottom = drawRect.top + height;
                break;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;

            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(this, this.checked);
            }
        }

        float desPos = this.checked ? 1f : 0f;

        if (thumbPosition != desPos) {
            startAnimation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                widthSize = getSuggestedMinimumWidth();
                break;
            case MeasureSpec.AT_MOST:
                widthSize = Math.min(widthSize, getSuggestedMinimumWidth());
                break;
        }

        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                heightSize = getSuggestedMinimumHeight();
                break;
            case MeasureSpec.AT_MOST:
                heightSize = Math.min(heightSize, getSuggestedMinimumHeight());
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);

        getRippleManager().onTouchEvent(this, event);
        float x = event.getX();

        if (mIsRtl) {
            x = 2 * drawRect.centerX() - x;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                memoX = x;
                startX = memoX;
                startTime = SystemClock.uptimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                float velocity = (x - startX) / (SystemClock.uptimeMillis() - startTime) * 300; // 600

                if (Math.abs(velocity) >= flingVelocity) {
                    setChecked(velocity > 0);
                } else if ((!checked && thumbPosition < 0.1f) || (checked && thumbPosition > 0.9f)) {
                    toggle();
                } else {
                    setChecked(thumbPosition > 0.5f);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                setChecked(thumbPosition > 0.5f);
                break;
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float x = (drawRect.width() - thumbRadius * 2) * thumbPosition + drawRect.left + thumbRadius;

        if (mIsRtl) {
            x = 2 * drawRect.centerX() - x;
        }

        float y = drawRect.centerY();
        getTrackPath(x, y, thumbRadius);
        paint.setColor(ColorUtil.getMiddleColor(getTrackColor(false), getTrackColor(true), thumbPosition));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(trackPath, paint);

        if (shadowSize > 0) {
            int saveCount = canvas.save();
            canvas.translate(x, y + shadowOffset);
            canvas.drawPath(shadowPath, shadowPaint);
            canvas.restoreToCount(saveCount);
        }

        paint.setColor(ColorUtil.getMiddleColor(getThumbColor(false), getThumbColor(true), thumbPosition));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, thumbRadius, paint);
    }

    protected RippleManager getRippleManager(){
        if (rippleManager == null) {
            synchronized (RippleManager.class) {

                if (rippleManager == null) {
                    rippleManager = new RippleManager();
                }
            }
        }

        return rippleManager;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RippleManager.cancelRipple(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        RippleManager rippleManager = getRippleManager();

        if (l == rippleManager) {
            super.setOnClickListener(l);
        } else {
            rippleManager.setOnClickListener(l);
            setOnClickListener(rippleManager);
        }
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        boolean rtl = layoutDirection == LAYOUT_DIRECTION_RTL;

        if (mIsRtl != rtl) {
            mIsRtl = rtl;
            invalidate();
        }
    }

    private int getTrackColor(boolean checked){
        mTempStates[0] = isEnabled() ? android.R.attr.state_enabled : -android.R.attr.state_enabled;
        mTempStates[1] = checked ? android.R.attr.state_checked : -android.R.attr.state_checked;
        return mTrackColors.getColorForState(mTempStates, 0);
    }

    private int getThumbColor(boolean checked){
        mTempStates[0] = isEnabled() ? android.R.attr.state_enabled : -android.R.attr.state_enabled;
        mTempStates[1] = checked ? android.R.attr.state_checked : -android.R.attr.state_checked;
        return mThumbColors.getColorForState(mTempStates, 0);
    }

    private void buildShadow(){
        if (shadowSize <= 0) {
            return;
        }

        if (shadowPaint == null) {
            shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            shadowPaint.setStyle(Paint.Style.FILL);
            shadowPaint.setDither(true);
        }

        float startRatio = (float) thumbRadius / (thumbRadius + shadowSize + shadowOffset);
        shadowPaint.setShader(new RadialGradient(0, 0, thumbRadius + shadowSize,
                new int[]{COLOR_SHADOW_START, COLOR_SHADOW_START, COLOR_SHADOW_END},
                new float[] { 0f, startRatio, 1f }, Shader.TileMode.CLAMP));

        if (shadowPath == null){
            shadowPath = new Path();
            shadowPath.setFillType(Path.FillType.EVEN_ODD);
        } else {
            shadowPath.reset();
        }

        float radius = thumbRadius + shadowSize;
        tempRect.set(-radius, -radius, radius, radius);
        shadowPath.addOval(tempRect, Path.Direction.CW);
        radius = thumbRadius - 1;
        tempRect.set(-radius, -radius - shadowOffset, radius, radius - shadowOffset);
        shadowPath.addOval(tempRect, Path.Direction.CW);
    }

    private void getTrackPath(float x, float y, float radius){
        float halfStroke = trackSize / 2f;

        trackPath.reset();

        if (trackCap != Paint.Cap.ROUND) {
            tempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
            float angle = (float)(Math.asin(halfStroke / (radius - 1f)) / Math.PI * 180);

            if (x - radius > drawRect.left) {
                trackPath.moveTo(drawRect.left, y - halfStroke);
                trackPath.arcTo(tempRect, 180 + angle, -angle * 2);
                trackPath.lineTo(drawRect.left, y + halfStroke);
                trackPath.close();
            }

            if (x + radius < drawRect.right) {
                trackPath.moveTo(drawRect.right, y - halfStroke);
                trackPath.arcTo(tempRect, -angle, angle * 2);
                trackPath.lineTo(drawRect.right, y + halfStroke);
                trackPath.close();
            }
        } else {
            float angle = (float)(Math.asin(halfStroke / (radius - 1f)) / Math.PI * 180);

            if (x - radius > drawRect.left) {
                float angle2 = (float)(Math.acos(Math.max(0f, (drawRect.left + halfStroke - x + radius) / halfStroke)) / Math.PI * 180);
                tempRect.set(drawRect.left, y - halfStroke, drawRect.left + trackSize, y + halfStroke);
                trackPath.arcTo(tempRect, 180 - angle2, angle2 * 2);
                tempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
                trackPath.arcTo(tempRect, 180 + angle, -angle * 2);
                trackPath.close();
            }

            if (x + radius < drawRect.right) {
                float angle2 = (float)Math.acos(Math.max(0f, (x + radius - drawRect.right + halfStroke) / halfStroke));
                trackPath.moveTo((float) (drawRect.right - halfStroke + Math.cos(angle2) * halfStroke), (float) (y + Math.sin(angle2) * halfStroke));
                angle2 = (float)(angle2 / Math.PI * 180);
                tempRect.set(drawRect.right - trackSize, y - halfStroke, drawRect.right, y + halfStroke);
                trackPath.arcTo(tempRect, angle2, -angle2 * 2);
                tempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
                trackPath.arcTo(tempRect, -angle, angle * 2);
                trackPath.close();
            }
        }
    }

    private void resetAnimation(){
        startTime = SystemClock.uptimeMillis();
        mStartPosition = thumbPosition;
        animDuration = (int)(animDuration * (checked ? (1f - mStartPosition) : mStartPosition));
    }

    private void startAnimation() {
        if (getHandler() != null) {
            resetAnimation();
            running = true;
            getHandler().postAtTime(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
        } else {
            thumbPosition = checked ? 1f : 0f;
        }

        invalidate();
    }

    private void stopAnimation() {
        running = false;
        thumbPosition = checked ? 1f : 0f;

        if (getHandler() != null) {
            getHandler().removeCallbacks(mUpdater);
        }

        invalidate();
    }

    private final Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

    private void update(){
        long curTime = SystemClock.uptimeMillis();
        float progress = Math.min(1f, (float)(curTime - startTime) / animDuration);
        float value = interpolator.getInterpolation(progress);
        thumbPosition = checked ? (mStartPosition * (1 - value) + value) : (mStartPosition * (1 - value));

        if (progress == 1f) {
            stopAnimation();
        }

        if (running) {
            if (getHandler() != null) {
                getHandler().postAtTime(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            } else {
                stopAnimation();
            }
        }

        invalidate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    public static class SavedState extends BaseSavedState {

        boolean checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean)in.readValue(null);
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "Switch.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked + "}";
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

//-------------------------------------------------------------------------------------------------


	
	protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		drawRect = new RectF();
		tempRect = new RectF();
		trackPath = new Path();
        flingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
	}

    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        if(mTrackColors == null){
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };
            int[] colors = new int[]{
                    ColorUtil.getColor(ThemeUtil.colorControlNormal(context, 0xFF000000), 0.5f),
                    ColorUtil.getColor(ThemeUtil.colorControlActivated(context, 0xFF000000), 0.5f),
            };

            mTrackColors = new ColorStateList(states, colors);
        }

        if(mThumbColors == null){
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };
            int[] colors = new int[]{
                    0xFAFAFA,
                    ThemeUtil.colorControlActivated(context, 0xFF000000),
            };

            mThumbColors = new ColorStateList(states, colors);
        }

        paint.setStrokeCap(trackCap);
        buildShadow();
        invalidate();
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        Drawable background = getBackground();
        if(background instanceof RippleDrawableW && !(drawable instanceof RippleDrawableW))
            ((RippleDrawableW) background).setBackgroundDrawable(drawable);
        else
            super.setBackgroundDrawable(drawable);
    }

    public void setCheckedImmediately(boolean checked){
        if (this.checked != checked) {
            this.checked = checked;
            if(onCheckedChangeListener != null)
                onCheckedChangeListener.onCheckedChanged(this, this.checked);
        }
        thumbPosition = this.checked ? 1f : 0f;
        invalidate();
    }
}