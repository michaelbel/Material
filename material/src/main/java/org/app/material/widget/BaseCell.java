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

package org.app.material.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.widget.FrameLayout;

import org.app.material.AndroidUtilities;

public class BaseCell extends FrameLayout {

    private static Paint mPaint;
    private static int mDividerColor = 0xFFD9D9D9;
    private static boolean mNeedDivider = false;

    public BaseCell(Context context) {
        super(context);

        AndroidUtilities.bind(context);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(mDividerColor);
            mPaint.setStrokeWidth(1);
        }
    }

    public BaseCell setDivider(boolean divider) {
        mNeedDivider = divider;
        return this;
    }

    public BaseCell setDividerColor(@ColorInt int color) {
        mDividerColor = color;
        invalidate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mNeedDivider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, mPaint);
        }
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(hMeasureSpec + (mNeedDivider ? 1 : 0)), MeasureSpec.EXACTLY));
    }
}