package org.app.material.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

public class Browser {

    private int toolbarColor;
    private int shareIcon;
    private String shareIconText;

    public void setToolbarColor(int color) {
        this.toolbarColor = color;
    }

    public void setShareIcon(int icon) {
        this.shareIcon = icon;
    }

    public void setShareIconHiddenText(String text) {
        this.shareIconText = text;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getShareIcon() {
        return shareIcon;
    }

    public String getShareIconText() {
        return shareIconText;
    }

    public void openUrl(Context context, String url) {
        if (context == null || url == null) {
            return;
        }

        openUrl(context, Uri.parse(url));
    }

    public void openUrl(Context context, Uri uri) {
        if (context == null || uri == null) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra("android.support.customtabs.extra.SESSION", (Parcelable) null);
            if (toolbarColor != 0) {
                intent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", getToolbarColor());
            }
            intent.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", 1);
            Intent actionIntent = new Intent(Intent.ACTION_SEND);
            actionIntent.setType("text/plain");
            actionIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
            actionIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.customtabs.customaction.ID", 0);
            if (shareIcon != 0) {
                bundle.putParcelable("android.support.customtabs.customaction.ICON", BitmapFactory.decodeResource(context.getResources(), getShareIcon()));
            }
            if (shareIconText != null) {
                bundle.putString("android.support.customtabs.customaction.DESCRIPTION", getShareIconText());
            }
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", pendingIntent);
            intent.putExtra("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
            intent.putExtra("android.support.customtabs.extra.TINT_ACTION_BUTTON", false);
            intent.putExtra(android.provider.Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception ignored) {}
    }
}