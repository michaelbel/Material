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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.app.material.AndroidUtilities;

import java.util.ArrayList;

public class FrameLayoutFixed extends FrameLayout {

    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);

    public FrameLayoutFixed(Context context) {
        super(context);
    }

    public FrameLayoutFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutFixed(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int getMeasuredStateFixed(View view) {
        return (view.getMeasuredWidth() & 0xff000000) | ((view.getMeasuredHeight() >> 16) & (0xff000000 >> 16));
    }

    public static int resolveSizeAndStateFixed(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | 0x01000000;
                } else {
                    result = size;
                }

                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }

        return result | (childMeasuredState & 0xff000000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int count = getChildCount();

            final boolean measureMatchParentChildren =
                    MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                            MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
            mMatchParentChildren.clear();

            int maxHeight = 0;
            int maxWidth = 0;
            int childState = 0;

            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() != GONE) {
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                    maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                    childState |= getMeasuredStateFixed(child);

                    if (measureMatchParentChildren) {
                        if (lp.width == LayoutHelper.MATCH_PARENT || lp.height == LayoutHelper.MATCH_PARENT) {
                            mMatchParentChildren.add(child);
                        }
                    }
                }
            }

            maxWidth += getPaddingLeft() + getPaddingRight();
            maxHeight += getPaddingTop() + getPaddingBottom();

            maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
            maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

            final Drawable drawable = getForeground();

            if (drawable != null) {
                maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
                maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
            }

            setMeasuredDimension(resolveSizeAndStateFixed(maxWidth, widthMeasureSpec, childState),
                    resolveSizeAndStateFixed(maxHeight, heightMeasureSpec, childState << 16));

            count = mMatchParentChildren.size();

            if (count > 1) {
                for (int i = 0; i < count; i++) {
                    final View child = mMatchParentChildren.get(i);

                    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    int childWidthMeasureSpec;
                    int childHeightMeasureSpec;

                    if (lp.width == LayoutHelper.MATCH_PARENT) {
                        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() -
                                getPaddingLeft() - getPaddingRight() -
                                lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                    } else {
                        childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
                    }

                    if (lp.height == LayoutHelper.MATCH_PARENT) {
                        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() -
                                getPaddingTop() - getPaddingBottom() - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                    } else {
                        childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
                    }

                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        } catch (Exception e) {

            try {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } catch (Exception e2) {

                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(getContext(), 10), MeasureSpec.EXACTLY));
            }
        }
    }
}