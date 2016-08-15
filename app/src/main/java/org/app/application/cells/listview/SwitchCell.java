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
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Switch;

public class SwitchCell extends BaseCell {

    private Switch mSwitch;
    private TextView mTextView;

    public SwitchCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        mTextView = new TextView(context);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mTextView);

        mSwitch = new Switch(context);
        mSwitch.setFocusable(false);
        mSwitch.setClickable(false);
        mSwitch.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mSwitch);
    }

    public SwitchCell withText(String text) {
        mTextView.setText(text);
        return this;
    }

    public SwitchCell withText(@StringRes int stringId) {
        withText(getResources().getString(stringId));
        return this;
    }

    public SwitchCell withChecked(boolean checked) {
        mSwitch.setChecked(checked);
        return this;
    }
}