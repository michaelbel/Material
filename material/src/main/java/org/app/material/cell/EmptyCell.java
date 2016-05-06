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
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class EmptyCell extends FrameLayout {

    private TextView headText;
    private TextView noteText;

    public EmptyCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        this.setLayoutParams(params);

        headText = new TextView(context);
        headText.setVisibility(INVISIBLE);
        headText.setGravity(Gravity.START);
        headText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        headText.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        headText.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12));
        addView(headText, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        noteText = new TextView(context);
        headText.setVisibility(INVISIBLE);
        noteText.setGravity(Gravity.START);
        noteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        noteText.setPadding(AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12));
        addView(noteText, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public EmptyCell addHead(String text) {
        noteText.setVisibility(INVISIBLE);
        headText.setVisibility(VISIBLE);
        headText.setText(text);
        return this;
    }

    public EmptyCell addHead(@StringRes int resId) {
        noteText.setVisibility(INVISIBLE);
        headText.setVisibility(VISIBLE);
        headText.setText(getContext().getResources().getText(resId));
        return this;
    }

    public EmptyCell addNote(String text) {
        headText.setVisibility(INVISIBLE);
        noteText.setVisibility(VISIBLE);
        noteText.setText(text);
        return this;
    }

    public EmptyCell addNote(@StringRes int resId) {
        headText.setVisibility(INVISIBLE);
        noteText.setVisibility(VISIBLE);
        noteText.setText(getContext().getResources().getText(resId));
        return this;
    }
}