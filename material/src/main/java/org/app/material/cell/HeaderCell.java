package org.app.material.cell;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;
import org.app.material.R;

public class HeaderCell extends FrameLayout {

    private TextView headText;
    public FrameLayout dividerLayout;
    public FrameLayout dividerTopLayout;

    public HeaderCell(Context context) {
        super(context);

        dividerLayout = new FrameLayout(context);
        dividerLayout.setVisibility(INVISIBLE);
        dividerLayout.setBackgroundResource(R.drawable.divider);
        addView(dividerLayout, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        dividerTopLayout = new FrameLayout(context);
        dividerTopLayout.setVisibility(INVISIBLE);
        dividerTopLayout.setBackgroundResource(R.drawable.divider_top);
        addView(dividerTopLayout, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));

        FrameLayout headLayout = new FrameLayout(context);
        headText = new TextView(context);
        headText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        headText.setTypeface(AndroidUtilities.getTypeface("medium.ttf"));
        headText.setGravity(Gravity.START);
        headText.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(14), AndroidUtilities.dp(16), AndroidUtilities.dp(14));
        headLayout.addView(headText);
        addView(headLayout, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
    }

    public HeaderCell addDivider() {
        dividerLayout.setVisibility(VISIBLE);
        dividerTopLayout.setVisibility(VISIBLE);
        return this;
    }

    public HeaderCell addHead(String text) {
        headText.setText(text.toUpperCase());
        return this;
    }
}