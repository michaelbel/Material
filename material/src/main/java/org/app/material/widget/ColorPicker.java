package org.app.material.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rey.material.widget.Slider;

import org.app.material.AndroidUtilities;
import org.app.material.SeekBar;

@Deprecated
public class ColorPicker extends FrameLayout implements Slider.OnPositionChangeListener {

    private View mColorView;

    private TextView mTextViewRed;
    private TextView mTextViewGreen;
    private TextView mTextViewBlue;

    private SeekBar mSeekBarRed;
    private SeekBar mSeekBarGreen;
    private SeekBar mSeekBarBlue;

    private int mRedColor = 0;
    private int mGreenColor = 0;
    private int mBlueColor = 0;

    private int newColor;

    public ColorPicker(Context context) {
        super(context);

        mColorView = new View(context);
        mColorView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 80, Gravity.TOP));
        //if (AndroidUtilities.isLandscape(context)) {
        //    mColorView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 80, Gravity.START));
        //}
        addView(mColorView);

        mTextViewRed = new TextView(context);
        mTextViewRed.setText(String.format("%d", mRedColor));
        mTextViewRed.setSingleLine(true);
        mTextViewRed.setTextColor(0xFFF44336);
        //mTextViewRed.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mTextViewRed.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mTextViewRed.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mTextViewRed.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        mTextViewRed.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.START, 0, 123, 0, 0));
        addView(mTextViewRed);

        mTextViewGreen = new TextView(context);
        mTextViewGreen.setSingleLine(true);
        //mTextViewGreen.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mTextViewGreen.setText(String.format("%d", mGreenColor));
        mTextViewGreen.setTextColor(0xFF4CAF50);
        mTextViewGreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mTextViewGreen.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mTextViewGreen.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        mTextViewGreen.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.START, 0, 163, 0, 0));
        addView(mTextViewGreen);

        mTextViewBlue = new TextView(context);
        mTextViewBlue.setText(String.format("%d", mBlueColor));
        mTextViewBlue.setSingleLine(true);
        //mTextViewBlue.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mTextViewBlue.setTextColor(0xFF2196F3);
        mTextViewBlue.setTypeface(Typeface.SERIF);
        mTextViewBlue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mTextViewBlue.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mTextViewBlue.setPadding(AndroidUtilities.dp(context, 16), 0, AndroidUtilities.dp(context, 16), 0);
        mTextViewBlue.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 203, 0, 0));
        addView(mTextViewBlue);

        mSeekBarRed = new SeekBar(context);
        mSeekBarRed.setPrimaryColor(0xFFF44336);
        mSeekBarRed.setValueRange(0, 255, false);
        mSeekBarRed.setOnPositionChangeListener(this);
        mSeekBarRed.setPadding(AndroidUtilities.dp(context, 42), 0, AndroidUtilities.dp(context, 16), 0);
        mSeekBarRed.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 120, 0, 0));
        addView(mSeekBarRed);

        mSeekBarGreen = new SeekBar(context);
        mSeekBarGreen.setValueRange(0, 255, false);
        mSeekBarGreen.setPrimaryColor(0xFF4CAF50);
        mSeekBarGreen.setOnPositionChangeListener(this);
        mSeekBarGreen.setPadding(AndroidUtilities.dp(context, 42), 0, AndroidUtilities.dp(context, 16), 0);
        mSeekBarGreen.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 160, 0, 0));
        addView(mSeekBarGreen);

        mSeekBarBlue = new SeekBar(context);
        mSeekBarBlue.setPrimaryColor(0xFF2196F3);
        mSeekBarBlue.setValueRange(0, 255, false);
        mSeekBarBlue.setOnPositionChangeListener(this);
        mSeekBarBlue.setPadding(AndroidUtilities.dp(context, 42), 0, AndroidUtilities.dp(context, 16), 0);
        mSeekBarBlue.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 200, 0, 10));
        addView(mSeekBarBlue);
    }

    public ColorPicker setColor(int colorRed, int colorGreen, int colorBlue) {

        mTextViewRed.setText("" + colorRed);
        mTextViewGreen.setText("" + colorGreen);
        mTextViewBlue.setText("" + colorBlue);

        mSeekBarRed.setPosition(colorRed, false);
        mSeekBarGreen.setPosition(colorGreen, false);
        mSeekBarBlue.setPosition(colorBlue, false);

        mColorView.setBackgroundColor(Color.rgb(colorRed, colorGreen, colorBlue));
        newColor = Color.rgb(colorRed, colorGreen, colorBlue);
        return this;
    }

    public int getNewColor() {
        return newColor;
    }

    @Override
    public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
        if (view == mSeekBarRed) {
            mRedColor = newValue;
            mTextViewRed.setText("" + mRedColor);
        } else if (view == mSeekBarGreen) {
            mGreenColor = newValue;
            mTextViewGreen.setText("" + mGreenColor);
        } else if (view == mSeekBarBlue) {
            mBlueColor = newValue;
            mTextViewBlue.setText("" + mBlueColor);
        }

        newColor = Color.rgb(mRedColor, mGreenColor, mBlueColor);
        mColorView.setBackgroundColor(Color.rgb(mRedColor, mGreenColor, mBlueColor));
    }
}