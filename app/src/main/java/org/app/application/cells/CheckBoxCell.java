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
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.CheckBox;
import org.app.material.widget.LayoutHelper;

public class CheckBoxCell extends FrameLayout {

    private TextView mTextView;
    private CheckBox mCheckBox;

    private static Paint paint;
    private boolean needDivider;

    public CheckBoxCell(Context context) {
        super(context);

        AndroidUtilities.bind(context);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(0xffd9d9d9);
            paint.setStrokeWidth(1);
        }

        this.setBackgroundColor(0xFFFFFFFF);

        mTextView = new TextView(context);
        mTextView.setTextColor(0xFF000000);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mTextView);

        mCheckBox = new CheckBox(context);
        addView(mCheckBox, LayoutHelper.makeFrame(context, 18, 18, (Gravity.END | Gravity.CENTER_VERTICAL), 16, 0, 16, 0));
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(getContext(), 54 + (needDivider ? 1 : 0)), MeasureSpec.EXACTLY));
    }

    public CheckBoxCell setText(@StringRes int resId) {
        mTextView.setText(getResources().getString(resId));
        return this;
    }

    public CheckBoxCell setChecked(boolean checked, boolean animated) {
        mCheckBox.setChecked(checked, animated);
        return this;
    }

    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    public CheckBoxCell setDivider(boolean divider) {
        needDivider = divider;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
        }
    }
}
