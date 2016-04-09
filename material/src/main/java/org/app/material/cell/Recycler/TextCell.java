package org.app.material.cell.Recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.LayoutHelper;

public class TextCell extends FrameLayout {

    private TextView textView;
    private TextView valueTextView;
    private ImageView valueImageView;
    private RadioButton radioButton;
    private CheckBox checkBox;
    private Switch switchCompat;

    public TextCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 0.3f);

        this.setElevation(4);
        this.setLayoutParams(params);
        this.setBackground(AndroidUtilities.getRipple(0xFFFFFFFF, 0xFFE0E0E0));

        textView = new TextView(context);
        textView.setVisibility(INVISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xFF444444);
        textView.setGravity(Gravity.START);
        textView.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16));
        addView(textView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        valueTextView = new TextView(context);
        valueTextView.setVisibility(INVISIBLE);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        valueTextView.setTextColor(0xFFBDBDBD);
        valueTextView.setGravity(Gravity.END);
        valueTextView.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16));
        addView(valueTextView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END));

        valueImageView = new ImageView(context);
        valueImageView.setVisibility(INVISIBLE);
        valueImageView.setScaleType(ImageView.ScaleType.CENTER);
        valueImageView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(valueImageView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        radioButton = new RadioButton(context);
        radioButton.setClickable(false);
        radioButton.setFocusable(false);
        //radioButton.setFocusableInTouchMode(false);
        radioButton.setVisibility(INVISIBLE);
        radioButton.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        radioButton.setPadding(AndroidUtilities.dp(6), 0, AndroidUtilities.dp(6), 0);
        addView(radioButton, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        checkBox = new CheckBox(context);
        checkBox.setClickable(false);
        checkBox.setFocusable(false);
        //checkBox.setFocusableInTouchMode(false);
        checkBox.setVisibility(INVISIBLE);
        checkBox.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        checkBox.setPadding(AndroidUtilities.dp(7), 0, AndroidUtilities.dp(7), 0);
        addView(checkBox, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));

        switchCompat = new Switch(context);
        switchCompat.setClickable(false);
        switchCompat.setFocusable(false);
        //switchCompat.setFocusableInTouchMode(false);
        switchCompat.setVisibility(INVISIBLE);
        switchCompat.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        switchCompat.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), 0);
        addView(switchCompat, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));
    }

    public TextCell addTitle(String title) {
        valueTextView.setVisibility(INVISIBLE);
        valueImageView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        textView.setVisibility(VISIBLE);
        textView.setText(title);
        return this;
    }

    public TextCell addValue(String value) {
        valueImageView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        return this;
    }

    public TextCell addImage(int image) {
        valueTextView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        valueImageView.setVisibility(VISIBLE);
        valueImageView.setImageResource(image);
        return this;
    }

    public TextCell addImage(Drawable image) {
        valueTextView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        valueImageView.setVisibility(VISIBLE);
        valueImageView.setImageDrawable(image);
        return this;
    }

    public TextCell addRadio(boolean checked) {
        valueTextView.setVisibility(INVISIBLE);
        valueImageView.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        radioButton.setVisibility(VISIBLE);
        radioButton.setChecked(checked);
        return this;
    }

    public TextCell addCheckBox(boolean checked) {
        valueTextView.setVisibility(INVISIBLE);
        valueImageView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        switchCompat.setVisibility(INVISIBLE);

        checkBox.setVisibility(VISIBLE);
        checkBox.setChecked(checked);
        return this;
    }

    public TextCell addSwitch(boolean checked) {
        valueTextView.setVisibility(INVISIBLE);
        valueImageView.setVisibility(INVISIBLE);
        radioButton.setVisibility(INVISIBLE);
        checkBox.setVisibility(INVISIBLE);

        switchCompat.setVisibility(VISIBLE);
        switchCompat.setChecked(checked);
        return this;
    }
}