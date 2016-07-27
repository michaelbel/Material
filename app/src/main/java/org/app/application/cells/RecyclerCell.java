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
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
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
    private ImageView mOptionButton;
    private AvatarImageView mImageView;

    private static Paint mPaint;
    private boolean needDivider = false;

    private Rect rect = new Rect();

    public RecyclerCell(Context context) {
        super(context);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(0xFFD9D9D9);
        }

        mImageView = new AvatarImageView(context);
        mImageView.setFocusable(false);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setShapeDrawable(AvatarImageView.CIRCLE);
        mImageView.setLayoutParams(LayoutHelper.makeFrame(context, 48, 48, Gravity.START |
                Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mImageView);

        mTextView1 = new TextView(context);
        mTextView1.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mTextView1.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 80, 10, 21, 0));
        addView(mTextView1);

        mTextView2 = new TextView(context);
        mTextView2.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTextView2.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 80, 0, 21, 10));
        addView(mTextView2);

        mOptionButton = new ImageView(context);
        mOptionButton.setFocusable(false);
        mOptionButton.setScaleType(ImageView.ScaleType.CENTER);
        mOptionButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        mOptionButton.setImageDrawable(AndroidUtilities.getIcon(R.drawable.ic_dots_menu,
                ContextCompat.getColor(context, R.color.textColorSecondary)));
        mOptionButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 5, 5, 5, 5));
        addView(mOptionButton);
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

    public RecyclerCell withDivider(boolean divider) {
        needDivider = divider;
        return this;
    }

    public void setOnOptionsClick(OnClickListener listener) {
        mOptionButton.setOnClickListener(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(),
                    getHeight() - 1, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64) + (needDivider ? 1 : 0),
                        MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
            mOptionButton.getHitRect(rect);

            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() ==
                    MotionEvent.ACTION_MOVE) {
                getBackground().setHotspot(event.getX(), event.getY());
            }
        }

        return super.onTouchEvent(event);
    }
}