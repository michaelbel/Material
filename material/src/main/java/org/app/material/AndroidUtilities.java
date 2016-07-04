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

package org.app.material;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.app.material.anim.ViewProxy;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Locale;

public class AndroidUtilities {

    private static Context context;
    private static float density = 1;
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    private static volatile Handler applicationHandler;
    public static Point displaySize = new Point();
    private static Vibrator vibrator;

    public static void bind(@NonNull Context context) {
        AndroidUtilities.context = context;
    }

    public static int dp (float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static float dpf2(float value) {
        return context.getResources().getDisplayMetrics().density * value;
    }

    public static float getDensity() {
        density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static boolean isPortrait() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static Drawable getIcon(@DrawableRes int resource, int colorFilter) {
        Drawable iconDrawable = context.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            iconDrawable.mutate().setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
        }

        return iconDrawable;
    }

    public static Typeface getTypeface(String assetPath) {
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

    public static int getContextColor(@AttrRes int androidAttr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int selectableItemBackground() {
        int[] attrs = new int[]{org.app.material.R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static int selectableItemBackgroundBorderless() {
        int[] attrs = new int[]{R.attr.selectableItemBackgroundBorderless};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static Drawable customSelectable() {
        return context.getDrawable(R.drawable.list_selector);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result ;
    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }

        InputMethodManager inputManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }

        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.isActive(view);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isActive()) {
            return;
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*@RequiresPermission(Manifest.permission.VIBRATE)*/
    public static void vibrate(int duration) {
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

    public static String formatSize(long size) {
        if (size < 1024) {
            return String.format(Locale.US, "%d B", size);
        } else if (size < Math.pow(1024, 2)) {
            return String.format(Locale.US, "%.1f KB", size / 1024.0f);
        } else if (size < Math.pow(1024, 3)) {
            return String.format(Locale.US, "%.1f MB", size / Math.pow(1024.0f, 2));
        } else if (size < Math.pow(1024, 4)) {
            return String.format(Locale.US, "%.1f GB", size / Math.pow(1024.0f, 3));
        } else if (size < Math.pow(1024, 5)) {
            return String.format(Locale.US, "%.1f TB", size / Math.pow(1024.0f, 4));
        } else if (size < Math.pow(1024, 6)) {
            return String.format(Locale.US, "%.1f PB", size / Math.pow(1024.0f, 5));
        } else if (size < Math.pow(1024, 7)) {
            return String.format(Locale.US, "%.1f EB", size / Math.pow(1024.0f, 6));
        } else if (size < Math.pow(1024, 8)) {
            return String.format(Locale.US, "%.1f ZB", size / Math.pow(1024.0f, 7));
        } else {
            return String.format(Locale.US, "%.1f YB", size / Math.pow(1024.0f, 8));
        }
    }

    public static void shakeView(final View view, final float x, final int num) {
        if (num == 6) {
            ViewProxy.setTranslationX(view, 0);
            view.clearAnimation();
            return;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", dp(x)));
        animatorSet.setDuration(50);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                shakeView(view, num == 5 ? 0 : -x, num + 1);
            }
        });
        animatorSet.start();
    }

    public static void clearCursorDrawable(EditText editText) {
        if (editText == null || Build.VERSION.SDK_INT < 12) {
            return;
        }

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.setInt(editText, 0);
        } catch (Exception ignored) {}
    }
}