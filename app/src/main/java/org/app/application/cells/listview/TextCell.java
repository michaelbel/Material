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
    private TextView valueView;

    public TextCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(textView);

        valueView = new TextView(context);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        valueView.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        valueView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(valueView);
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
        valueView.setText(value);
        return this;
    }

    public TextCell setValue(@StringRes int stringId) {
        valueView.setText(getContext().getText(stringId));
        return this;
    }
}