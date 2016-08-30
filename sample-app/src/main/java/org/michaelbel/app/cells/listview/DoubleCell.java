package org.michaelbel.app.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.michaelbel.app.R;
import org.michaelbel.material.widget.LayoutHelper;

public class DoubleCell extends BaseCell {

    private TextView textView;
    private TextView valueTextView;

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

        valueTextView = new TextView(context);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        valueTextView.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        valueTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 16, 0, 16, 10));
        addView(valueTextView);
    }

    public DoubleCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public DoubleCell setValue(@StringRes int stringId) {
        valueTextView.setText(getContext().getText(stringId));
        return this;
    }
}