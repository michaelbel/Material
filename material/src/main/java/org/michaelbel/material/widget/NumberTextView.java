package org.michaelbel.material.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import org.michaelbel.material.Utils;

import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class NumberTextView extends View {

    private static final int ANIMATION_DURATION = 150;

    private int currentNumber = 1;
    private float progress = 0.0f;

    private ObjectAnimator animator;
    private ArrayList<StaticLayout> letters = new ArrayList<>();
    private ArrayList<StaticLayout> oldLetters = new ArrayList<>();
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public NumberTextView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public NumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public NumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        Utils.bind(context);
    }

    public void setProgress(float value) {
        if (progress == value) {
            return;
        }

        progress = value;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setNumber(int number) {
        setNumber(number, true);
    }

    public void setNumber(int number, boolean animated) {
        if (currentNumber == number && animated) {
            return;
        }

        if (animator != null) {
            animator.cancel();
            animator = null;
        }

        oldLetters.clear();
        oldLetters.addAll(letters);
        letters.clear();
        String oldText = String.format(Locale.US, "%d", currentNumber);
        String text = String.format(Locale.US, "%d", number);
        boolean forwardAnimation = number > currentNumber;
        currentNumber = number;
        progress = 0;

        for (int a = 0; a < text.length(); a++) {
            String ch = text.substring(a, a + 1);
            String oldCh = !oldLetters.isEmpty() && a < oldText.length() ? oldText.substring(a, a + 1) : null;

            if (oldCh != null && oldCh.equals(ch)) {
                letters.add(oldLetters.get(a));
                oldLetters.set(a, null);
            } else {
                StaticLayout layout = new StaticLayout(ch, textPaint, (int) Math.ceil(textPaint.measureText(ch)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                letters.add(layout);
            }
        }

        if (animated && !oldLetters.isEmpty()) {
            animator = ObjectAnimator.ofFloat(this, "progress", forwardAnimation ? -1 : 1, 0);
            animator.setDuration(ANIMATION_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animator = null;
                    oldLetters.clear();
                }
            });
            animator.start();
        }
        invalidate();
    }

    public void setTextSize(int size) {
        textPaint.setTextSize(size);
        oldLetters.clear();
        letters.clear();
        setNumber(currentNumber, false);
    }

    public void setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setTypeface(@NonNull Typeface typeface) {
        textPaint.setTypeface(typeface);
        oldLetters.clear();
        letters.clear();
        setNumber(currentNumber, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (letters.isEmpty()) {
            return;
        }

        float height = letters.get(0).getHeight();
        canvas.save();
        canvas.translate(getPaddingLeft(), (getMeasuredHeight() - height) / 2);
        int count = Math.max(letters.size(), oldLetters.size());

        for (int a = 0; a < count; a++) {
            canvas.save();
            StaticLayout old = a < oldLetters.size() ? oldLetters.get(a) : null;
            StaticLayout layout = a < letters.size() ? letters.get(a) : null;

            if (progress > 0) {
                if (old != null) {
                    textPaint.setAlpha((int) (255 * progress));
                    canvas.save();
                    canvas.translate(0, (progress - 1.0f) * height);
                    old.draw(canvas);
                    canvas.restore();
                    if (layout != null) {
                        textPaint.setAlpha((int) (255 * (1.0f - progress)));
                        canvas.translate(0, progress * height);
                    }
                } else {
                    textPaint.setAlpha(255);
                }
            } else if (progress < 0) {
                if (old != null) {
                    textPaint.setAlpha((int) (255 * -progress));
                    canvas.save();
                    canvas.translate(0, (1.0f + progress) * height);
                    old.draw(canvas);
                    canvas.restore();
                }

                if (layout != null) {
                    if (a == count - 1 || old != null) {
                        textPaint.setAlpha((int) (255 * (1.0f + progress)));
                        canvas.translate(0, progress * height);
                    } else {
                        textPaint.setAlpha(255);
                    }
                }
            } else if (layout != null) {
                textPaint.setAlpha(255);
            }

            if (layout != null) {
                layout.draw(canvas);
            }

            canvas.restore();
            if (layout != null) canvas.translate(layout.getLineWidth(0), 0);
            else canvas.translate((old != null ? old.getLineWidth(0) : 0) + Utils.dp(getContext(), 1), 0);
        }

        canvas.restore();
    }
}