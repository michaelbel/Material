package org.app.application.cells.listview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.app.material.widget.LayoutHelper;

public class EmptyCell extends BaseCell {

    private TextView headerTextView;
    private boolean upperCase = false;

    public EmptyCell(Context context) {
        super(context);

        setHeight(42);

        headerTextView = new TextView(context);
        headerTextView.setGravity(Gravity.START);
        headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        headerTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        headerTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(headerTextView);
    }

    public EmptyCell setHeader(String text) {
        if (upperCase) {
            headerTextView.setText(text.toUpperCase());
        } else {
            headerTextView.setText(text);
        }

        return this;
    }

    public EmptyCell setHeader(@StringRes int stringId) {
        setHeader(getContext().getString(stringId));
        return this;
    }

    public EmptyCell setGravity(int gravity) {
        headerTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, gravity | Gravity.CENTER_VERTICAL));
        return this;
    }

    public EmptyCell setTextToUpperCase(boolean upper) {
        upperCase = upper;
        return this;
    }
}