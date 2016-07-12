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
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

public class BottomPickerLayout extends FrameLayout {

    private int mPickerColor;
    private int mPickerHeight;
    private float mPickerElevation;
    private TextView mNegativeButton;
    private TextView mPositiveButton;
    private OnClickListener positiveButtonListener;
    private OnClickListener negativeButtonListener;

    public interface OnClickListener {
        void onClick(View v);
    }

    public BottomPickerLayout(Context context) {
        super(context);

        AndroidUtilities.bind(context);

        mPickerHeight = 48;
        mPickerElevation = 8;
        mPickerColor = 0xFFFFFFFF;

        this.setElevation(AndroidUtilities.dp(mPickerElevation));
        this.setBackgroundColor(mPickerColor);

        mNegativeButton = new TextView(context);
        mNegativeButton.setClickable(true);
        mNegativeButton.setGravity(Gravity.CENTER);
        mNegativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mNegativeButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mNegativeButton.setTextColor(AndroidUtilities.getContextColor(R.attr.colorPrimary));
        mNegativeButton.setPadding(AndroidUtilities.dp(32), 0, AndroidUtilities.dp(32), 0);
        mNegativeButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        mNegativeButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mNegativeButton);

        mPositiveButton = new TextView(context);
        mPositiveButton.setClickable(true);
        mPositiveButton.setGravity(Gravity.CENTER);
        mPositiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mPositiveButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mPositiveButton.setTextColor(AndroidUtilities.getContextColor(R.attr.colorPrimary));
        mPositiveButton.setPadding(AndroidUtilities.dp(32), 0, AndroidUtilities.dp(32), 0);
        mPositiveButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        mPositiveButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));
        addView(mPositiveButton);
    }

    public BottomPickerLayout setNegativeButton(String text, OnClickListener listener) {
        this.negativeButtonListener = listener;

        mNegativeButton.setText(text.toUpperCase());
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeButtonListener != null) {
                    negativeButtonListener.onClick(v);
                }
            }
        });

        return this;
    }

    public BottomPickerLayout setNegativeButton(@StringRes int resId, OnClickListener listener) {
        setNegativeButton(getContext().getString(resId), listener);
        return this;
    }

    public BottomPickerLayout setPositiveButton(String text, OnClickListener listener) {
        this.positiveButtonListener = listener;

        mPositiveButton.setText(text.toUpperCase());
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(v);
                }
            }
        });

        return this;
    }

    public BottomPickerLayout setPositiveButton(@StringRes int resId, OnClickListener listener) {
        setPositiveButton(getContext().getString(resId), listener);
        return this;
    }

    public BottomPickerLayout setHeight(int height) {
        this.mPickerHeight = height;
        return this;
    }

    public BottomPickerLayout setElevation(int elevation) {
        this.mPickerElevation = elevation;
        return this;
    }

    public BottomPickerLayout setPickerColor(int color) {
        this.mPickerColor = color;
        return this;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(mPickerHeight), MeasureSpec.EXACTLY));
    }
}