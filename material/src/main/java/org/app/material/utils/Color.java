package org.app.material.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

public class Color {

    public static int getThemeColor(@NonNull Context context, @AttrRes int colorAttr) {
        int[] attrs = new int[] {
                colorAttr
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();

        return color;
    }
}