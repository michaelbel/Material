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
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class CardCell extends CardView {

    private TextView textView;
    private TextView valueTextView;
    private ImageView valueImageView;
    private ImageView optionsButton;

    public CardCell(Context context) {
        super(context);

        this.setElevation(5);
        this.setRadius(AndroidUtilities.dp(context, 5));
        this.setPreventCornerOverlap(false);
        this.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10, 10, 10, 0));
        this.setBackgroundColor(0xFFFFFFFF);

        FrameLayout layout = new FrameLayout(context);
        layout.setClickable(true);
        layout.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        addView(layout);

        textView = new TextView(context);
        textView.setVisibility(INVISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xFF444444);
        textView.setGravity(Gravity.START);
        textView.setPadding(AndroidUtilities.dp(context, 72), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 72), AndroidUtilities.dp(context, 16));
        layout.addView(textView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        valueTextView = new TextView(context);
        valueTextView.setVisibility(INVISIBLE);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        valueTextView.setTextColor(0xFFBDBDBD);
        valueTextView.setGravity(Gravity.START);
        valueTextView.setPadding(AndroidUtilities.dp(context, 72), AndroidUtilities.dp(context, 48), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 16));
        layout.addView(valueTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START));

        valueImageView = new ImageView(context);
        valueImageView.setVisibility(INVISIBLE);
        valueImageView.setFocusable(false);
        valueImageView.setScaleType(ImageView.ScaleType.CENTER);
        valueImageView.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        layout.addView(valueImageView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        optionsButton = new ImageView(context);
        optionsButton.setVisibility(INVISIBLE);
        optionsButton.setFocusable(false);
        optionsButton.setClickable(true);
        optionsButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        optionsButton.setScaleType(ImageView.ScaleType.CENTER);
        optionsButton.setPadding(0, AndroidUtilities.dp(context, 10), AndroidUtilities.dp(context, 8), 0);
        layout.addView(optionsButton, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP));
    }

    public CardCell addTitle(String title) {
        textView.setVisibility(VISIBLE);
        textView.setText(title);
        return this;
    }

    public CardCell addValue(String value) {
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        return this;
    }

    public CardCell addImage(int image) {
        valueImageView.setVisibility(VISIBLE);
        valueImageView.setImageResource(image);
        return this;
    }

    public CardCell addOptionButton(Drawable optionButton) {
        optionsButton.setVisibility(VISIBLE);
        optionsButton.setImageDrawable(optionButton);
        return this;
    }

    public void setOnOptionsClick(OnClickListener listener) {
        optionsButton.setOnClickListener(listener);
    }
}
