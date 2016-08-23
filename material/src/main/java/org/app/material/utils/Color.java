package org.app.material.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

import org.app.material.annotation.Experimental;

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

    @Experimental(priority = "MEDIUM", task = "Improve method")
    public static int setColorWithAlpha(int color, float mAlpha) {
        int alpha = Math.round(android.graphics.Color.alpha(color) * mAlpha);
        int red = android.graphics.Color.red(color);
        int green = android.graphics.Color.green(color);
        int blue = android.graphics.Color.blue(color);
        return android.graphics.Color.argb(alpha, red, green, blue);
    }
}