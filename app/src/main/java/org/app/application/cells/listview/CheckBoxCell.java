package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.widget.CheckBox;
import org.app.material.widget.LayoutHelper;

public class CheckBoxCell extends BaseCell {

    private TextView mTextView;
    private CheckBox mCheckBox;

    public CheckBoxCell(Context context) {
        super(context);

        mCheckBox = new CheckBox(context);
        mCheckBox.setLayoutParams(LayoutHelper.makeFrame(context, 20, 20, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(mCheckBox);

        mTextView = new TextView(context);
        mTextView.setTextColor(0xFF333333);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 52, 0, 0, 0));
        addView(mTextView);
    }

    @Override
    public void setBackgroundColor(int color) {
        color = 0xFFFFFFFF;
        super.setBackgroundColor(color);
    }

    public CheckBoxCell withText(String text) {
        mTextView.setText(text);
        return this;
    }

    public CheckBoxCell withText(@StringRes int stringId) {
        withText(getResources().getString(stringId));
        return this;
    }

    public CheckBoxCell withChecked(boolean checked, boolean animated) {
        mCheckBox.setChecked(checked, animated);
        return this;
    }
}