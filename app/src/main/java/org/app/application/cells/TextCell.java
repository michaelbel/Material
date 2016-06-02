/*
 * Copyright 2016 Michael Bel
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

package org.app.application.cells;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;
import org.app.material.widget.LayoutHelper;

public class TextCell extends FrameLayout {

    private TextView mTextView;
    private TextView mValueTextView;
    private ImageView mValueImageView;
    private RadioButton mRadioButton;
    private CheckBox mCheckBox;
    private Switch mSwitchCompat;

    public TextCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 0.3f);

        this.setElevation(4);
        this.setLayoutParams(params);
        this.setBackgroundColor(0xFFFFFFFF);

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        this.setBackgroundResource(backgroundResource);
        typedArray.recycle();

        mTextView = new TextView(context);
        mTextView.setVisibility(INVISIBLE);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mTextView.setTextColor(0xFF444444);
        mTextView.setGravity(Gravity.START);
        mTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16));
        addView(mTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        mValueTextView = new TextView(context);
        mValueTextView.setVisibility(INVISIBLE);
        mValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mValueTextView.setTextColor(0xFF757575);
        mValueTextView.setGravity(Gravity.END);
        mValueTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16));
        addView(mValueTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END));

        mValueImageView = new ImageView(context);
        mValueImageView.setVisibility(INVISIBLE);
        mValueImageView.setScaleType(ImageView.ScaleType.CENTER);
        mValueImageView.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        addView(mValueImageView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        mRadioButton = new RadioButton(context);
        mRadioButton.setClickable(false);
        mRadioButton.setFocusable(false);
        //mRadioButton.setFocusableInTouchMode(false);
        mRadioButton.setVisibility(INVISIBLE);
        mRadioButton.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        mRadioButton.setPadding(AndroidUtilities.dp(context, 6), 0, AndroidUtilities.dp(context, 6), 0);
        addView(mRadioButton, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        mCheckBox = new CheckBox(context);
        mCheckBox.setClickable(false);
        mCheckBox.setFocusable(false);
        //mCheckBox.setFocusableInTouchMode(false);
        mCheckBox.setVisibility(INVISIBLE);
        mCheckBox.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        mCheckBox.setPadding(AndroidUtilities.dp(context, 7), 0, AndroidUtilities.dp(context, 7), 0);
        addView(mCheckBox, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        mSwitchCompat = new Switch(context);
        mSwitchCompat.setClickable(false);
        mSwitchCompat.setFocusable(false);
        //mSwitchCompat.setFocusableInTouchMode(false);
        mSwitchCompat.setVisibility(INVISIBLE);
        mSwitchCompat.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        mSwitchCompat.setPadding(AndroidUtilities.dp(context, 12), 0, AndroidUtilities.dp(context, 12), 0);
        addView(mSwitchCompat, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));
    }

    public TextCell addTitle(String title) {
        mValueTextView.setVisibility(INVISIBLE);
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mTextView.setVisibility(VISIBLE);
        mTextView.setText(title);
        return this;
    }

    public TextCell addTitle(@StringRes int resId) {
        mValueTextView.setVisibility(INVISIBLE);
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mTextView.setVisibility(VISIBLE);
        mTextView.setText(getResources().getText(resId));
        return this;
    }

    public TextCell addValue(String value) {
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mValueTextView.setVisibility(VISIBLE);
        mValueTextView.setText(value);
        return this;
    }

    public TextCell addValue(@StringRes int resId) {
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mValueTextView.setVisibility(VISIBLE);
        mValueTextView.setText(getResources().getText(resId));
        return this;
    }

    public TextCell addImage(int image) {
        mValueTextView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mValueImageView.setVisibility(VISIBLE);
        mValueImageView.setImageResource(image);
        return this;
    }

    public TextCell addImage(Drawable image) {
        mValueTextView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mValueImageView.setVisibility(VISIBLE);
        mValueImageView.setImageDrawable(image);
        return this;
    }

    public TextCell addRadio(boolean checked) {
        mValueTextView.setVisibility(INVISIBLE);
        mValueImageView.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mRadioButton.setVisibility(VISIBLE);
        mRadioButton.setChecked(checked);
        return this;
    }

    public TextCell addCheckBox(boolean checked) {
        mValueTextView.setVisibility(INVISIBLE);
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mSwitchCompat.setVisibility(INVISIBLE);

        mCheckBox.setVisibility(VISIBLE);
        mCheckBox.setChecked(checked);
        return this;
    }

    public TextCell addSwitch(boolean checked) {
        mValueTextView.setVisibility(INVISIBLE);
        mValueImageView.setVisibility(INVISIBLE);
        mRadioButton.setVisibility(INVISIBLE);
        mCheckBox.setVisibility(INVISIBLE);

        mSwitchCompat.setVisibility(VISIBLE);
        mSwitchCompat.setChecked(checked);
        return this;
    }

    public void setElevationCell(float elevation) {
        this.setElevation(elevation);
    }

    public void setRiipleEffect(int background, int rippleColor) {
        this.setBackgroundResource(AndroidUtilities.selectableItemBackground(getContext()));
    }

    public void setBackgroundCell(int color) {
        this.setBackgroundColor(color);
    }
}