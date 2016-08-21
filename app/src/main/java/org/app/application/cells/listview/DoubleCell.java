package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;

public class DoubleCell extends BaseCell {

    private TextView textView;
    private TextView valueView;

    public DoubleCell(Context context) {
        super(context);

        setHeight(62);
        setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16, 10, 16, 0));
        addView(textView);

        valueView = new TextView(context);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        valueView.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        valueView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 16, 0, 16, 10));
        addView(valueView);
    }

    public DoubleCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public DoubleCell setValue(@StringRes int stringId) {
        valueView.setText(getContext().getText(stringId));
        return this;
    }
}