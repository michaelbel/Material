package org.app.material.cell;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;

public class RadioCell extends FrameLayout {

    private float height;
    private View listDivider;
    private TextView textView;
    private RadioButton radioButton;

    public RadioCell(Context context) {
        super(context);

        this.height = 48;
        this.setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xFF000000);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(textView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        radioButton = new RadioButton(context);
        radioButton.setClickable(false);
        radioButton.setFocusable(false);
        radioButton.setFocusableInTouchMode(false);
        radioButton.setVisibility(INVISIBLE);
        radioButton.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        radioButton.setPadding(AndroidUtilities.dp(6), 0, AndroidUtilities.dp(6), 0);
        addView(radioButton, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        listDivider.setBackgroundColor(0xFFF5F5F5);
        addView(listDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.8f, Gravity.BOTTOM));
    }

    public RadioCell addTitle(String title) {
        textView.setText(title);
        return this;
    }

    public RadioCell addRadio(boolean checked) {
        radioButton.setVisibility(VISIBLE);
        radioButton.setChecked(checked);
        return this;
    }

    public RadioCell addDivider() {
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
