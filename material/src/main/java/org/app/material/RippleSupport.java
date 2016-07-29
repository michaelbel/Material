package org.app.material;

import android.content.Context;
import android.view.View;

public class RippleSupport {

    public static void setItemSelectable(View view) {
        view.setBackgroundResource(R.drawable.list_selector);
    }

    public static void setItemSelectableWhite(View view) {
        view.setBackgroundResource(R.drawable.list_selector_white);
    }

    public static void setSelectableItemBackground(Context context, View view) {
        view.setBackgroundResource(AndroidUtilities.selectableItemBackground(context));
    }

    public static void setSelectableItemBackgroundBorderless(Context context, View view) {
        view.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
    }
}