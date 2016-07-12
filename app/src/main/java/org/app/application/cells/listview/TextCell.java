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

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class TextCell extends BaseCell {

    private TextView mTextView;
    private TextView mValueView;

    public TextCell(Context context) {
        super(context);

        withBackgroundColor(0xFFFFFFFF);

        mTextView = new TextView(context);
        mTextView.setTextColor(0xFF333333);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mTextView);

        mValueView = new TextView(context);
        mValueView.setTextColor(0xFF757575);
        mValueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mValueView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mValueView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        mValueView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));
        addView(mValueView);
    }

    public TextCell withText(String text) {
        mTextView.setText(text);
        return this;
    }

    public TextCell withText(@StringRes int resId) {
        withText(getContext().getString(resId));
        return this;
    }

    public TextCell withValue(String value) {
        mValueView.setText(value);
        return this;
    }

    public TextCell withValue(@StringRes int resId) {
        withValue(getContext().getString(resId));
        return this;
    }
}