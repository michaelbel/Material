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

package org.app.material.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.app.material.AndroidUtilities;

public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private static int getSize(Context context, float size) {
        return (int) (size < 0 ? size : AndroidUtilities.dp(context, size));
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height), gravity);
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height), gravity);
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, float height) {
        return new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, float height, int gravity) {
        return new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height), gravity);
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, float height, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, float height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height), gravity);
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height), weight);
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height) {
        return makeRelative(context, width, height, 0, 0, 0, 0, -1, -1, -1);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int alignParent) {
        return makeRelative(context, width, height, 0, 0, 0, 0, alignParent, -1, -1);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int alignRelative, int anchorRelative) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.addRule(alignRelative, anchorRelative);
        return layoutParams;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int alignParent, int alignRelative, int anchorRelative) {
        return makeRelative(context, width, height, 0, 0, 0, 0, alignParent, alignRelative, anchorRelative);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        return makeRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, -1, -1, -1);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignParent) {
        return makeRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, alignParent, -1, -1);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, float width, float height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignRelative, int anchorRelative) {
        return makeRelative(context, width, height, leftMargin, topMargin, rightMargin, bottomMargin, -1, alignRelative, anchorRelative);
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, float width, float height, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int alignParent, int alignRelative, int anchorRelative) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        if (alignParent >= 0) {
            layoutParams.addRule(alignParent);
        }
        if (alignRelative >= 0 && anchorRelative >= 0) {
            layoutParams.addRule(alignRelative, anchorRelative);
        }
        layoutParams.leftMargin = AndroidUtilities.dp(context, leftMargin);
        layoutParams.topMargin = AndroidUtilities.dp(context, topMargin);
        layoutParams.rightMargin = AndroidUtilities.dp(context, rightMargin);
        layoutParams.bottomMargin = AndroidUtilities.dp(context, bottomMargin);
        return layoutParams;
    }

    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, float width, float height) {
        return new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, int width, int height, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }

    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, int width, int height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        layoutParams.gravity = gravity;
        layoutParams.setMargins(AndroidUtilities.dp(context, leftMargin), AndroidUtilities.dp(context, topMargin), AndroidUtilities.dp(context, rightMargin), AndroidUtilities.dp(context, bottomMargin));
        return layoutParams;
    }
}