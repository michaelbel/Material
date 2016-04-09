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

public class TextCell extends FrameLayout {

    private float height;
    private View listDivider;
    private TextView titleTextView;
    private TextView valueTextView;

    public TextCell(Context context) {
        super(context);

        //FrameLayout.LayoutParams params = LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 0.5f);

        this.height = 48;
        //this.setLayoutParams(params);
        this.setBackgroundColor(0xFFFFFFFF);

        titleTextView = new TextView(context);
        titleTextView.setLines(1);
        titleTextView.setMaxLines(1);
        titleTextView.setSingleLine(true);
        titleTextView.setVisibility(INVISIBLE);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        titleTextView.setTextColor(0xFF000000);
        titleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        titleTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(titleTextView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        valueTextView = new TextView(context);
        valueTextView.setLines(1);
        valueTextView.setMaxLines(1);
        valueTextView.setSingleLine(true);
        valueTextView.setTextColor(0xFFF44336);
        valueTextView.setVisibility(INVISIBLE);
        valueTextView.setEllipsize(TextUtils.TruncateAt.END);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        valueTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        valueTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(valueTextView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        listDivider.setBackgroundColor(0xFFF5F5F5);
        addView(listDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.8f, Gravity.BOTTOM));
    }

    public TextCell addTitle(String title) {
        valueTextView.setVisibility(INVISIBLE);

        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(title);
        return this;
    }

    public TextCell addValue(String value) {
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        return this;
    }

    public TextCell addDivider() {
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