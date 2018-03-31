package org.michaelbel.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.michaelbel.app.R;
import org.michaelbel.material.widget2.LayoutHelper;
import org.michaelbel.material.widget2.Switch;

public class SwitchCell extends BaseCell {

    private Switch switchView;
    private TextView textView;

    public SwitchCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(textView);

        switchView = new Switch(context);
        switchView.setClickable(false);
        switchView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(switchView);
    }

    public SwitchCell setText(String text) {
        textView.setText(text);
        return this;
    }

    public SwitchCell setText(@StringRes int stringId) {
        textView.setText(getContext().getText(stringId));
        return this;
    }

    public SwitchCell setChecked(boolean checked) {
        switchView.setChecked(checked);
        return this;
    }
}