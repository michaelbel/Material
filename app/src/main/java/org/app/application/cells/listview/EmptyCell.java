package org.app.application.cells.listview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.widget.LayoutHelper;

public class EmptyCell extends BaseCell {

    private TextView headerText;
    private boolean upperCase = false;

    public EmptyCell(Context context) {
        super(context);

        setHeight(42);

        headerText = new TextView(context);
        headerText.setGravity(Gravity.START);
        headerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        headerText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        headerText.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(headerText);
    }

    public EmptyCell setHeader(String text) {
        if (upperCase) {
            headerText.setText(text.toUpperCase());
        } else {
            headerText.setText(text);
        }

        return this;
    }

    public EmptyCell setHeader(@StringRes int stringId) {
        setHeader(getContext().getString(stringId));
        return this;
    }

    public EmptyCell setGravity(int gravity) {
        headerText.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, gravity | Gravity.CENTER_VERTICAL));
        return this;
    }

    public EmptyCell setTextToUpperCase(boolean upper) {
        upperCase = upper;
        return this;
    }
}