/*
 * Copyright 2015 Michael Bel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.michaelbel.material.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.michaelbel.material.extensions.Extensions;

@SuppressWarnings("all")
public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private static int getSize(@NonNull Context context, float size) {
        return (int) (size < 0 ? size : Extensions.dp(context, size));
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height), gravity);
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height) {
        return new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height) {
        return new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int verb) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb);
        return params;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int verb, int anhor) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb, anhor);
        return params;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height,int verb, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb);
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height,int verb, int anhor, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb, anhor);
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    public static SwipeRefreshLayout.LayoutParams makeSwipeRefresh(Context context, int width, int height) {
        return new SwipeRefreshLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }
}