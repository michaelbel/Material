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
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.app.material.AndroidUtilities;

public class LayoutHelper {

    /*
     * Includes:
     *
     * ScrollView
     * SwipeRefreshLayout
     * CoordinatorLayout
     * FrameLayout
     * LinearLayout
     * RelativeLayout
     * CardView
     * TableRow (partially)
     * TableLayout
     * GridLayout (not implemented)
     * DrawerLayout
     */

    /**
     * The dimension to match that of the parent element.
     */
    public static final int MATCH_PARENT = -1;

    /**
     * The dimension to the size required to fit the content of the element.
     */
    public static final int WRAP_CONTENT = -2;

    /**
     * @param context Current context.
     * @param size
     * @return
     */
    private static int getSize(Context context, float size) {
        return (int) (size < 0 ? size : AndroidUtilities.dp(context, size));
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @return
     */
    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity) {
        return new ScrollView.LayoutParams(getSize(context, width), getSize(context, height), gravity);
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static ScrollView.LayoutParams makeScroll(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static SwipeRefreshLayout.LayoutParams makeSwipeRefresh(Context context, int width, int height) {
        return new SwipeRefreshLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, float width, float height) {
        return new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, float width, float height, int gravity) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param anhorGravity
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, float width, float height, int gravity, int anhorGravity) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.anchorGravity = anhorGravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param anhorGravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static CoordinatorLayout.LayoutParams makeCoordinator(Context context, int width, int height, int gravity, int anhorGravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.anchorGravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height) {
        return new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static FrameLayout.LayoutParams makeFrame(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height) {
        return new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param weight
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, int gravity, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static LinearLayout.LayoutParams makeLinear(Context context, int width, int height, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
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

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height) {
        return new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param verb
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int verb) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param verb
     * @param anhor
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height, int verb, int anhor) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb, anhor);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param verb
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height,int verb, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb);
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param verb
     * @param anhor
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static RelativeLayout.LayoutParams makeRelative(Context context, int width, int height,int verb, int anhor, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.addRule(verb, anhor);
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static CardView.LayoutParams makeCard(Context context, int width, int height) {
        return new CardView.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static CardView.LayoutParams makeCard(Context context, int width, int height, int gravity) {
        CardView.LayoutParams params = new CardView.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static CardView.LayoutParams makeCard(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        CardView.LayoutParams params = new CardView.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static CardView.LayoutParams makeCard(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        CardView.LayoutParams params = new CardView.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height) {
        return new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, int gravity) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param weight
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, float weight) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, int gravity, float weight) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableRow.LayoutParams makeTableRow(Context context, int width, int height, int gravity, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height) {
        return new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, int gravity) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param weight
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, float weight) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, int gravity, float weight) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context Current context.
     * @param width
     * @param height
     * @param gravity
     * @param weight
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static TableLayout.LayoutParams makeTable(Context context, int width, int height, int gravity, float weight, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.weight = weight;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static DrawerLayout.LayoutParams makeDrawer(Context context, int width, int height) {
        return new DrawerLayout.LayoutParams(getSize(context, width), getSize(context, height));
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @return
     */
    public static DrawerLayout.LayoutParams makeDrawer(Context context, int width, int height, int gravity) {
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static DrawerLayout.LayoutParams makeDrawer(Context context, int width, int height, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /**
     * @param context
     * @param width
     * @param height
     * @param gravity
     * @param startMargin
     * @param topMargin
     * @param endMargin
     * @param bottomMargin
     * @return
     */
    public static DrawerLayout.LayoutParams makeDrawer(Context context, int width, int height, int gravity, float startMargin, float topMargin, float endMargin, float bottomMargin) {
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(getSize(context, width), getSize(context, height));
        params.gravity = gravity;
        params.leftMargin = getSize(context, startMargin);
        params.topMargin = getSize(context, topMargin);
        params.rightMargin = getSize(context, endMargin);
        params.bottomMargin = getSize(context, bottomMargin);
        return params;
    }

    /*public static GridLayout.LayoutParams makeGrid(Context context, int width, int height) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = width;
        params.height = height;
        return params;
    }*/
}