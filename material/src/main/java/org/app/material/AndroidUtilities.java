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

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Hashtable;

public class AndroidUtilities {

    /**
     * Binder context.
     */
    private static Context context;

    /**
     *
     */
    private static float density = 1;

    /**
     *
     */
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    /**
     *
     */
    private static volatile Handler applicationHandler;

    public static Point displaySize = new Point();

    private static Vibrator vibrator;

    public static void bind(@NonNull Context context) {
        AndroidUtilities.context = context;
    }

    public static int dp (float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static int dp (@NonNull Context context, float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static float dpf2(float value) {
        return context.getResources().getDisplayMetrics().density * value;
    }

    public static float dpf2(@NonNull Context context, float value) {
        return context.getResources().getDisplayMetrics().density * value;
    }

    public static float getDensity() {
        density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static float getDensity(@NonNull Context context) {
        density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static void runOnUIThread(@NonNull Context context, Runnable runnable) {
        runOnUIThread(context, runnable, 0);
    }

    public static void runOnUIThread(@NonNull Context context, Runnable runnable, long delay) {
        if (delay == 0) {
            applicationHandler = new Handler(context.getMainLooper());
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static boolean isPortrait() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isPortrait(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static Drawable getIcon(int resource, int colorFilter) {
        Drawable iconDrawable = context.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            iconDrawable.mutate().setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
        }

        return iconDrawable;
    }

    public static Drawable getIcon(@NonNull Context context, int resource, int colorFilter) {
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

    public static Typeface getTypeface(@NonNull Context context, String assetPath) {
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

    public static int getContextColor(int androidAttr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int getContextColor(@NonNull Context context, int androidAttr) {
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

    public static int selectableItemBackground(@NonNull Context context) {
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

    public static int selectableItemBackgroundBorderless(@NonNull Context context) {
        int[] attrs = new int[]{R.attr.selectableItemBackgroundBorderless};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static Drawable customSelectable() {
        return context.getDrawable(R.drawable.list_selector);
    }

    public static Drawable customSelector(@NonNull Context context) {
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

    public static int getStatusBarHeight(@NonNull Context context) {
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

    public static void vibrate(int duration) {
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

    public static void vibrate(@NonNull Context context, int duration) {
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

    /*private boolean hasVibratePermission(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(android.Manifest.permission.VIBRATE, context.getPackageName());

        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }*/
}