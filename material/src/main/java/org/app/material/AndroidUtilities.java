package org.app.material;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Browser;

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

    public static boolean isLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Drawable getIcon(int resource, int colorFilter) {
        Drawable iconDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(resource, null);

        if (iconDrawable != null) {
            //iconDrawable.setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
            iconDrawable.mutate().setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
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

    public static void openUrl(Context context, String url) {
        if (context == null || url == null) {
            return;
        }

        openUrl(context, Uri.parse(url), 0, 0);
    }

    public static void openUrl(Context context, String url, int toolbarColor) {
        if (context == null || url == null) {
            return;
        }

        openUrl(context, Uri.parse(url), toolbarColor, 0);
    }

    public static void openUrl(Context context, String url, int toolbarColor, int shareImage) {
        if (context == null || url == null) {
            return;
        }

        openUrl(context, Uri.parse(url), toolbarColor, shareImage);
    }

    public static void openUrl(Context context, Uri uri, int toolbarColor, int shareImage) {
        if (context == null || uri == null) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra("android.support.customtabs.extra.SESSION", (Parcelable) null);
            intent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", toolbarColor);
            intent.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", 1);
            Intent actionIntent = new Intent(Intent.ACTION_SEND);
            actionIntent.setType("text/plain");
            actionIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            actionIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            PendingIntent pendingIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.customtabs.customaction.ID", 0);
            bundle.putParcelable("android.support.customtabs.customaction.ICON", BitmapFactory.decodeResource(context.getResources(), shareImage));
            bundle.putString("android.support.customtabs.customaction.DESCRIPTION", "ShareFile");
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", pendingIntent);
            intent.putExtra("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
            intent.putExtra("android.support.customtabs.extra.TINT_ACTION_BUTTON", false);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception ignored) {}
    }
}