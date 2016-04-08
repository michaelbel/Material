package org.app.material.cell;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;
import org.app.material.Switch;

public class SwitchCell extends FrameLayout {

    private float height;
    private View listDivider;
    private TextView titleTextView;
    private Switch mSwitch;

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

        mSwitch = new Switch(context);
        mSwitch.setClickable(false);
        //mSwitch.setEnabled(false);
        //mSwitch.setZ(1);
        //mSwitch.setActivated(false);
        //mSwitch.setContextClickable(false);
        //mSwitch.setHovered(false);
        //mSwitch.setPressed(false);
        mSwitch.setFocusable(false);
        mSwitch.setFocusableInTouchMode(false);
        mSwitch.setVisibility(INVISIBLE);
        mSwitch.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        mSwitch.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(mSwitch, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        listDivider.setBackgroundColor(0xFFF5F5F5);
        addView(listDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.8f, Gravity.BOTTOM));
    }

    public SwitchCell addTitle(String title) {
        titleTextView.setText(title);
        return this;
    }

    public SwitchCell addSwitch(boolean checked) {
        mSwitch.setVisibility(VISIBLE);
        mSwitch.setChecked(checked);
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