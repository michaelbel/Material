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

package org.app.material.cell;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class DoubleCell extends FrameLayout {

    private ImageView iconView;
    private TextView titleTextView;
    private TextView valueTextView;

    public DoubleCell(Context context) {
        super(context);

        iconView = new ImageView(context);
        iconView.setVisibility(INVISIBLE);
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        iconView.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        addView(iconView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        titleTextView = new TextView(context);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        titleTextView.setLines(1);
        titleTextView.setMaxLines(1);
        titleTextView.setVisibility(INVISIBLE);
        titleTextView.setSingleLine(true);
        titleTextView.setTextColor(0xFF000000);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        titleTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 10), 0, 0);
        addView(titleTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));

        valueTextView = new TextView(context);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        valueTextView.setLines(1);
        valueTextView.setVisibility(INVISIBLE);
        valueTextView.setMaxLines(1);
        valueTextView.setSingleLine(true);
        valueTextView.setGravity(Gravity.START);
        valueTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 35), 0, AndroidUtilities.dp(context, 10));
        addView(valueTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));
    }

    public DoubleCell addIcon(int icon) {
        iconView.setVisibility(VISIBLE);
        iconView.setImageResource(icon);
        titleTextView.setPadding(AndroidUtilities.dp(getContext(), 72), AndroidUtilities.dp(getContext(), 10), 0, 0);
        valueTextView.setPadding(AndroidUtilities.dp(getContext(), 72), AndroidUtilities.dp(getContext(), 35), 0, AndroidUtilities.dp(getContext(), 10));
        return this;
    }

    public DoubleCell addTitle(String title) {
        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(title);
        return this;
    }

    public DoubleCell addTitle(@StringRes int resId) {
        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(getContext().getResources().getText(resId));
        return this;
    }

    public DoubleCell addValue(String value) {
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        return this;
    }

    public DoubleCell addValue(@StringRes int resId) {
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(getContext().getResources().getText(resId));
        return this;
    }
}