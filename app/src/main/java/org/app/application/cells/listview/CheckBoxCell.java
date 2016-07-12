package org.app.application.cells.listview;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.widget.CheckBox;
import org.app.material.widget.LayoutHelper;

public class CheckBoxCell extends BaseCell {

    private TextView mTextView;
    private CheckBox mCheckBox;

    public CheckBoxCell(Context context) {
        super(context);

        withBackgroundColor(0xFFFFFFFF);

        mTextView = new TextView(context);
        mTextView.setTextColor(0xFF333333);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTextView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        mTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mTextView);

        mCheckBox = new CheckBox(context);
        mCheckBox.setLayoutParams(LayoutHelper.makeFrame(context, 18, 18, (Gravity.END | Gravity.CENTER_VERTICAL), 16, 0, 16, 0));
        addView(mCheckBox);
    }

    public CheckBoxCell withText(@StringRes int resId) {
        mTextView.setText(getResources().getString(resId));
        return this;
    }

    public CheckBoxCell withChecked(boolean checked, boolean animated) {
        mCheckBox.setChecked(checked, animated);
        return this;
    }
}