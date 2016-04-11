package org.app.material.cell;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;

public class EmptyCell extends FrameLayout {

    private TextView headText;
    private TextView noteText;

    public EmptyCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        this.setLayoutParams(params);

        headText = new TextView(context);
        headText.setVisibility(INVISIBLE);
        headText.setGravity(Gravity.START);
        headText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        headText.setTypeface(AndroidUtilities.getTypeface("medium.ttf"));
        headText.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(12), AndroidUtilities.dp(16), AndroidUtilities.dp(12));
        addView(headText, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        noteText = new TextView(context);
        headText.setVisibility(INVISIBLE);
        noteText.setGravity(Gravity.START);
        noteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        noteText.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(12), AndroidUtilities.dp(16), AndroidUtilities.dp(12));
        addView(noteText, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
    }

    public EmptyCell addHead(String text, boolean toUpperCase) {
        noteText.setVisibility(INVISIBLE);

        headText.setVisibility(VISIBLE);

        if (toUpperCase) {
            headText.setText(text.toUpperCase());
        } else {
            headText.setText(text);
        }

        return this;
    }

    public EmptyCell addNote(String text) {
        headText.setVisibility(INVISIBLE);

        noteText.setVisibility(VISIBLE);
        noteText.setText(text);
        return this;
    }
}