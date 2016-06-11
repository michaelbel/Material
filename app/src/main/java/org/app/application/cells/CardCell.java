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

package org.app.application.cells;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class CardCell extends CardView {

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private ImageView mImageView;
    private ImageView mOptionButton;
    private FrameLayout layout;

    public OnCardClickListener mCardClickListener;
    public OnOptionClickListener mOptionClickListener;

    public interface OnOptionClickListener {
        void onClick();
    }

    public interface OnCardClickListener {
        void onClick();
    }

    public CardCell(Context context) {
        super(context);

        this.setCardBackgroundColor(0xFFFFFFFF);
        this.setRadius(AndroidUtilities.dp(context, 4F));
        this.setCardElevation(AndroidUtilities.dp(context, 2));
        this.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 6, 6, 6, 0));

        layout = new FrameLayout(context);
        layout.setClickable(true);
        layout.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCardClickListener != null) {
                    mCardClickListener.onClick();
                }
            }
        });
        addView(layout);

        mImageView = new ImageView(context);
        mImageView.setFocusable(false);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 10, 0, 10, 0));
        layout.addView(mImageView);

        mTextView1 = new TextView(context);
        mTextView1.setTextColor(0xFF000000);
        mTextView1.setGravity(Gravity.START | Gravity.TOP);
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mTextView1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mTextView1.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 100, 16, 21, 0));
        layout.addView(mTextView1);

        mTextView2 = new TextView(context);
        mTextView2.setTextColor(0xFF616161);
        mTextView2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTextView2.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 100, 0, 21, 0));
        layout.addView(mTextView2);

        mTextView3 = new TextView(context);
        mTextView3.setTextColor(0xFF9E9E9E);
        mTextView3.setGravity(Gravity.START | Gravity.BOTTOM);
        mTextView3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView3.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 100, 0, 21, 16));
        layout.addView(mTextView3);

        mOptionButton = new ImageView(context);
        mOptionButton.setClickable(true);
        mOptionButton.setFocusable(false);
        mOptionButton.setScaleType(ImageView.ScaleType.CENTER);
        mOptionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOptionClickListener != null) {
                    mOptionClickListener.onClick();
                }
            }
        });
        mOptionButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        mOptionButton.setImageDrawable(AndroidUtilities.getIcon(context, R.drawable.ic_dots_menu, 0xFF757575));
        mOptionButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 5, 5, 5, 5));
        layout.addView(mOptionButton);
    }

    public CardCell setText1(String text) {
        mTextView1.setText(text);
        return this;
    }

    public CardCell setText2 (String text) {
        mTextView2.setText(text);
        return this;
    }

    public CardCell setText3 (String text) {
        mTextView3.setText(text);
        return this;
    }

    public CardCell setImage(int image) {
        mImageView.setImageResource(image);
        return this;
    }

    public void setOnOptionsClick(OnOptionClickListener listener) {
        this.mOptionClickListener = listener;
    }

    public void setOnCardClick(OnCardClickListener listener) {
        this.mCardClickListener = listener;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(getContext(), 116), MeasureSpec.EXACTLY));
    }
}