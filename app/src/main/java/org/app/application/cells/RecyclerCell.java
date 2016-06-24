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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.AvatarImageView;
import org.app.material.widget.LayoutHelper;

public class RecyclerCell extends FrameLayout {

    private TextView mTextView1;
    private TextView mTextView2;
    private AvatarImageView mImageView;
    private ImageView mOptionButton;
    private FrameLayout layout;

    private static Paint mPaint;
    private boolean needDivider = false;

    public OnRecyclerClickListener mItemClickListener;
    public OnOptionClickListener mItemOptionClickListener;

    public RecyclerCell(Context context) {
        super(context);

        AndroidUtilities.bind(context);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(0xffd9d9d9);
            mPaint.setStrokeWidth(1);
        }

        this.setBackgroundColor(0xFFFFFFFF);
        this.setElevation(AndroidUtilities.dp(0.5F));
        this.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 64));

        layout = new FrameLayout(context);
        layout.setClickable(true);
        layout.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick();
                }
            }
        });
        addView(layout);

        mImageView = new AvatarImageView(context);
        mImageView.setFocusable(false);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setShapeDrawable(AvatarImageView.CIRCLE);
        mImageView.setLayoutParams(LayoutHelper.makeFrame(context, 46, 46, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        layout.addView(mImageView);

        mTextView1 = new TextView(context);
        mTextView1.setTextColor(0xFF000000);
        mTextView1.setGravity(Gravity.START | Gravity.TOP);
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mTextView1.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 80, 10, 21, 0));
        layout.addView(mTextView1);

        mTextView2 = new TextView(context);
        mTextView2.setTextColor(0xFF616161);
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTextView2.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTextView2.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 80, 0, 21, 10));
        layout.addView(mTextView2);

        mOptionButton = new ImageView(context);
        mOptionButton.setClickable(true);
        mOptionButton.setFocusable(false);
        mOptionButton.setScaleType(ImageView.ScaleType.CENTER);
        mOptionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOptionClickListener != null) {
                    mItemOptionClickListener.onClick();
                }
            }
        });
        mOptionButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        mOptionButton.setImageDrawable(AndroidUtilities.getIcon(R.drawable.ic_dots_menu, 0xFF757575));
        mOptionButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 5, 5, 5, 5));
        layout.addView(mOptionButton);
    }

    public RecyclerCell setImage(int image) {
        mImageView.setImageResource(image);
        return this;
    }

    public RecyclerCell setText1(String text) {
        mTextView1.setText(text);
        return this;
    }

    public RecyclerCell setText2 (String text) {
        mTextView2.setText(text);
        return this;
    }

    public RecyclerCell setDivider(boolean divider) {
        needDivider = divider;
        return this;
    }

    public void setOnOptionsClick(OnOptionClickListener listener) {
        this.mItemOptionClickListener = listener;
    }

    public void setOnItemClick(OnRecyclerClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(getContext(), 64), MeasureSpec.EXACTLY));
    }

    public interface OnOptionClickListener {
        void onClick();
    }

    public interface OnRecyclerClickListener {
        void onClick();
    }
}