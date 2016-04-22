package org.app.application.cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class CardCell extends CardView {

    private TextView textView;
    private TextView valueTextView;
    private ImageView valueImageView;
    private ImageView optionsButton;

    public CardCell(Context context) {
        super(context);

        FrameLayout.LayoutParams params = LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT);
        params.setMargins(AndroidUtilities.dp(6), AndroidUtilities.dp(8), AndroidUtilities.dp(6), 0);

        this.setElevation(5);
        this.setPreventCornerOverlap(false);
        this.setLayoutParams(params);
        this.setBackground(AndroidUtilities.getRipple(0xFFFFFFFF, 0xFFE0E0E0));

        textView = new TextView(context);
        textView.setVisibility(INVISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTextColor(0xFF444444);
        textView.setGravity(Gravity.START);
        textView.setPadding(AndroidUtilities.dp(72), AndroidUtilities.dp(16), AndroidUtilities.dp(72), AndroidUtilities.dp(16));
        addView(textView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        valueTextView = new TextView(context);
        valueTextView.setVisibility(INVISIBLE);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        valueTextView.setTextColor(0xFFBDBDBD);
        valueTextView.setGravity(Gravity.START);
        valueTextView.setPadding(AndroidUtilities.dp(72), AndroidUtilities.dp(48), AndroidUtilities.dp(16), AndroidUtilities.dp(16));
        addView(valueTextView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START));

        valueImageView = new ImageView(context);
        valueImageView.setVisibility(INVISIBLE);
        valueImageView.setFocusable(false);
        valueImageView.setScaleType(ImageView.ScaleType.CENTER);
        valueImageView.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
        addView(valueImageView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));

        optionsButton = new ImageView(context);
        optionsButton.setVisibility(INVISIBLE);
        optionsButton.setFocusable(false);
        optionsButton.setBackgroundResource(R.drawable.ripple);
        optionsButton.setScaleType(ImageView.ScaleType.CENTER);
        optionsButton.setPadding(0, AndroidUtilities.dp(10), AndroidUtilities.dp(8), 0);
        addView(optionsButton, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP));

    }

    public CardCell addTitle(String title) {
        textView.setVisibility(VISIBLE);
        textView.setText(title);
        return this;
    }

    public CardCell addValue(String value) {
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        return this;
    }

    public CardCell addImage(int image) {
        valueImageView.setVisibility(VISIBLE);
        valueImageView.setImageResource(image);
        return this;
    }

    public CardCell addImage(Drawable image) {
        valueImageView.setVisibility(VISIBLE);
        valueImageView.setImageDrawable(image);
        return this;
    }

    public CardCell addOptionButton(Drawable optionButton) {
        optionsButton.setVisibility(VISIBLE);
        optionsButton.setImageDrawable(optionButton);
        return this;
    }

    public void setOnOptionsClick(OnClickListener listener) {
        optionsButton.setOnClickListener(listener);
    }
}
