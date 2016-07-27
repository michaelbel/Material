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
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.widget.LayoutHelper;

public class EmptyCell extends BaseCell {

    private TextView mHeadText;
    private boolean upperCase = false;

    public EmptyCell(Context context) {
        super(context);

        withHeight(42);

        mHeadText = new TextView(context);
        mHeadText.setGravity(Gravity.START);
        mHeadText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mHeadText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mHeadText.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mHeadText);
    }

    public EmptyCell withHeader(String text) {
        if (upperCase) {
            mHeadText.setText(text.toUpperCase());
        } else {
            mHeadText.setText(text);
        }

        return this;
    }

    public EmptyCell withHeader(@StringRes int resId) {
        withHeader(getContext().getString(resId));
        return this;
    }

    public EmptyCell withGravity(int gravity) {
        mHeadText.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, gravity | Gravity.CENTER_VERTICAL));
        return this;
    }

    public EmptyCell withTextToUpperCase(boolean upper) {
        upperCase = upper;
        return this;
    }
}