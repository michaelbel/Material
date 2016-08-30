package org.michaelbel.material.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.michaelbel.material.R;
import org.michaelbel.material.animation.ViewProxy;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AndroidUtilities {

    private static final String TAG = AndroidUtilities.class.getSimpleName();

    private static Context context;
    private static float density = 1;
    public static Point displaySize = new Point();

    public static void bind(@NonNull Context context) {
        AndroidUtilities.context = context;
    }

    public static int dp (float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static int dp (@NonNull Context context,  float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static float dpf2(float value) {
        return context.getResources().getDisplayMetrics().density * value;
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

    public static int selectableItemBackgroundBorderless() {
        int[] attrs = new int[] {
                R.attr.selectableItemBackgroundBorderless
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        return backgroundResource;
    }

    public static int getThemeColor(@AttrRes int colorAttr) {
        int[] attrs = new int[] {
                colorAttr
        };

        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();

        return color;
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

    public static void vibrate(int duration) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_COLOR = 4;
    public static final int FLAG_TAG_ALL = FLAG_TAG_BR | FLAG_TAG_BOLD | FLAG_TAG_COLOR;

    public static SpannableStringBuilder replaceTags(Context context, @StringRes int stringId) {
        return replaceTags(context.getString(stringId), FLAG_TAG_ALL);
    }

    public static SpannableStringBuilder replaceTags(String str, int flag) {
        try {
            int start;
            int end;
            StringBuilder stringBuilder = new StringBuilder(str);

            if ((flag & FLAG_TAG_BR) != 0) {
                while ((start = stringBuilder.indexOf("<br>")) != -1) {
                    stringBuilder.replace(start, start + 4, "\n");
                }

                while ((start = stringBuilder.indexOf("<br/>")) != -1) {
                    stringBuilder.replace(start, start + 5, "\n");
                }
            }

            ArrayList<Integer> bolds = new ArrayList<>();

            if ((flag & FLAG_TAG_BOLD) != 0) {
                while ((start = stringBuilder.indexOf("<b>")) != -1) {
                    stringBuilder.replace(start, start + 3, "");
                    end = stringBuilder.indexOf("</b>");

                    if (end == -1) {
                        end = stringBuilder.indexOf("<b>");
                    }

                    stringBuilder.replace(end, end + 4, "");
                    bolds.add(start);
                    bolds.add(end);
                }
            }

            ArrayList<Integer> colors = new ArrayList<>();

            if ((flag & FLAG_TAG_COLOR) != 0) {
                while ((start = stringBuilder.indexOf("<c#")) != -1) {
                    stringBuilder.replace(start, start + 2, "");
                    end = stringBuilder.indexOf(">", start);
                    int color = Color.parseColor(stringBuilder.substring(start, end));
                    stringBuilder.replace(start, end + 1, "");
                    end = stringBuilder.indexOf("</c>");
                    stringBuilder.replace(end, end + 4, "");
                    colors.add(start);
                    colors.add(end);
                    colors.add(color);
                }
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder);

            for (int a = 0; a < bolds.size() / 2; a++) {
                spannableStringBuilder.setSpan(new TypefaceSpan("sans-serif-medium"), bolds.get(a * 2), bolds.get(a * 2 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            for (int a = 0; a < colors.size() / 3; a++) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(colors.get(a * 3 + 2)), colors.get(a * 3), colors.get(a * 3 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return spannableStringBuilder;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return new SpannableStringBuilder(str);
    }
}