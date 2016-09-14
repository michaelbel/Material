/*
 * Copyright (C) 2008 The Android Open Source Project
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

package org.michaelbel.material.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.michaelbel.material.R;
import org.michaelbel.material.util.Utils;

import java.util.Locale;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class NumberPicker extends LinearLayout {

    private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = SELECTOR_WHEEL_ITEM_COUNT / 2;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
    private static final int SNAP_SCROLL_DURATION = 300;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;

    private int mSelectionDividersDistance;
    private int mMinHeight;
    private int mMaxHeight;
    private int mMinWidth;
    private int mMaxWidth;
    private int mTextSize;
    private int mSelectorTextGapHeight;

    private int mMinValue;
    private int mMaxValue;
    private int mValue;
    private int selectorElementHeight;
    private int initialScrollOffset = Integer.MIN_VALUE;
    private int currentScrollOffset;
    private int previousScrollerY;
    private int touchSlop;
    private int minimumFlingVelocity;
    private int maximumFlingVelocity;
    private int solidColor;
    private int selectionDividerHeight;
    private int scrollState = OnScrollListener.SCROLL_STATE_IDLE;
    private int topSelectionDividerTop;
    private int bottomSelectionDividerBottom;
    private int lastHandledDownDpadKeyCode = -1;
    private long lastDownEventTime;
    private float lastDownEventY;
    private float lastDownOrMoveEventY;
    private boolean computeMaxWidth;
    private boolean wrapSelectorWheel;
    private boolean ingonreMoveEvents;
    private boolean incrementVirtualButtonPressed;
    private boolean decrementVirtualButtonPressed;
    private final int[] selectorIndices = new int[SELECTOR_WHEEL_ITEM_COUNT];
    private final SparseArray<String> selectorIndexToStringCache = new SparseArray<>();

    private TextView inputText;
    private Paint selectorWheelPaint;
    private Scroller flingScroller;
    private Scroller adjustScroller;
    private Formatter mFormatter = null;
    private Drawable virtualButtonPressedDrawable;
    private Drawable selectionDivider;
    private VelocityTracker velocityTracker;
    private PressedStateHelper pressedStateHelper;
    private ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand;
    private OnValueChangeListener mOnValueChangeListener;

    public interface OnScrollListener {
        int SCROLL_STATE_IDLE = 0;
        int SCROLL_STATE_TOUCH_SCROLL = 1;
        int SCROLL_STATE_FLING = 2;
    }

    public interface Formatter {
        String format(int value);
    }

    public NumberPicker(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        solidColor = 0;
        selectionDivider = new ColorDrawable(Utils.getAttrColor(getContext(), R.attr.colorPrimary));
        selectionDividerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT, getResources().getDisplayMetrics());
        mSelectionDividersDistance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE, getResources().getDisplayMetrics());
        mMinHeight = SIZE_UNSPECIFIED;
        mMaxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());

        if (mMinHeight != SIZE_UNSPECIFIED && mMaxHeight != SIZE_UNSPECIFIED && mMinHeight > mMaxHeight) {
            throw new IllegalArgumentException("mMinHeight > mMaxHeight");
        }

        mMinWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        mMaxWidth = SIZE_UNSPECIFIED;
        computeMaxWidth = true;
        virtualButtonPressedDrawable = new ColorDrawable(0x00);
        pressedStateHelper = new PressedStateHelper();
        setWillNotDraw(false);
        inputText = new TextView(getContext());
        addView(inputText);
        inputText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        inputText.setGravity(Gravity.CENTER);
        inputText.setSingleLine(true);
        inputText.setBackgroundResource(0);
        inputText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = configuration.getScaledTouchSlop();
        minimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;
        mTextSize = (int) inputText.getTextSize();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(mTextSize);
        paint.setTypeface(inputText.getTypeface());
        ColorStateList colors = inputText.getTextColors();
        int color = colors.getColorForState(ENABLED_STATE_SET, 0xffffffff);
        paint.setColor(color);
        selectorWheelPaint = paint;
        flingScroller = new Scroller(getContext(), null, true);
        adjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5f));
        updateInputTextView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int msrdWdth = getMeasuredWidth();
        final int msrdHght = getMeasuredHeight();
        final int inptTxtMsrdWdth = inputText.getMeasuredWidth();
        final int inptTxtMsrdHght = inputText.getMeasuredHeight();
        final int inptTxtLeft = (msrdWdth - inptTxtMsrdWdth) / 2;
        final int inptTxtTop = (msrdHght - inptTxtMsrdHght) / 2;
        final int inptTxtRight = inptTxtLeft + inptTxtMsrdWdth;
        final int inptTxtBottom = inptTxtTop + inptTxtMsrdHght;

        inputText.layout(inptTxtLeft, inptTxtTop, inptTxtRight, inptTxtBottom);

        if (changed) {
            initializeSelectorWheel();
            initializeFadingEdges();
            topSelectionDividerTop = (getHeight() - mSelectionDividersDistance) / 2 - selectionDividerHeight;
            bottomSelectionDividerBottom = topSelectionDividerTop + 2 * selectionDividerHeight + mSelectionDividersDistance;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int newWidthMeasureSpec = makeMeasureSpec(widthMeasureSpec, mMaxWidth);
        final int newHeightMeasureSpec = makeMeasureSpec(heightMeasureSpec, mMaxHeight);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
        final int widthSize = resolveSizeAndStateRespectingMinSize(mMinWidth, getMeasuredWidth(), widthMeasureSpec);
        final int heightSize = resolveSizeAndStateRespectingMinSize(mMinHeight, getMeasuredHeight(), heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    public void setSelectionDividerColor(int color) {
        selectionDivider = new ColorDrawable(color);
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);

        int amountToScroll = scroller.getFinalY() - scroller.getCurrY();
        int futureScrollOffset = (currentScrollOffset + amountToScroll) % selectorElementHeight;
        int overshootAdjustment = initialScrollOffset - futureScrollOffset;

        if (overshootAdjustment != 0) {
            if (Math.abs(overshootAdjustment) > selectorElementHeight / 2) {
                if (overshootAdjustment > 0) {
                    overshootAdjustment -= selectorElementHeight;
                } else {
                    overshootAdjustment += selectorElementHeight;
                }
            }

            amountToScroll += overshootAdjustment;
            scrollBy(0, amountToScroll);
            return true;
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                removeAllCallbacks();
                inputText.setVisibility(View.INVISIBLE);
                lastDownOrMoveEventY = lastDownEventY = event.getY();
                lastDownEventTime = event.getEventTime();
                ingonreMoveEvents = false;

                if (lastDownEventY < topSelectionDividerTop) {
                    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                        pressedStateHelper.buttonPressDelayed(PressedStateHelper.BUTTON_DECREMENT);
                    }
                } else if (lastDownEventY > bottomSelectionDividerBottom) {
                    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                        pressedStateHelper.buttonPressDelayed(PressedStateHelper.BUTTON_INCREMENT);
                    }
                }

                getParent().requestDisallowInterceptTouchEvent(true);

                if (!flingScroller.isFinished()) {
                    flingScroller.forceFinished(true);
                    adjustScroller.forceFinished(true);
                    onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                } else if (!adjustScroller.isFinished()) {
                    flingScroller.forceFinished(true);
                    adjustScroller.forceFinished(true);
                } else if (lastDownEventY < topSelectionDividerTop) {
                    postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
                } else if (lastDownEventY > bottomSelectionDividerBottom) {
                    postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (ingonreMoveEvents) {
                    break;
                }

                float currentMoveY = event.getY();

                if (scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    int deltaDownY = (int) Math.abs(currentMoveY - lastDownEventY);

                    if (deltaDownY > touchSlop) {
                        removeAllCallbacks();
                        onScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
                    }
                } else {
                    int deltaMoveY = (int) ((currentMoveY - lastDownOrMoveEventY));
                    scrollBy(0, deltaMoveY);
                    invalidate();
                }

                lastDownOrMoveEventY = currentMoveY;
                break;
            case MotionEvent.ACTION_UP:
                removeChangeCurrentByOneFromLongPress();
                pressedStateHelper.cancel();
                VelocityTracker velocityTracker = this.velocityTracker;
                velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();

                if (Math.abs(initialVelocity) > minimumFlingVelocity) {
                    fling(initialVelocity);
                    onScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
                } else {
                    int eventY = (int) event.getY();
                    int deltaMoveY = (int) Math.abs(eventY - lastDownEventY);
                    long deltaTime = event.getEventTime() - lastDownEventTime;

                    if (deltaMoveY <= touchSlop && deltaTime < ViewConfiguration.getTapTimeout()) {
                        int selectorIndexOffset = (eventY / selectorElementHeight) - SELECTOR_MIDDLE_ITEM_INDEX;

                        if (selectorIndexOffset > 0) {
                            changeValueByOne(true);
                            pressedStateHelper.buttonTapped(
                                    PressedStateHelper.BUTTON_INCREMENT);
                        } else if (selectorIndexOffset < 0) {
                            changeValueByOne(false);
                            pressedStateHelper.buttonTapped(
                                    PressedStateHelper.BUTTON_DECREMENT);
                        }
                    } else {
                        ensureScrollWheelAdjusted();
                    }

                    onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
                }

                this.velocityTracker.recycle();
                this.velocityTracker = null;
            break;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                removeAllCallbacks();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                removeAllCallbacks();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_UP:
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        if (wrapSelectorWheel || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                                ? getValue() < getMaxValue() : getValue() > getMinValue()) {
                            requestFocus();
                            lastHandledDownDpadKeyCode = keyCode;
                            removeAllCallbacks();

                            if (flingScroller.isFinished()) {
                                changeValueByOne(keyCode == KeyEvent.KEYCODE_DPAD_DOWN);
                            }

                            return true;
                        }
                        break;
                    case KeyEvent.ACTION_UP:
                        if (lastHandledDownDpadKeyCode == keyCode) {
                            lastHandledDownDpadKeyCode = -1;
                            return true;
                        }

                        break;
                }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                removeAllCallbacks();
                break;
        }

        return super.dispatchTrackballEvent(event);
    }

    @Override
    public void computeScroll() {
        Scroller scroller = flingScroller;

        if (scroller.isFinished()) {
            scroller = adjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }

        scroller.computeScrollOffset();
        int currentScrollerY = scroller.getCurrY();

        if (previousScrollerY == 0) {
            previousScrollerY = scroller.getStartY();
        }

        scrollBy(0, currentScrollerY - previousScrollerY);
        previousScrollerY = currentScrollerY;

        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            invalidate();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        inputText.setEnabled(enabled);
    }

    @Override
    public void scrollBy(int x, int y) {
        int[] selectorIndices = this.selectorIndices;

        if (!wrapSelectorWheel && y > 0 && selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX] <= mMinValue) {
            currentScrollOffset = initialScrollOffset;
            return;
        }

        if (!wrapSelectorWheel && y < 0 && selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX] >= mMaxValue) {
            currentScrollOffset = initialScrollOffset;
            return;
        }

        currentScrollOffset += y;

        while (currentScrollOffset - initialScrollOffset > mSelectorTextGapHeight) {
            currentScrollOffset -= selectorElementHeight;
            decrementSelectorIndices(selectorIndices);
            setValueInternal(selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX]);

            if (!wrapSelectorWheel && selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX] <= mMinValue) {
                currentScrollOffset = initialScrollOffset;
            }
        }

        while (currentScrollOffset - initialScrollOffset < -mSelectorTextGapHeight) {
            currentScrollOffset += selectorElementHeight;
            incrementSelectorIndices(selectorIndices);
            setValueInternal(selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX]);

            if (!wrapSelectorWheel && selectorIndices[SELECTOR_MIDDLE_ITEM_INDEX] >= mMaxValue) {
                currentScrollOffset = initialScrollOffset;
            }
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return currentScrollOffset;
    }

    @Override
    protected int computeVerticalScrollRange() {
        return (mMaxValue - mMinValue + 1) * selectorElementHeight;
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    @Override
    public int getSolidColor() {
        return solidColor;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllCallbacks();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = (getRight() - getLeft()) / 2;
        float y = currentScrollOffset;

        if (virtualButtonPressedDrawable != null && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (decrementVirtualButtonPressed) {
                virtualButtonPressedDrawable.setState(PRESSED_STATE_SET);
                virtualButtonPressedDrawable.setBounds(0, 0, getRight(), topSelectionDividerTop);
                virtualButtonPressedDrawable.draw(canvas);
            }

            if (incrementVirtualButtonPressed) {
                virtualButtonPressedDrawable.setState(PRESSED_STATE_SET);
                virtualButtonPressedDrawable.setBounds(0, bottomSelectionDividerBottom, getRight(), getBottom());
                virtualButtonPressedDrawable.draw(canvas);
            }
        }

        int[] selectorIndices = this.selectorIndices;

        for (int i = 0; i < selectorIndices.length; i++) {
            int selectorIndex = selectorIndices[i];
            String scrollSelectorValue = selectorIndexToStringCache.get(selectorIndex);

            if (i != SELECTOR_MIDDLE_ITEM_INDEX || inputText.getVisibility() != VISIBLE) {
                canvas.drawText(scrollSelectorValue, x, y, selectorWheelPaint);
            }

            y += selectorElementHeight;
        }

        if (selectionDivider != null) {
            int topOfTopDivider = topSelectionDividerTop;
            int bottomOfTopDivider = topOfTopDivider + selectionDividerHeight;
            selectionDivider.setBounds(0, topOfTopDivider, getRight(), bottomOfTopDivider);
            selectionDivider.draw(canvas);

            int bottomOfBottomDivider = bottomSelectionDividerBottom;
            int topOfBottomDivider = bottomOfBottomDivider - selectionDividerHeight;
            selectionDivider.setBounds(0, topOfBottomDivider, getRight(), bottomOfBottomDivider);
            selectionDivider.draw(canvas);
        }
    }

    public void setValue(int value) {
        setValueInternal(value);
    }

    private void tryComputeMaxWidth() {
        if (!computeMaxWidth) {
            return;
        }

        int maxTextWidth;
        float maxDigitWidth = 0;

        for (int i = 0; i <= 9; i++) {
            final float digitWidth = selectorWheelPaint.measureText(formatNumberWithLocale(i));

            if (digitWidth > maxDigitWidth) {
                maxDigitWidth = digitWidth;
            }
        }

        int numberOfDigits = 0;
        int current = mMaxValue;

        while (current > 0) {
            numberOfDigits++;
            current = current / 10;
        }

        maxTextWidth = (int) (numberOfDigits * maxDigitWidth);
        maxTextWidth += inputText.getPaddingLeft() + inputText.getPaddingRight();

        if (mMaxWidth != maxTextWidth) {
            if (maxTextWidth > mMinWidth) {
                mMaxWidth = maxTextWidth;
            } else {
                mMaxWidth = mMinWidth;
            }

            invalidate();
        }
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        final boolean wrappingAllowed = (mMaxValue - mMinValue) >= selectorIndices.length;

        if ((!wrapSelectorWheel || wrappingAllowed) && wrapSelectorWheel != this.wrapSelectorWheel) {
            this.wrapSelectorWheel = wrapSelectorWheel;
        }
    }

    public int getValue() {
        return mValue;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int mMinValue) {
        if (this.mMinValue == mMinValue) {
            return;
        }

        if (mMinValue < 0) {
            throw new IllegalArgumentException("mMinValue must be >= 0");
        }

        this.mMinValue = mMinValue;

        if (this.mMinValue > mValue) {
            mValue = this.mMinValue;
        }

        boolean wrapSelectorWheel = mMaxValue - this.mMinValue > selectorIndices.length;
        setWrapSelectorWheel(wrapSelectorWheel);
        initializeSelectorWheelIndices();
        updateInputTextView();
        tryComputeMaxWidth();
        invalidate();
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int mMaxValue) {
        if (this.mMaxValue == mMaxValue) {
            return;
        }

        if (mMaxValue < 0) {
            throw new IllegalArgumentException("mMaxValue must be >= 0");
        }

        this.mMaxValue = mMaxValue;

        if (this.mMaxValue < mValue) {
            mValue = this.mMaxValue;
        }

        boolean wrapSelectorWheel = this.mMaxValue - mMinValue > selectorIndices.length;
        setWrapSelectorWheel(wrapSelectorWheel);
        initializeSelectorWheelIndices();
        updateInputTextView();
        tryComputeMaxWidth();
        invalidate();
    }

    private int makeMeasureSpec(int measureSpec, int maxSize) {
        if (maxSize == SIZE_UNSPECIFIED) {
            return measureSpec;
        }

        final int size = MeasureSpec.getSize(measureSpec);
        final int mode = MeasureSpec.getMode(measureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                return measureSpec;
            case MeasureSpec.AT_MOST:
                return MeasureSpec.makeMeasureSpec(Math.min(size, maxSize), MeasureSpec.EXACTLY);
            case MeasureSpec.UNSPECIFIED:
                return MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.EXACTLY);
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
    }

    private int resolveSizeAndStateRespectingMinSize(int minSize, int measuredSize, int measureSpec) {
        if (minSize != SIZE_UNSPECIFIED) {
            final int desiredWidth = Math.max(minSize, measuredSize);
            return resolveSizeAndState(desiredWidth, measureSpec, 0);
        } else {
            return measuredSize;
        }
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | 16777216;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & (-16777216));
    }

    private void initializeSelectorWheelIndices() {
        selectorIndexToStringCache.clear();
        int[] selectorIndices = this.selectorIndices;
        int current = getValue();

        for (int i = 0; i < this.selectorIndices.length; i++) {
            int selectorIndex = current + (i - SELECTOR_MIDDLE_ITEM_INDEX);

            if (wrapSelectorWheel) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex);
            }

            selectorIndices[i] = selectorIndex;
            ensureCachedScrollSelectorValue(selectorIndices[i]);
        }
    }

    private void setValueInternal(int current) {
        if (mValue == current) {
            return;
        }

        if (wrapSelectorWheel) {
            current = getWrappedSelectorIndex(current);
        } else {
            current = Math.max(current, mMinValue);
            current = Math.min(current, mMaxValue);
        }

        mValue = current;
        updateInputTextView();
        initializeSelectorWheelIndices();
        invalidate();
    }

    private void changeValueByOne(boolean increment) {
        inputText.setVisibility(View.INVISIBLE);

        if (!moveToFinalScrollerPosition(flingScroller)) {
            moveToFinalScrollerPosition(adjustScroller);
        }

        previousScrollerY = 0;

        if (increment) {
            flingScroller.startScroll(0, 0, 0, -selectorElementHeight, SNAP_SCROLL_DURATION);
        } else {
            flingScroller.startScroll(0, 0, 0, selectorElementHeight, SNAP_SCROLL_DURATION);
        }

        invalidate();
    }

    private void initializeSelectorWheel() {
        initializeSelectorWheelIndices();
        int[] selectorIndices = this.selectorIndices;
        int totalTextHeight = selectorIndices.length * mTextSize;
        float totalTextGapHeight = (getBottom() - getTop()) - totalTextHeight;
        float textGapCount = selectorIndices.length;
        mSelectorTextGapHeight = (int) (totalTextGapHeight / textGapCount + 0.5f);
        selectorElementHeight = mTextSize + mSelectorTextGapHeight;
        int editTextTextPosition = inputText.getBaseline() + inputText.getTop();
        initialScrollOffset = editTextTextPosition - (selectorElementHeight * SELECTOR_MIDDLE_ITEM_INDEX);
        currentScrollOffset = initialScrollOffset;
        updateInputTextView();
    }

    private void initializeFadingEdges() {
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength((getBottom() - getTop() - mTextSize) / 2);
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == flingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }

            onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
        } else {
            if (scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                updateInputTextView();
            }
        }
    }

    private void onScrollStateChange(int scrollState) {
        if (this.scrollState == scrollState) {
            return;
        }

        this.scrollState = scrollState;
    }

    private void fling(int velocityY) {
        previousScrollerY = 0;

        if (velocityY > 0) {
            flingScroller.fling(0, 0, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        } else {
            flingScroller.fling(0, Integer.MAX_VALUE, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        }

        invalidate();
    }

    private int getWrappedSelectorIndex(int selectorIndex) {
        if (selectorIndex > mMaxValue) {
            return mMinValue + (selectorIndex - mMaxValue) % (mMaxValue - mMinValue) - 1;
        } else if (selectorIndex < mMinValue) {
            return mMaxValue - (mMinValue - selectorIndex) % (mMaxValue - mMinValue) + 1;
        }
        return selectorIndex;
    }

    private void incrementSelectorIndices(int[] selectorIndices) {
        System.arraycopy(selectorIndices, 1, selectorIndices, 0, selectorIndices.length - 1);
        int nextScrollSelectorIndex = selectorIndices[selectorIndices.length - 2] + 1;

        if (wrapSelectorWheel && nextScrollSelectorIndex > mMaxValue) {
            nextScrollSelectorIndex = mMinValue;
        }

        selectorIndices[selectorIndices.length - 1] = nextScrollSelectorIndex;
        ensureCachedScrollSelectorValue(nextScrollSelectorIndex);
    }

    private void decrementSelectorIndices(int[] selectorIndices) {
        System.arraycopy(selectorIndices, 0, selectorIndices, 1, selectorIndices.length - 1);
        int nextScrollSelectorIndex = selectorIndices[1] - 1;

        if (wrapSelectorWheel && nextScrollSelectorIndex < mMinValue) {
            nextScrollSelectorIndex = mMaxValue;
        }

        selectorIndices[0] = nextScrollSelectorIndex;
        ensureCachedScrollSelectorValue(nextScrollSelectorIndex);
    }

    private void ensureCachedScrollSelectorValue(int selectorIndex) {
        SparseArray<String> cache = selectorIndexToStringCache;
        String scrollSelectorValue = cache.get(selectorIndex);

        if (scrollSelectorValue != null) {
            return;
        }

        if (selectorIndex < mMinValue || selectorIndex > mMaxValue) {
            scrollSelectorValue = "";
        } else {
            scrollSelectorValue = formatNumber(selectorIndex);
        }

        cache.put(selectorIndex, scrollSelectorValue);
    }

    private String formatNumber(int value) {
        return (mFormatter != null) ? mFormatter.format(value) : formatNumberWithLocale(value);
    }

    private boolean updateInputTextView() {
        String text = formatNumber(mValue);

        if (!TextUtils.isEmpty(text) && !text.equals(inputText.getText().toString())) {
            inputText.setText(text);
            return true;
        }

        return false;
    }

    private void postChangeCurrentByOneFromLongPress(boolean increment, long delayMillis) {
        if (changeCurrentByOneFromLongPressCommand == null) {
            changeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }

        changeCurrentByOneFromLongPressCommand.setStep(increment);
        postDelayed(changeCurrentByOneFromLongPressCommand, delayMillis);
    }

    private void removeChangeCurrentByOneFromLongPress() {
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }
    }

    private void removeAllCallbacks() {
        if (changeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
        }

        pressedStateHelper.cancel();
    }

    private boolean ensureScrollWheelAdjusted() {
        int deltaY = initialScrollOffset - currentScrollOffset;

        if (deltaY != 0) {
            previousScrollerY = 0;

            if (Math.abs(deltaY) > selectorElementHeight / 2) {
                deltaY += (deltaY > 0) ? -selectorElementHeight : selectorElementHeight;
            }

            adjustScroller.startScroll(0, 0, 0, deltaY, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
            invalidate();
            return true;
        }
        return false;
    }

    public class PressedStateHelper implements Runnable {
        public static final int BUTTON_INCREMENT = 1;
        public static final int BUTTON_DECREMENT = 2;

        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;

        private int mManagedButton;
        private int mMode;

        public void cancel() {
            mMode = 0;
            mManagedButton = 0;
            NumberPicker.this.removeCallbacks(this);

            if (incrementVirtualButtonPressed) {
                incrementVirtualButtonPressed = false;
                invalidate(0, bottomSelectionDividerBottom, getRight(), getBottom());
            }

            decrementVirtualButtonPressed = false;
        }

        public void buttonPressDelayed(int button) {
            cancel();
            mMode = MODE_PRESS;
            mManagedButton = button;
            NumberPicker.this.postDelayed(this, ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int button) {
            cancel();
            mMode = MODE_TAPPED;
            mManagedButton = button;
            NumberPicker.this.post(this);
        }

        @Override
        public void run() {
            switch (mMode) {
                case MODE_PRESS: {
                    switch (mManagedButton) {
                        case BUTTON_INCREMENT: {
                            incrementVirtualButtonPressed = true;
                            invalidate(0, bottomSelectionDividerBottom, getRight(), getBottom());
                        }
                        break;
                        case BUTTON_DECREMENT: {
                            decrementVirtualButtonPressed = true;
                            invalidate(0, 0, getRight(), topSelectionDividerTop);
                        }
                    }
                }
                break;
                case MODE_TAPPED: {
                    switch (mManagedButton) {
                        case BUTTON_INCREMENT: {
                            if (!incrementVirtualButtonPressed) {
                                NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                            }
                            incrementVirtualButtonPressed ^= true;
                            invalidate(0, bottomSelectionDividerBottom, getRight(), getBottom());
                        }
                        break;
                        case BUTTON_DECREMENT: {
                            if (!decrementVirtualButtonPressed) {
                                NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
                            }
                            decrementVirtualButtonPressed ^= true;
                            invalidate(0, 0, getRight(), topSelectionDividerTop);
                        }
                    }
                }
                break;
            }
        }
    }

    public class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        private void setStep(boolean increment) {
            mIncrement = increment;
        }

        @Override
        public void run() {
            changeValueByOne(mIncrement);
            long mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
            postDelayed(this, mLongPressUpdateInterval);
        }
    }

    static private String formatNumberWithLocale(int value) {
        return String.format(Locale.getDefault(), "%d", value);
    }

    public void setFormatter(Formatter formatter) {
        if (formatter == mFormatter) {
            return;
        }
        mFormatter = formatter;
        initializeSelectorWheelIndices();
        updateInputTextView();
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        mOnValueChangeListener = onValueChangedListener;
    }

    public interface OnValueChangeListener {
        void onValueChange(NumberPicker picker, int oldVal, int newVal);
    }
}