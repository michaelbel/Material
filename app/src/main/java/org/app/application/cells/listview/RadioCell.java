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

package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.widget.LayoutHelper;
import org.app.material.widget.RadioButton;

public class RadioCell extends BaseCell {

    private TextView mTextView;
    private RadioButton mRadioButton;

    public RadioCell(Context context) {
        super(context);

        mRadioButton = new RadioButton(context);
        mRadioButton.setLayoutParams(LayoutHelper.makeFrame(context, 22, 22, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mRadioButton);

        mTextView = new TextView(context);
        mTextView.setTextColor(0xFF333333);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 54, 0, 0, 0));
        addView(mTextView);
    }

    @Override
    public void setBackgroundColor(int color) {
        color = 0xFFFFFFFF;
        super.setBackgroundColor(color);
    }

    public RadioCell withText(String text) {
        mTextView.setText(text);
        return this;
    }

    public RadioCell withText(@StringRes int stringId) {
        withText(getResources().getString(stringId));
        return this;
    }

    public RadioCell withChecked(boolean checked, boolean animated) {
        mRadioButton.setChecked(checked, animated);
        return this;
    }
}