package org.michaelbel.material.extensions;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.michaelbel.material.R;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;

/**
 * Date: 3 FEB 2018
 * Time: 23:22 MSK
 *
 * @author Michael Bel
 */

@SuppressWarnings("all")
public class Extensions {

    public static int dp(@NonNull Context context,  float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static void copyToClipboard(Context context, @NonNull CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(text, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clipData);
        }
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, @FloatRange(from = 0.00F, to = 1.00F) float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static double[] rgbToHsv(int r, int g, int b) {
        double rf = r / 255.0;
        double gf = g / 255.0;
        double bf = b / 255.0;
        double max = (rf > gf && rf > bf) ? rf : (gf > bf) ? gf : bf;
        double min = (rf < gf && rf < bf) ? rf : (gf < bf) ? gf : bf;
        double h, s;
        double d = max - min;

        s = max == 0 ? 0 : d / max;

        if (max == min) {
            h = 0;
        } else {
            if (rf > gf && rf > bf) {
                h = (gf - bf) / d + (gf < bf ? 6 : 0);
            } else if (gf > bf) {
                h = (bf - rf) / d + 2;
            } else {
                h = (rf - gf) / d + 4;
            }

            h /= 6;
        }

        return new double[]{ h, s, max };
    }

    public static int[] hsvToRgb(double h, double s, double v) {
        double r = 0, g = 0, b = 0;
        double i = (int) Math.floor(h * 6);
        double f = h * 6 - i;
        double p = v * (1 - s);
        double q = v * (1 - f * s);
        double t = v * (1 - (1 - f) * s);

        switch ((int) i % 6) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
        }

        return new int[]{(int) (r * 255), (int) (g * 255), (int) (b * 255)};
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static void clearCursorDrawable(EditText editText) {
        if (editText == null) {
            return;
        }

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.setInt(editText, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int selectableItemBackground(Context context) {
        int[] attrs = new int[] {
                R.attr.selectableItemBackground
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return backgroundResource;
    }

    public static int selectableItemBackgroundBorderless(Context context) {
        int[] attrs = new int[] {
                R.attr.selectableItemBackgroundBorderless
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return backgroundResource;
    }

    public static Drawable selectableItemBackgroundDrawable(Context context) {
        int[] attrs = new int[] {
                android.R.attr.selectableItemBackground
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawableFromTheme;
    }

    public static Drawable selectableItemBackgroundBorderlessDrawable(Context context) {
        int[] attrs = new int[] {
                android.R.attr.selectableItemBackgroundBorderless
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawableFromTheme;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isUndefined(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_UNDEFINED;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }

        return result;
    }

    public static boolean isScreenLock(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isRTL(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isLTR(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;
    }

    public static String loadProperty(Context context, String fileName, String key) {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static float convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int getAttrColor(@NonNull Context context, @AttrRes int colorAttr) {
        int color = 0;
        int[] attrs = new int[] {
                colorAttr
        };

        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs);
            color = typedArray.getColor(0, 0);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return color;
    }

    public static int[] getColorArray(@NonNull Context context, @ArrayRes int arrayRes) {
        if (arrayRes == 0) {
            return null;
        }

        TypedArray ta = context.getResources().obtainTypedArray(arrayRes);
        int[] colors = new int[ta.length()];

        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }

        ta.recycle();
        return colors;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Drawable getIcon(Context context, @DrawableRes int resource, int colorFilter) {
        return getIcon(context, resource, colorFilter, PorterDuff.Mode.MULTIPLY);
    }

    public static Drawable getIcon(Context context, @DrawableRes int resource, int colorFilter, PorterDuff.Mode mode) {
        Drawable iconDrawable = ContextCompat.getDrawable(context, resource);

        if (iconDrawable != null) {
            iconDrawable.clearColorFilter();
            iconDrawable.mutate().setColorFilter(colorFilter, mode);
        }

        return iconDrawable;
    }

    public static String formatSize(long size) {
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", size);
        } else if (size < Math.pow(1024, 2)) {
            return String.format(Locale.getDefault(), "%.1f KB", size / 1024.0F);
        } else if (size < Math.pow(1024, 3)) {
            return String.format(Locale.getDefault(), "%.1f MB", size / Math.pow(1024.0F, 2));
        } else if (size < Math.pow(1024, 4)) {
            return String.format(Locale.getDefault(), "%.1f GB", size / Math.pow(1024.0F, 3));
        } else if (size < Math.pow(1024, 5)) {
            return String.format(Locale.getDefault(), "%.1f TB", size / Math.pow(1024.0F, 4));
        } else if (size < Math.pow(1024, 6)) {
            return String.format(Locale.getDefault(), "%.1f PB", size / Math.pow(1024.0F, 5));
        } else if (size < Math.pow(1024, 7)) {
            return String.format(Locale.getDefault(), "%.1f EB", size / Math.pow(1024.0F, 6));
        } else if (size < Math.pow(1024, 8)) {
            return String.format(Locale.getDefault(), "%.1f ZB", size / Math.pow(1024.0F, 7));
        } else {
            return String.format(Locale.getDefault(), "%.1f YB", size / Math.pow(1024.0F, 8));
        }
    }
}