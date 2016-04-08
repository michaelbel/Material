package org.app.material.cell;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;
import org.app.material.R;

public class NoteCell extends FrameLayout {

    private TextView noteText;
    private FrameLayout dividerLayout;

    public NoteCell(Context context) {
        super(context);

        dividerLayout = new FrameLayout(context);
        dividerLayout.setVisibility(INVISIBLE);
        dividerLayout.setBackground(AndroidUtilities.getDrawable(R.drawable.divider, 0));
        addView(dividerLayout, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        FrameLayout noteLayout = new FrameLayout(context);
        noteText = new TextView(context);
        noteText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        noteText.setGravity(Gravity.START);
        noteText.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(12), AndroidUtilities.dp(16), AndroidUtilities.dp(12));
        noteLayout.addView(noteText);
        addView(noteLayout, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
    }

    public NoteCell addDivider() {
        dividerLayout.setVisibility(VISIBLE);
        return this;
    }

    public NoteCell addNote(String text) {
        noteText.setText(text);
        return this;
    }
}