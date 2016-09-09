package org.michaelbel.material.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.michaelbel.material.util.Utils;

@SuppressWarnings({"unused"})
public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private static int getSize(@NonNull Context context, float size) {
        return (int) (size < 0 ? size : Utils.dp(context, size));
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
}