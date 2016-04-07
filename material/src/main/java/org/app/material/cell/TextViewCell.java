package org.app.material.cell;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.DimenUtil;
import org.app.material.LayoutHelper;

public class TextViewCell extends FrameLayout {

    private View listDivider;
    private TextView textView;
    private float cellHeight;

    public TextViewCell(Context context) {
        super(context);

        cellHeight = 52;

        textView = new TextView(context);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xff000000);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setPadding(DimenUtil.dp(16), 0, DimenUtil.dp(16), 0);
        addView(textView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        listDivider.setBackgroundColor(0xfff5f5f5);
        addView(listDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, 0.8f, Gravity.BOTTOM));
    }

    public TextViewCell addTitle(String title) {
        textView.setText(title);
        return this;
    }

    public TextViewCell addDivider() {
        listDivider.setVisibility(VISIBLE);
        return this;
    }

    public void setCellHeight(float height) {
        this.cellHeight = height;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(DimenUtil.dp(getCellHeight()), MeasureSpec.EXACTLY));
    }
}