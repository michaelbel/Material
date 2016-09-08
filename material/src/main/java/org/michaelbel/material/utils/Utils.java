package org.michaelbel.material.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.michaelbel.material.R;
import org.michaelbel.material.anim.ViewProxy;

public class Utils {

    public static volatile Handler applicationHandler;

    public static int dp(@NonNull Context context,  float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static int selectableItemBackgroundBorderless(@NonNull Context context) {
        int[] attrs = new int[] {
                R.attr.selectableItemBackgroundBorderless
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static int getAttrColor(@NonNull Context context, @AttrRes int colorAttr) {
        int[] attrs = new int[] {
                colorAttr
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();

        return color;
    }

    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static boolean isTablet() {
        return false;
    }

    public static boolean isTablet(@NonNull Context context) {
        return false;
    }

    public static void runOnUIThread(Context context, Runnable runnable) {
        runOnUIThread(context, runnable, 0);
    }

    public static void runOnUIThread(Context context, Runnable runnable, long delay) {
        applicationHandler = new Handler(context.getMainLooper());

        if (delay == 0) {
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Context context, Runnable runnable) {
        applicationHandler = new Handler(context.getMainLooper());
        applicationHandler.removeCallbacks(runnable);
    }

    public static Drawable selectableItemBackgroundDrawable(@NonNull Context context) {
        int[] attrs = new int[] {
                android.R.attr.selectableItemBackground
        };

        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();

        return drawableFromTheme;
    }

    public static float dpf2(float value) {
        return context.getResources().getDisplayMetrics().density * value;
    }







    private static Context context;
    private static float density = 1;
    public static Point displaySize = new Point();

    public static void bind(@NonNull Context context) {
        Utils.context = context;
    }

    public static float getDensity() {
        density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static Drawable getIcon(@DrawableRes int resource, int colorFilter) {
        return getIcon(resource, colorFilter, PorterDuff.Mode.MULTIPLY);
    }

    public static Drawable getIcon(@DrawableRes int resource, int colorFilter, PorterDuff.Mode mode) {
        Drawable iconDrawable = context.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            iconDrawable.clearColorFilter();
            iconDrawable.mutate().setColorFilter(colorFilter, mode);
        }

        return iconDrawable;
    }

    public static void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static void shakeView(final Context context, final View view, final float x, final int num) {
        if (num == 6) {
            ViewProxy.setTranslationX(view, 0);
            view.clearAnimation();
            return;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", dp(context, x)));
        animatorSet.setDuration(50);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                shakeView(context, view, num == 5 ? 0 : -x, num + 1);
            }
        });
        animatorSet.start();
    }
}