package org.app.material;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

public class NumberTextView extends View {

    private int mCurrentNumber = 1;
    private float mProgress = 0.0f;
    private ObjectAnimator mAnimator;
    private ArrayList<StaticLayout> mLetters = new ArrayList<>();
    private ArrayList<StaticLayout> mOldLetters = new ArrayList<>();
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public NumberTextView(Context context) {
        super(context);

        AndroidUtilities.bind(context);
    }

    public void withProgress(float value) {
        if (mProgress == value) {
            return;
        }

        mProgress = value;
        invalidate();
    }

    public float getProgress() {
        return mProgress;
    }

    public void withNumber(int number, boolean animated) {
        if (mCurrentNumber == number && animated) {
            return;
        }

        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        mOldLetters.clear();
        mOldLetters.addAll(mLetters);
        mLetters.clear();

        String text = String.format(Locale.US, "%d", number);
        String oldText = String.format(Locale.US, "%d", mCurrentNumber);

        boolean forwardAnimation = number > mCurrentNumber;

        mCurrentNumber = number;
        mProgress = 0;

        for (int a = 0; a < text.length(); a++) {
            String ch = text.substring(a, a + 1);
            String oldCh = !mOldLetters.isEmpty() && a < oldText.length() ? oldText.substring(a, a + 1) : null;

            if (oldCh != null && oldCh.equals(ch)) {
                mLetters.add(mOldLetters.get(a));
                mOldLetters.set(a, null);
            } else {
                StaticLayout layout = new StaticLayout(ch, mTextPaint, (int) Math.ceil(mTextPaint.measureText(ch)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                mLetters.add(layout);
            }
        }

        if (animated && !mOldLetters.isEmpty()) {
            mAnimator = ObjectAnimator.ofFloat(this, "progress", forwardAnimation ? -1 : 1, 0);
            mAnimator.setDuration(150);
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimator = null;
                    mOldLetters.clear();
                }
            });

            mAnimator.start();
        }

        invalidate();
    }

    public void withTextSize(int size) {
        mTextPaint.setTextSize(AndroidUtilities.dp(size));
        mOldLetters.clear();
        mLetters.clear();

        withNumber(mCurrentNumber, false);
    }

    public void withTextColor(int value) {
        mTextPaint.setColor(value);
        invalidate();
    }

    public void withTypeface(Typeface typeface) {
        mTextPaint.setTypeface(typeface);
        mOldLetters.clear();
        mLetters.clear();
        withNumber(mCurrentNumber, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLetters.isEmpty()) {
            return;
        }

        float height = mLetters.get(0).getHeight();
        canvas.save();
        canvas.translate(getPaddingLeft(), (getMeasuredHeight() - height) / 2);
        int count = Math.max(mLetters.size(), mOldLetters.size());

        for (int a = 0; a < count; a++) {
            canvas.save();
            StaticLayout old = a < mOldLetters.size() ? mOldLetters.get(a) : null;
            StaticLayout layout = a < mLetters.size() ? mLetters.get(a) : null;

            if (mProgress > 0) {
                if (old != null) {
                    mTextPaint.setAlpha((int) (255 * mProgress));
                    canvas.save();
                    canvas.translate(0, (mProgress - 1.0f) * height);
                    old.draw(canvas);
                    canvas.restore();

                    if (layout != null) {
                        mTextPaint.setAlpha((int) (255 * (1.0f - mProgress)));
                        canvas.translate(0, mProgress * height);
                    }
                } else {
                    mTextPaint.setAlpha(255);
                }
            } else if (mProgress < 0) {
                if (old != null) {
                    mTextPaint.setAlpha((int) (255 * - mProgress));
                    canvas.save();
                    canvas.translate(0, (1.0f + mProgress) * height);
                    old.draw(canvas);
                    canvas.restore();
                }

                if (layout != null) {
                    if (a == count - 1 || old != null) {
                        mTextPaint.setAlpha((int) (255 * (1.0f + mProgress)));
                        canvas.translate(0, mProgress * height);
                    } else {
                        mTextPaint.setAlpha(255);
                    }
                }
            } else if (layout != null) {
                mTextPaint.setAlpha(255);
            }

            if (layout != null) {
                layout.draw(canvas);
            }

            canvas.restore();
            canvas.translate(layout != null ? layout.getLineWidth(0) : old.getLineWidth(0) + AndroidUtilities.dp(1), 0);
        }

        canvas.restore();
    }
}