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
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class IconCell extends FrameLayout {

    private TextView mTextView;
    private ImageView mIconView;

    private View listShortDivider;

    public IconCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);

        this.setElevation(4);
        this.setLayoutParams(params);
        this.setBackground(AndroidUtilities.getRipple(0xFFFFFFFF, 0xFFE0E0E0));

        mIconView = new ImageView(context);
        mIconView.setVisibility(INVISIBLE);
        mIconView.setScaleType(ImageView.ScaleType.CENTER);
        mIconView.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        addView(mIconView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        mTextView = new TextView(context);
        mTextView.setVisibility(INVISIBLE);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mTextView.setTextColor(0xFF444444);
        mTextView.setGravity(Gravity.START);
        mTextView.setPadding(AndroidUtilities.dp(context, 72), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 72), AndroidUtilities.dp(context, 16));
        addView(mTextView, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        listShortDivider = new View(context);
        listShortDivider.setVisibility(INVISIBLE);
        listShortDivider.setBackgroundColor(0xFF444444);
        addView(listShortDivider, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, 0.3f, Gravity.BOTTOM, 70, 0, 0, 0));
    }

    public IconCell addTitle(String text) {
        mTextView.setVisibility(VISIBLE);
        mTextView.setText(text);
        return this;
    }

    public IconCell addTitle(@StringRes int resId) {
        mTextView.setVisibility(VISIBLE);
        mTextView.setText(getResources().getText(resId));
        return this;
    }

    public IconCell addIcon(int icon) {
        mIconView.setVisibility(VISIBLE);
        mIconView.setImageResource(icon);
        return this;
    }

    public IconCell addIcon(Drawable icon) {
        mIconView.setVisibility(VISIBLE);
        mIconView.setImageDrawable(icon);
        return this;
    }

    public IconCell addShortDivider() {
        listShortDivider.setVisibility(VISIBLE);
        return this;
    }

    public void setElevationCell(float elevation) {
        this.setElevation(elevation);
    }

    public void setRiipleEffect(int background, int rippleColor) {
        this.setBackground(AndroidUtilities.getRipple(background, rippleColor));
    }
}