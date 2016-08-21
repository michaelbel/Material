package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.CheckBox;
import org.app.material.widget.LayoutHelper;

public class CheckBoxCell extends BaseCell {

    private TextView textView;
    private CheckBox checkBox;

    public CheckBoxCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        checkBox = new CheckBox(context);
        checkBox.setLayoutParams(LayoutHelper.makeFrame(context, 20, 20, Gravity.START |
                Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(checkBox);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 52, 0, 0, 0));
        addView(textView);
    }

    public CheckBoxCell setText(String text) {
        textView.setText(text);
        return this;
    }

    public CheckBoxCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public CheckBoxCell setChecked(boolean checked, boolean animated) {
        checkBox.setChecked(checked, animated);
        return this;
    }
}