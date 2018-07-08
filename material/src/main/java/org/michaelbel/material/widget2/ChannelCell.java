package org.michaelbel.material.widget2;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.michaelbel.material.util2.Utils;
import org.michaelbel.material.widget.LayoutHelper;

public class ChannelCell extends LinearLayout {

    private TextView labelTextView;
    private TextView progressTextView;
    private SeekBar seekBar;

    public ChannelCell(Context context) {
        super(context);

        labelTextView = new TextView(context);
        labelTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        labelTextView.setPadding(Utils.dp(context, 16), 0, Utils.dp(context, 16), 0);
        labelTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER_HORIZONTAL));
        addView(labelTextView);

        seekBar = new SeekBar(context);
        seekBar.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER_HORIZONTAL));
        addView(seekBar);

        progressTextView = new TextView(context);
        progressTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        progressTextView.setPadding(Utils.dp(context, 16), 0, Utils.dp(context, 16), 0);
        progressTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER_HORIZONTAL));
        addView(progressTextView);
    }
}