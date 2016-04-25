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

package org.app.material;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.TypedValue;

import java.util.Hashtable;

public class AndroidUtilities {

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    public static int dp (Context context, float value) {
        if (value == 0) {
            return 0;
        }

        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLollipopMR1() {
        return Build.VERSION.SDK_INT >= 22;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Drawable getIcon(Context context, int resource, int colorFilter) {
        Drawable iconDrawable = context.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            iconDrawable.mutate().setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
        }

        return iconDrawable;
    }

    public static Typeface getTypeface(Context context, String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    return null;
                }
            }

            return typefaceCache.get(assetPath);
        }
    }

    public static Drawable getRipple(int background, int rippleColor) {
        ColorStateList colorStateList;
        RippleDrawable rippleDrawable;

        colorStateList = ColorStateList.valueOf(rippleColor);
        rippleDrawable = new RippleDrawable(colorStateList, new ColorDrawable(background), null);

        return rippleDrawable;
    }

    public static int getContextColor(Context context, int androidAttr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    /* Annotation enum example:
    private static final int ONE = 1, TWO = 2, THREE = 3;
    @IntDef({ONE, TWO, THREE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NUMBER {}
    public void setNumber(@NUMBER int number) {} */
}