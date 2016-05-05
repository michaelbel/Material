/*
 * Copyright 2012 Lars Werkman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.app.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.app.material.AndroidUtilities;
import org.app.material.R;

public class ColorPickerHolo extends View {

	private static final String STATE_PARENT = "parent";
	private static final String STATE_ANGLE = "angle";
	private static final String STATE_OLD_COLOR = "color";
	private static final String STATE_SHOW_OLD_COLOR = "showColor";

	private int[] COLORS = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
	private int[] M_COLORS = new int[] { 0xFFF44336, 0xFF9C27B0, 0xFF2196F3, 0xFF00BCD4, 0xFF4CAF50, 0xFFFFEB3B, 0xFFF44336 };

	private boolean mUsedColors;

	private Paint mColorWheelPaint;
	private Paint mPointerHaloPaint;
	private Paint mPointerColor;

	private int mColorWheelThickness;
	private int mColorWheelRadius;
	private int mPreferredColorWheelRadius;
	private int mColorCenterRadius;
	private int mPreferredColorCenterRadius;
	private int mColorCenterHaloRadius;
	private int mPreferredColorCenterHaloRadius;
	private int mColorPointerRadius;
	private int mColorPointerHaloRadius;
	private RectF mColorWheelRectangle = new RectF();
	private RectF mCenterRectangle = new RectF();
	private boolean mUserIsMovingPointer = false;
	private int mColor;
	private int mCenterOldColor;
	private boolean mShowCenterOldColor;
	private int mCenterNewColor;
	private float mTranslationOffset;
	private float mSlopX;
	private float mSlopY;
	private float mAngle;
	private Paint mCenterOldPaint;
	private Paint mCenterNewPaint;
	private Paint mCenterHaloPaint;
	private float[] mHSV = new float[3];
	private SaturationValueBar mSVbar = null;
	private OpacityBar mOpacityBar = null;
	private SaturationBar mSaturationBar = null;
	private boolean mTouchAnywhereOnColorWheelEnabled = true;
	private ValueBar mValueBar = null;
	private OnColorChangedListener onColorChangedListener;
	private OnColorSelectedListener onColorSelectedListener;

	private int oldChangedListenerColor;
	private int oldSelectedListenerColor;

	public ColorPickerHolo(Context context) {
		super(context);
		init(null, 0);
	}

	public ColorPickerHolo(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public ColorPickerHolo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPickerHolo, defStyle, 0);
		mColorWheelThickness = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_wheel_thickness, AndroidUtilities.dp(getContext(), 8));
		mColorWheelRadius = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_wheel_radius, AndroidUtilities.dp(getContext(), 124));
		mPreferredColorWheelRadius = mColorWheelRadius;
		mColorCenterRadius = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_center_radius, AndroidUtilities.dp(getContext(), 54));
		mPreferredColorCenterRadius = mColorCenterRadius;
		mColorCenterHaloRadius = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_center_halo_radius, AndroidUtilities.dp(getContext(), 54));
		mPreferredColorCenterHaloRadius = mColorCenterHaloRadius;
		mColorPointerRadius = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_pointer_radius, AndroidUtilities.dp(getContext(), 15));
		mColorPointerHaloRadius = a.getDimensionPixelSize(R.styleable.ColorPickerHolo_color_pointer_halo_radius, AndroidUtilities.dp(getContext(), 14));
		mUsedColors = a.getBoolean(R.styleable.ColorPickerHolo_useMaterialColors, true);
		a.recycle();

		mAngle = (float) (-Math.PI / 2);

		Shader s = new SweepGradient(0, 0, mUsedColors ? M_COLORS : COLORS, null);

		mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mColorWheelPaint.setShader(s);
		mColorWheelPaint.setStyle(Paint.Style.STROKE);
		mColorWheelPaint.setStrokeWidth(mColorWheelThickness);

		mPointerHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointerHaloPaint.setColor(Color.BLACK);
		mPointerHaloPaint.setAlpha(0x50);

		mPointerColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointerColor.setColor(calculateColor(mAngle));

		mCenterNewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterNewPaint.setColor(calculateColor(mAngle));
		mCenterNewPaint.setStyle(Paint.Style.FILL);

		mCenterOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterOldPaint.setColor(calculateColor(mAngle));
		mCenterOldPaint.setStyle(Paint.Style.FILL);

		mCenterHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterHaloPaint.setColor(Color.BLACK);
		mCenterHaloPaint.setAlpha(0x00);

		mCenterNewColor = calculateColor(mAngle);
		mCenterOldColor = calculateColor(mAngle);
		mShowCenterOldColor = true;
	}

	public ColorPickerHolo useMaterialColors(boolean isColors) {
		mUsedColors = isColors;
		return this;
	}

	public ColorPickerHolo setNewCenterColor(int color) {
		mCenterNewColor = color;
		mCenterNewPaint.setColor(color);

		if (mCenterOldColor == 0) {
			mCenterOldColor = color;
			mCenterOldPaint.setColor(color);
		}

		if (onColorChangedListener != null && color != oldChangedListenerColor ) {
			onColorChangedListener.onColorChanged(color);
			oldChangedListenerColor  = color;
		}

		invalidate();
		return this;
	}

	public ColorPickerHolo setOldCenterColor(int color) {
		mCenterOldColor = color;
		mCenterOldPaint.setColor(color);
		invalidate();
		return this;
	}

	public ColorPickerHolo setShowOldCenterColor(boolean show) {
		mShowCenterOldColor = show;
		invalidate();
		return this;
	}

	public ColorPickerHolo addSVBar(SaturationValueBar bar) {
		mSVbar = bar;
		mSVbar.setColorPicker(this);
		mSVbar.setColor(mColor);
		return this;
	}

	public ColorPickerHolo addOpacityBar(OpacityBar bar) {
		mOpacityBar = bar;
		mOpacityBar.setColorPicker(this);
		mOpacityBar.setColor(mColor);
		return this;
	}

	public ColorPickerHolo addSaturationBar(SaturationBar bar) {
		mSaturationBar = bar;
		mSaturationBar.setColorPicker(this);
		mSaturationBar.setColor(mColor);
		return this;
	}

	public ColorPickerHolo addValueBar(ValueBar bar) {
		mValueBar = bar;
		mValueBar.setColorPicker(this);
		mValueBar.setColor(mColor);
		return this;
	}

	public ColorPickerHolo setColor(int color) {
		mAngle = colorToAngle(color);
		mPointerColor.setColor(calculateColor(mAngle));

		if (mOpacityBar != null) {
			mOpacityBar.setColor(mColor);
			mOpacityBar.setOpacity(Color.alpha(color));
		}

		if (mSVbar != null) {
			Color.colorToHSV(color, mHSV);
			mSVbar.setColor(mColor);

			if (mHSV[1] < mHSV[2]) {
				mSVbar.setSaturation(mHSV[1]);
			} else if(mHSV[1] > mHSV[2]){
				mSVbar.setValue(mHSV[2]);
			}
		}

		if (mSaturationBar != null) {
			Color.colorToHSV(color, mHSV);
			mSaturationBar.setColor(mColor);
			mSaturationBar.setSaturation(mHSV[1]);
		}

		if (mValueBar != null && mSaturationBar == null) {
			Color.colorToHSV(color, mHSV);
			mValueBar.setColor(mColor);
			mValueBar.setValue(mHSV[2]);
		} else if (mValueBar != null) {
			Color.colorToHSV(color, mHSV);
			mValueBar.setValue(mHSV[2]);
		}

		setNewCenterColor(color);
		return this;
	}

	public ColorPickerHolo changeOpacityBarColor(int color) {
		if (mOpacityBar != null) {
			mOpacityBar.setColor(color);
		}

		return this;
	}

	public ColorPickerHolo changeSaturationBarColor(int color) {
		if (mSaturationBar != null) {
			mSaturationBar.setColor(color);
		}

		return this;
	}

	public ColorPickerHolo changeValueBarColor(int color) {
		if (mValueBar != null) {
			mValueBar.setColor(color);
		}

		return this;
	}

	public interface OnColorChangedListener {
		void onColorChanged(int color);
	}

	public interface OnColorSelectedListener {
		void onColorSelected(int color);
	}

	public void setOnColorChangedListener(OnColorChangedListener listener) {
		this.onColorChangedListener = listener;
	}

	public OnColorChangedListener getOnColorChangedListener() {
		return this.onColorChangedListener;
	}

	public void setOnColorSelectedListener(OnColorSelectedListener listener) {
		this.onColorSelectedListener = listener;
	}

	public OnColorSelectedListener getOnColorSelectedListener() {
		return this.onColorSelectedListener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.translate(mTranslationOffset, mTranslationOffset);
		canvas.drawOval(mColorWheelRectangle, mColorWheelPaint);

		float[] pointerPosition = calculatePointerPosition(mAngle);

		canvas.drawCircle(pointerPosition[0], pointerPosition[1], mColorPointerHaloRadius, mPointerHaloPaint);
		canvas.drawCircle(pointerPosition[0], pointerPosition[1], mColorPointerRadius, mPointerColor);
		canvas.drawCircle(0, 0, mColorCenterHaloRadius, mCenterHaloPaint);
		
		if (mShowCenterOldColor) {
			canvas.drawArc(mCenterRectangle, 90, 180, true, mCenterOldPaint);
			canvas.drawArc(mCenterRectangle, 270, 180, true, mCenterNewPaint);
		} else {
			canvas.drawArc(mCenterRectangle, 0, 360, true, mCenterNewPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int intrinsicSize = 2 * (mPreferredColorWheelRadius + mColorPointerHaloRadius);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(intrinsicSize, widthSize);
		} else {
			width = intrinsicSize;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(intrinsicSize, heightSize);
		} else {
			height = intrinsicSize;
		}

		int min = Math.min(width, height);
		setMeasuredDimension(min, min);
		mTranslationOffset = min * 0.5f;

		mColorWheelRadius = min / 2 - mColorWheelThickness - mColorPointerHaloRadius;
		mColorWheelRectangle.set(-mColorWheelRadius, -mColorWheelRadius, mColorWheelRadius, mColorWheelRadius);

		mColorCenterRadius = (int) ((float) mPreferredColorCenterRadius * ((float) mColorWheelRadius / (float) mPreferredColorWheelRadius));
		mColorCenterHaloRadius = (int) ((float) mPreferredColorCenterHaloRadius * ((float) mColorWheelRadius / (float) mPreferredColorWheelRadius));
		mCenterRectangle.set(-mColorCenterRadius, -mColorCenterRadius, mColorCenterRadius, mColorCenterRadius);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);

		float x = event.getX() - mTranslationOffset;
		float y = event.getY() - mTranslationOffset;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				float[] pointerPosition = calculatePointerPosition(mAngle);
				if (x >= (pointerPosition[0] - mColorPointerHaloRadius)
						&& x <= (pointerPosition[0] + mColorPointerHaloRadius)
						&& y >= (pointerPosition[1] - mColorPointerHaloRadius)
						&& y <= (pointerPosition[1] + mColorPointerHaloRadius)) {
					mSlopX = x - pointerPosition[0];
					mSlopY = y - pointerPosition[1];
					mUserIsMovingPointer = true;
					invalidate();
				} else if (x >= -mColorCenterRadius && x <= mColorCenterRadius
						&& y >= -mColorCenterRadius && y <= mColorCenterRadius
						&& mShowCenterOldColor) {
					mCenterHaloPaint.setAlpha(0x50);
					setColor(getOldCenterColor());
					invalidate();
				} else if (Math.sqrt(x * x + y * y)  <= mColorWheelRadius + mColorPointerHaloRadius
						&& Math.sqrt(x * x + y * y) >= mColorWheelRadius - mColorPointerHaloRadius
						&& mTouchAnywhereOnColorWheelEnabled) {

					mUserIsMovingPointer = true;
					invalidate();
				} else {
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mUserIsMovingPointer) {
					mAngle = (float) Math.atan2(y - mSlopY, x - mSlopX);
					mPointerColor.setColor(calculateColor(mAngle));

					setNewCenterColor(mCenterNewColor = calculateColor(mAngle));

					if (mOpacityBar != null) {
						mOpacityBar.setColor(mColor);
					}

					if (mValueBar != null) {
						mValueBar.setColor(mColor);
					}

					if (mSaturationBar != null) {
						mSaturationBar.setColor(mColor);
					}

					if (mSVbar != null) {
						mSVbar.setColor(mColor);
					}

					invalidate();
				} else {
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
				break;
			case MotionEvent.ACTION_UP:
				mUserIsMovingPointer = false;
				mCenterHaloPaint.setAlpha(0x00);

				if (onColorSelectedListener != null && mCenterNewColor != oldSelectedListenerColor) {
					onColorSelectedListener.onColorSelected(mCenterNewColor);
					oldSelectedListenerColor = mCenterNewColor;
				}

				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
				if (onColorSelectedListener != null && mCenterNewColor != oldSelectedListenerColor) {
					onColorSelectedListener.onColorSelected(mCenterNewColor);
					oldSelectedListenerColor = mCenterNewColor;
				}

				break;
		}

		return true;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable(STATE_PARENT, superState);
		state.putFloat(STATE_ANGLE, mAngle);
		state.putInt(STATE_OLD_COLOR, mCenterOldColor);
		state.putBoolean(STATE_SHOW_OLD_COLOR, mShowCenterOldColor);

		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable(STATE_PARENT);
		super.onRestoreInstanceState(superState);

		mAngle = savedState.getFloat(STATE_ANGLE);
		setOldCenterColor(savedState.getInt(STATE_OLD_COLOR));
		mShowCenterOldColor = savedState.getBoolean(STATE_SHOW_OLD_COLOR);
		int currentColor = calculateColor(mAngle);
		mPointerColor.setColor(currentColor);
		setNewCenterColor(currentColor);
	}

	private int ave(int s, int d, float p) {
		return s + Math.round(p * (d - s));
	}

	private int calculateColor(float angle) {
		float unit = (float) (angle / (2 * Math.PI));
		int[] colors = mUsedColors ? M_COLORS : COLORS;

		if (unit < 0) {
			unit += 1;
		}

		if (unit <= 0) {
			mColor = colors[0];
			return colors[0];
		}

		if (unit >= 1) {
			mColor = colors[colors.length - 1];
			return colors[colors.length - 1];
		}

		float p = unit * (colors.length - 1);
		int i = (int) p;
		p -= i;

		int c0 = colors[i];
		int c1 = colors[i + 1];
		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0), Color.red(c1), p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0), Color.blue(c1), p);

		mColor = Color.argb(a, r, g, b);
		return Color.argb(a, r, g, b);
	}

	public int getColor() {
		return mCenterNewColor;
	}

	private float colorToAngle(int color) {
		float[] colors = new float[3];
		Color.colorToHSV(color, colors);
		
		return (float) Math.toRadians(-colors[0]);
	}

	private float[] calculatePointerPosition(float angle) {
		float x = (float) (mColorWheelRadius * Math.cos(angle));
		float y = (float) (mColorWheelRadius * Math.sin(angle));

		return new float[] { x, y };
	}

	public int getOldCenterColor() {
		return mCenterOldColor;
	}
	
	public boolean getShowOldCenterColor() {
		return mShowCenterOldColor;
	}

	public boolean hasOpacityBar(){
		return mOpacityBar != null;
	}

	public boolean hasValueBar(){
		return mValueBar != null;
	}

	public boolean hasSaturationBar(){
		return mSaturationBar != null;
	}

	public boolean hasSVBar(){
		return mSVbar != null;
	}

	public void setTouchAnywhereOnColorWheelEnabled(boolean TouchAnywhereOnColorWheelEnabled){
		mTouchAnywhereOnColorWheelEnabled = TouchAnywhereOnColorWheelEnabled;
	}

	public boolean getTouchAnywhereOnColorWheel(){
		return mTouchAnywhereOnColorWheelEnabled;
	}

	public boolean getUsedColors() {
		return mUsedColors;
	}
}