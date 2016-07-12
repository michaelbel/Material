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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.app.material.AndroidUtilities;

public class ColorPickerShift extends View {

	public static final int VERTICAL = 1;
	public static final int HORIZONTAL = 0;
	public static int[] DEFAULT;

	static {
		DEFAULT = new int[] {
				Color.parseColor("#b8c847"),
				Color.parseColor("#67bb43"),
				Color.parseColor("#41b691"),
				Color.parseColor("#4182b6"),
				Color.parseColor("#4149b6"),
				Color.parseColor("#7641b6"),
				Color.parseColor("#b741a7"),
				Color.parseColor("#c54657"),
				Color.parseColor("#d1694a"),
				Color.parseColor("#d1904a"),
				Color.parseColor("#d1c54a")};
	}

	int[] colors;

	{
        if (isInEditMode()) {
            colors = DEFAULT;
        } else {
            colors = new int[1];
        }
    }

	private Paint mPaint;
	private Rect mRect = new Rect();
	boolean isColorSelected = false;
	private int mSelectedColor = colors[0];
	private int mCellSize;
	private int mOrientation = HORIZONTAL;
	private OnColorChangedListener onColorChanged;
	private boolean isClick = false;
	private int screenW;
	private int screenH;

	public ColorPickerShift(Context context) {
		super(context);

		AndroidUtilities.bind(context);

		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);
	}

	public ColorPickerShift(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);

		try {
			mOrientation = HORIZONTAL;

            if (!isInEditMode()) {
                final int colorsArrayResId = -1;

                if (colorsArrayResId > 0) {
                    final int[] colors = context.getResources().getIntArray(colorsArrayResId);
                    setColors(colors);
                }
            }

            final int selected = -1;

            if (selected != -1) {
                final int[] currentColors = getColors();
                final int currentColorsLength = currentColors != null ? currentColors.length : 0;

                if (selected < currentColorsLength) {
                    setSelectedColorPosition(selected);
                }
            }
		} finally {

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mOrientation == HORIZONTAL) {
			drawHorizontalPicker(canvas);
		} else {
			drawVerticalPicker(canvas);
		}

	}

	private void drawVerticalPicker(Canvas canvas) {
		mRect.left = 0;
		mRect.top = 0;
		mRect.right = canvas.getWidth();
		mRect.bottom = 0;

		int margin = Math.round(canvas.getWidth() * 0.08F);

		for (int color : colors) {
			mPaint.setColor(color);
			mRect.top = mRect.bottom;
			mRect.bottom += mCellSize;

			if (isColorSelected && color == mSelectedColor) {
				mRect.left = 0;
				mRect.right = canvas.getWidth();
			} else {
				mRect.left = margin;
				mRect.right = canvas.getWidth() - margin;
			}

			canvas.drawRect(mRect, mPaint);
		}
	}

	private void drawHorizontalPicker(Canvas canvas) {
		mRect.left = 0;
		mRect.top = 0;
		mRect.right = 0;
		mRect.bottom = canvas.getHeight();

		int margin = Math.round(canvas.getHeight() * 0.08f);

		for (int color : colors) {
			mPaint.setColor(color);
			mRect.left = mRect.right;
			mRect.right += mCellSize;

			if (isColorSelected && color == mSelectedColor) {
				mRect.top = 0;
				mRect.bottom = canvas.getHeight();
			} else {
				mRect.top = margin;
				mRect.bottom = canvas.getHeight() - margin;
			}

			canvas.drawRect(mRect, mPaint);
		}
	}

	private void onColorChanged(int color) {
		if (onColorChanged != null) {
			onColorChanged.onColorChanged(color);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionId = event.getAction();
		int newColor;

		switch (actionId) {
			case MotionEvent.ACTION_DOWN:
				isClick = true;
				break;
			case MotionEvent.ACTION_UP:
				newColor = getColorAtXY(event.getX(), event.getY());
				setSelectedColor(newColor);

				if (isClick) {
					performClick();
				}

				break;
			case MotionEvent.ACTION_MOVE:
				newColor = getColorAtXY(event.getX(), event.getY());
				setSelectedColor(newColor);
				break;
			case MotionEvent.ACTION_CANCEL:
				isClick = false;
				break;
			case MotionEvent.ACTION_OUTSIDE:
				isClick = false;
				break;
			default:
				break;
		}

		return true;
	}

	private int getColorAtXY(float x, float y) {
		if (mOrientation == HORIZONTAL) {
			int left;
			int right = 0;

			for (int color : colors) {
				left = right;
				right += mCellSize;

				if (left <= x && right >= x) {
					return color;
				}
			}
		} else {
			int top;
			int bottom = 0;

			for (int color : colors) {
				top = bottom;
				bottom += mCellSize;

				if (y >= top && y <= bottom) {
					return color;
				}
			}
		}

		return mSelectedColor;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.selectedColor = this.mSelectedColor;
		ss.isColorSelected = this.isColorSelected;
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		this.mSelectedColor = ss.selectedColor;
		this.isColorSelected = ss.isColorSelected;
	}

	public static class SavedState extends BaseSavedState {
		private int selectedColor;
		private boolean isColorSelected;

		private SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.selectedColor = in.readInt();
			this.isColorSelected = in.readInt() == 1;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(this.selectedColor);
			out.writeInt(this.isColorSelected ? 1 : 0);
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

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		screenW = w;
		screenH = h;
		recalcCellSize();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public int getColor() {
		return mSelectedColor;
	}

	public void setSelectedColor(int color) {
		if (!containsColor(colors, color)) {
			return;
		}

		if (!isColorSelected || mSelectedColor != color) {
			this.mSelectedColor = color;
			isColorSelected = true;
			invalidate();
			onColorChanged(color);
		}
	}

	public void setSelectedColorPosition(int position) {
		setSelectedColor(colors[position]);
	}

	public void setColors(int[] colors) {
		this.colors = colors;

		if (!containsColor(colors, mSelectedColor)) {
			mSelectedColor = colors[0];
		}

		recalcCellSize();
		invalidate();
	}

	private int recalcCellSize() {
		if (mOrientation == HORIZONTAL) {
			mCellSize = Math.round(screenW / (colors.length * 1f));
		} else {
			mCellSize = Math.round(screenH / (colors.length * 1f));
		}

		return mCellSize;
	}

	public int[] getColors() {
		return colors;
	}

	private boolean containsColor(int[] colors, int c) {
		for (int color : colors) {
			if (color == c) {
				return true;
			}
		}

		return false;
	}

	public void setOnColorChangedListener(OnColorChangedListener l) {
		this.onColorChanged = l;
	}

	public interface OnColorChangedListener {
		void onColorChanged(int c);
	}
}