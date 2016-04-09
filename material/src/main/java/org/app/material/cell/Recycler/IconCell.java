package org.app.material.cell.Recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;

public class IconCell extends FrameLayout {

    private TextView textView;
    private ImageView iconView;

    private View listShortDivider;

    public IconCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);

        this.setElevation(4);
        this.setLayoutParams(params);
        this.setBackground(AndroidUtilities.getRipple(0xFFFFFFFF, 0xFFE0E0E0));

        iconView = new ImageView(context);
        iconView.setVisibility(INVISIBLE);
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        iconView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(iconView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        textView = new TextView(context);
        textView.setVisibility(INVISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xFF444444);
        textView.setGravity(Gravity.START);
        textView.setPadding(AndroidUtilities.dp(72), AndroidUtilities.dp(16), AndroidUtilities.dp(72), AndroidUtilities.dp(16));
        addView(textView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        listShortDivider = new View(context);
        listShortDivider.setVisibility(INVISIBLE);
        listShortDivider.setBackgroundColor(0xFF444444);
        addView(listShortDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.3f, Gravity.BOTTOM, 70, 0, 0, 0));
    }

    public IconCell addTitle(String text) {
        textView.setVisibility(VISIBLE);
        textView.setText(text);
        return this;
    }

    public IconCell addIcon(int icon) {
        iconView.setVisibility(VISIBLE);
        iconView.setImageResource(icon);
        return this;
    }

    public IconCell addIcon(Drawable icon) {
        iconView.setVisibility(VISIBLE);
        iconView.setImageDrawable(icon);
        return this;
    }

    public IconCell addShortDivider() {
        listShortDivider.setVisibility(VISIBLE);
        return this;
    }
}