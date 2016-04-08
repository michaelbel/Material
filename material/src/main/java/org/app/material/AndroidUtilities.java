package org.app.material;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;

import org.app.material.core.AppLoader;

public class AndroidUtilities {

    public static float density = 1;

    static {
        density = AppLoader.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }

        return (int) Math.ceil(density * value);
    }

    public static boolean isPortrait() {
        return AppLoader.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape() {
        return AppLoader.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Drawable getDrawable(int resource, int color) {
        int iconColor = 0;
        Drawable iconDrawable = AppLoader.applicationContext.getResources().getDrawable(resource, null);

        if (color != 0) {
            iconColor = color;
        }

        if (iconDrawable != null) {
            iconDrawable.setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY));
        }

        return iconDrawable;
    }

    public static Typeface getTypeface(String path) {
        Typeface typeFace;
        typeFace = Typeface.createFromAsset(AppLoader.applicationContext.getAssets(), path);
        return typeFace;
    }
}