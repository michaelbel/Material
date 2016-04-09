package org.app.material.cell;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;

public class SwitchCell extends FrameLayout {

    private float height;
    private View listDivider;
    private TextView titleTextView;
    private SwitchCompat switchCompat;

    public SwitchCell(Context context) {
        super(context);

        this.height = 48;
        this.setBackgroundColor(0xFFFFFFFF);

        titleTextView = new TextView(context);
        titleTextView.setLines(1);
        titleTextView.setMaxLines(1);
        titleTextView.setSingleLine(true);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        titleTextView.setTextColor(0xFF000000);
        titleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        titleTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(titleTextView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        switchCompat = new SwitchCompat(context);
        switchCompat.setClickable(false);
        switchCompat.setFocusable(false);
        switchCompat.setFocusableInTouchMode(false);
        switchCompat.setVisibility(INVISIBLE);
        switchCompat.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        switchCompat.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(switchCompat, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        listDivider.setBackgroundColor(0xFFBDBDBD);
        addView(listDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.8f, Gravity.BOTTOM));
    }

    public SwitchCell addTitle(String title) {
        titleTextView.setText(title);
        return this;
    }

    public SwitchCell addSwitch(boolean checked) {
        switchCompat.setVisibility(VISIBLE);
        switchCompat.setChecked(checked);
        return this;
    }

    public SwitchCell addDivider() {
        listDivider.setVisibility(VISIBLE);
        return this;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getCellHeight() {
        return height;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(getCellHeight()), MeasureSpec.EXACTLY));
    }
}