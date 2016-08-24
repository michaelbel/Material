package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.RadioButton;

public class RadioCell extends BaseCell {

    private TextView textView;
    private RadioButton radioButton;

    public RadioCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        radioButton = new RadioButton(context);
        radioButton.setLayoutParams(LayoutHelper.makeFrame(context, 22, 22, Gravity.START |
                Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(radioButton);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 54, 0, 0, 0));
        addView(textView);
    }

    public RadioCell setText(String text) {
        textView.setText(text);
        return this;
    }

    public RadioCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public RadioCell setChecked(boolean checked, boolean animated) {
        radioButton.setChecked(checked, animated);
        return this;
    }
}