package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Switch;

public class SwitchCell extends BaseCell {

    private Switch mSwitch;
    private TextView textView;

    public SwitchCell(Context context) {
        super(context);

        setBackgroundColor(0xFFFFFFFF);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(textView);

        mSwitch = new Switch(context);
        mSwitch.setClickable(false);
        mSwitch.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mSwitch);
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
        mSwitch.setChecked(checked);
        return this;
    }
}