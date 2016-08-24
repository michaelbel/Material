package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;

public class TextCell extends BaseCell {

    private TextView textView;
    private TextView valueTextView;

    public TextCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(textView);

        valueTextView = new TextView(context);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        valueTextView.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        valueTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(valueTextView);
    }

    public TextCell setText(String text) {
        textView.setText(text);
        return this;
    }

    public TextCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public TextCell setValue(String value) {
        valueTextView.setText(value);
        return this;
    }

    public TextCell setValue(@StringRes int stringId) {
        valueTextView.setText(getContext().getText(stringId));
        return this;
    }
}