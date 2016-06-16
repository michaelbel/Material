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
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class EmptyCell extends FrameLayout {

    private TextView mHeadText;

    public EmptyCell(Context context) {
        super(context);

        AndroidUtilities.bind(context);

        mHeadText = new TextView(context);
        mHeadText.setGravity(Gravity.START);
        mHeadText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mHeadText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mHeadText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mHeadText.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        mHeadText.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(mHeadText);
    }

    public EmptyCell setHeader(String text) {
        mHeadText.setText(text);
        return this;
    }

    public EmptyCell setHeader(@StringRes int resId) {
        mHeadText.setText(getResources().getString(resId));
        return this;
    }

    public EmptyCell addNote(String text) {
        return this;
    }

    public EmptyCell addNote(@StringRes int resId) {
        return this;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44), MeasureSpec.EXACTLY));
    }
}