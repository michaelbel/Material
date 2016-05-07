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

    private ImageView mIconImageView;
    private TextView mTitleTextView;
    private TextView mValueTextView;

    public DoubleCell(Context context) {
        super(context);

        mIconImageView = new ImageView(context);
        mIconImageView.setVisibility(INVISIBLE);
        mIconImageView.setScaleType(ImageView.ScaleType.CENTER);
        mIconImageView.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        addView(mIconImageView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        mTitleTextView = new TextView(context);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTitleTextView.setLines(1);
        mTitleTextView.setMaxLines(1);
        mTitleTextView.setVisibility(INVISIBLE);
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setTextColor(0xFF000000);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTitleTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 10), 0, 0);
        addView(mTitleTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));

        mValueTextView = new TextView(context);
        mValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mValueTextView.setLines(1);
        mValueTextView.setVisibility(INVISIBLE);
        mValueTextView.setMaxLines(1);
        mValueTextView.setSingleLine(true);
        mValueTextView.setGravity(Gravity.START);
        mValueTextView.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 35), 0, AndroidUtilities.dp(context, 10));
        addView(mValueTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));
    }

    public DoubleCell addIcon(int icon) {
        mIconImageView.setVisibility(VISIBLE);
        mIconImageView.setImageResource(icon);
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 72), AndroidUtilities.dp(getContext(), 10), 0, 0);
        mValueTextView.setPadding(AndroidUtilities.dp(getContext(), 72), AndroidUtilities.dp(getContext(), 35), 0, AndroidUtilities.dp(getContext(), 10));
        return this;
    }

    public DoubleCell addTitle(String title) {
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(title);
        return this;
    }

    public DoubleCell addTitle(@StringRes int resId) {
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getResources().getText(resId));
        return this;
    }

    public DoubleCell addValue(String value) {
        mValueTextView.setVisibility(VISIBLE);
        mValueTextView.setText(value);
        return this;
    }

    public DoubleCell addValue(@StringRes int resId) {
        mValueTextView.setVisibility(VISIBLE);
        mValueTextView.setText(getResources().getText(resId));
        return this;
    }
}