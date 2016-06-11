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

package org.app.material.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import org.app.material.R;

import org.app.material.AndroidUtilities;

public class Browser {

    public static void openUrl(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.putExtra("android.support.customtabs.extra.SESSION", (Parcelable) null);
            intent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", AndroidUtilities.getContextColor(context, R.attr.colorPrimary));
            intent.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", 1);
            Intent actionIntent = new Intent(Intent.ACTION_SEND);
            actionIntent.setType("text/plain");
            actionIntent.putExtra(Intent.EXTRA_TEXT, Uri.parse(url).toString());
            actionIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.customtabs.customaction.ID", 0);
            bundle.putParcelable("android.support.customtabs.customaction.ICON", BitmapFactory.decodeResource(context.getResources(), R.drawable.abc_ic_menu_share_mtrl_alpha));
            bundle.putString("android.support.customtabs.customaction.DESCRIPTION", "Share link");
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", pendingIntent);
            intent.putExtra("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
            intent.putExtra("android.support.customtabs.extra.TINT_ACTION_BUTTON", false);
            intent.putExtra(android.provider.Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception ignored) {}
    }
}