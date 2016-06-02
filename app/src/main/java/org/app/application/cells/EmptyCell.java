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
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class EmptyCell extends FrameLayout {

    private TextView mHeadText;
    private TextView mNoteText;

    public EmptyCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        this.setLayoutParams(params);

        mHeadText = new TextView(context);
        mHeadText.setVisibility(INVISIBLE);
        mHeadText.setGravity(Gravity.START);
        mHeadText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mHeadText.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mHeadText.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12));
        addView(mHeadText, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        mNoteText = new TextView(context);
        mHeadText.setVisibility(INVISIBLE);
        mNoteText.setGravity(Gravity.START);
        mNoteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mNoteText.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12));
        addView(mNoteText, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public EmptyCell addHead(String text) {
        mNoteText.setVisibility(INVISIBLE);
        mHeadText.setVisibility(VISIBLE);
        mHeadText.setText(text);
        return this;
    }

    public EmptyCell addHead(@StringRes int resId) {
        mNoteText.setVisibility(INVISIBLE);
        mHeadText.setVisibility(VISIBLE);
        mHeadText.setText(getResources().getText(resId));
        return this;
    }

    public EmptyCell addNote(String text) {
        mHeadText.setVisibility(INVISIBLE);
        mNoteText.setVisibility(VISIBLE);
        mNoteText.setText(text);
        return this;
    }

    public EmptyCell addNote(@StringRes int resId) {
        mHeadText.setVisibility(INVISIBLE);
        mNoteText.setVisibility(VISIBLE);
        mNoteText.setText(getResources().getText(resId));
        return this;
    }
}