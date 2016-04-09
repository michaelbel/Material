package org.app.material;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;

import org.app.material.core.ApplicationLoader;

public class AndroidUtilities {

    public static float density = 1;

    static {
        density = ApplicationLoader.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }

        return (int) Math.ceil(density * value);
    }

    public static boolean isPortrait() {
        return ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape() {
        return ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Drawable getDrawable(int resource, int colorFilter) {
        Drawable iconDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            iconDrawable.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY));
        }

        return iconDrawable;
    }

    public static Typeface getTypeface(String path) {
        Typeface typeFace;
        typeFace = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), path);
        return typeFace;
    }

    public static Drawable getRipple(int background, int rippleColor) {
        ColorStateList colorStateList;
        RippleDrawable rippleDrawable;

        colorStateList = ColorStateList.valueOf(rippleColor);
        rippleDrawable = new RippleDrawable(colorStateList, new ColorDrawable(background), null);

        return rippleDrawable;
    }
}