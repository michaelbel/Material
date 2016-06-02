package org.app.material.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

public class BottomPickerLayout extends FrameLayout {

    private TextView mNegativeButton;
    private TextView mPositiveButton;
    private ViewPropertyAnimatorCompat mTranslationAnimator;

    //private org.app.material.OnClickListener positiveButtonListener;
    //private org.app.material.OnClickListener negativeButtonListener;

    private ClickListener positiveButtonListener;
    private ClickListener negativeButtonListener;

    public interface ClickListener {
        void onClick(View v);
    }

    public BottomPickerLayout(Context context) {
        super(context);

        this.setBackgroundColor(0xFFFFFFFF);
        this.setElevation(AndroidUtilities.dp(context, 10));

        mNegativeButton = new TextView(context);
        mNegativeButton.setGravity(Gravity.CENTER);
        mNegativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mNegativeButton.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mNegativeButton.setTextColor(AndroidUtilities.getContextColor(context, R.attr.colorPrimary));
        mNegativeButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        mNegativeButton.setPadding(AndroidUtilities.dp(context, 32), 0, AndroidUtilities.dp(context, 32), 0);
        addView(mNegativeButton, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        mPositiveButton = new TextView(context);
        mPositiveButton.setGravity(Gravity.CENTER);
        mPositiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mPositiveButton.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
        mPositiveButton.setTextColor(AndroidUtilities.getContextColor(context, R.attr.colorPrimary));
        mPositiveButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless(context));
        mPositiveButton.setPadding(AndroidUtilities.dp(context, 32), 0, AndroidUtilities.dp(context, 32), 0);
        addView(mPositiveButton, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));
    }

    public BottomPickerLayout setNegativeButton(@StringRes int resId, ClickListener listener) {
        mNegativeButton.setText(getResources().getString(resId).toUpperCase());
        mNegativeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeButtonListener != null) {
                    negativeButtonListener.onClick(v);
                }
            }
        });
        return this;
    }

    public BottomPickerLayout setPositiveButton(@StringRes int resId, ClickListener listener) {
        mPositiveButton.setText(getResources().getString(resId).toUpperCase());
        mPositiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(v);
                }
            }
        });
        return this;
    }

    public BottomPickerLayout hide() {
        hide(true);
        return this;
    }

    public BottomPickerLayout show() {
        show(true);
        return this;
    }

    public BottomPickerLayout hide(boolean animate) {
        setTranslationY(this.getHeight(), animate);
        return this;
    }

    public BottomPickerLayout show(boolean animate) {
        setTranslationY(0, animate);
        return this;
    }

    private void setTranslationY(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }

            this.setTranslationY(offset);
        }
    }

    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(400);
            mTranslationAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        } else {
            mTranslationAnimator.cancel();
        }

        mTranslationAnimator.translationY(offset).start();
    }
}