package org.michaelbel.material.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.michaelbel.material.R;
import org.michaelbel.material.util.Utils;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class BottomPickerLayout extends FrameLayout {

    private int mPickerHeight;
    private float mPickerElevation;
    protected TextView mNegativeButton;
    protected TextView mPositiveButton;
    private OnClickListener positiveButtonListener;
    private OnClickListener negativeButtonListener;

    public interface OnClickListener {
        void onClick(View v);
    }

    public BottomPickerLayout(Context context) {
        super(context);

        mPickerHeight = 48;
        mPickerElevation = 8;

        setElevation(Utils.dp(context, mPickerElevation));

        mNegativeButton = new TextView(context);
        mNegativeButton.setClickable(true);
        mNegativeButton.setGravity(Gravity.CENTER);
        mNegativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mNegativeButton.setTextColor(Utils.getAttrColor(context, R.attr.colorPrimary));
        mNegativeButton.setPadding(Utils.dp(context, 32), 0, Utils.dp(context, 32), 0);
        mNegativeButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mNegativeButton.setBackgroundResource(Utils.selectableItemBackgroundBorderless(context));
        mNegativeButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mNegativeButton);

        mPositiveButton = new TextView(context);
        mPositiveButton.setClickable(true);
        mPositiveButton.setGravity(Gravity.CENTER);
        mPositiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mPositiveButton.setTextColor(Utils.getAttrColor(context, R.attr.colorPrimary));
        mPositiveButton.setPadding(Utils.dp(context, 32), 0, Utils.dp(context, 32), 0);
        mPositiveButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mPositiveButton.setBackgroundResource(Utils.selectableItemBackgroundBorderless(context));
        mPositiveButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));
        addView(mPositiveButton);
    }

    public BottomPickerLayout setNegativeButton(@NonNull String text, OnClickListener listener) {
        negativeButtonListener = listener;

        mNegativeButton.setText(text.toUpperCase());
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeButtonListener != null) {
                    negativeButtonListener.onClick(v);
                }
            }
        });

        return this;
    }

    public BottomPickerLayout setNegativeButton(@StringRes int stringId, OnClickListener listener) {
        setNegativeButton(getContext().getString(stringId), listener);
        return this;
    }

    public BottomPickerLayout setPositiveButton(@NonNull String text, OnClickListener listener) {
        positiveButtonListener = listener;

        mPositiveButton.setText(text.toUpperCase());
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(v);
                }
            }
        });

        return this;
    }

    public BottomPickerLayout setPositiveButton(@StringRes int stringId, OnClickListener listener) {
        setPositiveButton(getContext().getString(stringId), listener);
        return this;
    }

    public BottomPickerLayout setHeight(int height) {
        mPickerHeight = height;
        return this;
    }

    public BottomPickerLayout setElevation(int elevation) {
        mPickerElevation = elevation;
        return this;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), mPickerHeight), MeasureSpec.EXACTLY));
    }
}