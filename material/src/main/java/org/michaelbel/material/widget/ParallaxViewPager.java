package org.michaelbel.material.widget;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import org.michaelbel.material.R;

import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;
import static android.graphics.drawable.GradientDrawable.Orientation.RIGHT_LEFT;

/**
 * Created: ybq
 * Date: 2014-08-15
 *
 * Updated:
 *
 * Date: 23 MAR 2018
 * Time: 22:20 MSK
 *
 * @author Michael Bel
 */

@SuppressWarnings("all")
public class ParallaxViewPager extends ViewPager {

    private Mode mMode;
    private int mShadowStart = Color.parseColor("#33000000");
    private int mShadowMid = Color.parseColor("#11000000");
    private int mShadowEnd = Color.parseColor("#00000000");

    private Drawable mRightShadow =
            new GradientDrawable(LEFT_RIGHT, new int[]{
                    mShadowStart,
                    mShadowMid,
                    mShadowEnd
            });

    private Drawable mLeftShadow =
            new GradientDrawable(RIGHT_LEFT, new int[]{
                    mShadowStart,
                    mShadowMid,
                    mShadowEnd
            });

    private int mOutset;
    private int mShadowWidth;
    private float mOutsetFraction = 0.5f;

    private Interpolator mInterpolator;
    private ParallaxTransformer mParallaxTransformer;

    public enum Mode {

        LEFT_OVERLAY(0),
        RIGHT_OVERLAY(1),
        NONE(2);

        int value;

        Mode(int value) {
            this.value = value;
        }
    }

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mParallaxTransformer = new ParallaxTransformer();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxViewPager, 0,0);
        mMode = Mode.values()[a.getInt(R.styleable.ParallaxViewPager_mode, 0)];
        setMode(mMode);

        if (a.hasValue(R.styleable.ParallaxViewPager_right_shadow)) {
            mRightShadow = a.getDrawable(R.styleable.ParallaxViewPager_right_shadow);
        }

        if (a.hasValue(R.styleable.ParallaxViewPager_left_shadow)) {
            mLeftShadow = a.getDrawable(R.styleable.ParallaxViewPager_left_shadow);
        }

        mShadowWidth = a.getDimensionPixelSize(R.styleable.ParallaxViewPager_shadow_width, (int) dp2px(20, context));
        TypedValue tv = a.peekValue(R.styleable.ParallaxViewPager_outset);

        if (tv != null) {
            if (tv.type == TypedValue.TYPE_FRACTION) {
                mOutsetFraction = a.getFraction(R.styleable.ParallaxViewPager_outset, 1, 1, 0);
                setOutsetFraction(mOutsetFraction);
            } else if (tv.type == TypedValue.TYPE_DIMENSION) {
                mOutset = (int) TypedValue.complexToDimension(tv.data, getResources().getDisplayMetrics());
                setOutset(mOutset);
            }
        }

        final int resID = a.getResourceId(R.styleable.ParallaxViewPager_interpolator, 0);

        if (resID > 0) {
            setInterpolator(context, resID);
        }

        a.recycle();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    @Override
    public void setPageMargin(int marginPixels) {
        super.setPageMargin(0);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);

        if (offset == 0) {
            int count = getChildCount();

            for (int i = 0; i < count; i++) {
                mParallaxTransformer.transformPage(getChildAt(i), 0);
            }
        }
    }

    public int getOutset() {
        return mOutset;
    }

    public void setOutset(int outset) {
        this.mOutset = outset;
        mOutsetFraction = 0;
        mParallaxTransformer.setOutset(mOutset);
    }

    public float getOutsetFraction() {
        return mOutsetFraction;
    }

    public void setOutsetFraction(float outsetFraction) {
        this.mOutsetFraction = outsetFraction;
        mOutset = 0;
        mParallaxTransformer.setOutsetFraction(mOutsetFraction);
    }

    public void setRightShadow(GradientDrawable rightShadow) {
        this.mRightShadow = rightShadow;
    }

    public void setLeftShadow(GradientDrawable leftShadow) {
        this.mLeftShadow = leftShadow;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator i) {
        mInterpolator = i;
        ensureInterpolator();
    }

    public void setInterpolator(Context context, int resID) {
        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    protected void ensureInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }

        if (mParallaxTransformer != null) {
            mParallaxTransformer.setInterpolator(mInterpolator);
        }
    }

    public void drawShadow(Canvas canvas) {
        if (mMode == Mode.NONE) {
            return;
        }

        if (getScrollX() % getWidth() == 0) {
            return;
        }

        switch (mMode) {
            case LEFT_OVERLAY:
                drawRightShadow(canvas);
                break;
            case RIGHT_OVERLAY:
                drawLeftShadow(canvas);
                break;
        }
    }

    private void drawRightShadow(Canvas canvas) {
        canvas.save();
        float translate = (getScrollX() / getWidth() + 1) * getWidth();
        canvas.translate(translate, 0);
        mRightShadow.setBounds(0, 0, mShadowWidth, getHeight());
        mRightShadow.draw(canvas);
        canvas.restore();
    }

    private void drawLeftShadow(Canvas canvas) {
        canvas.save();
        float translate = (getScrollX() / getWidth() + 1) * getWidth() - mShadowWidth;
        canvas.translate(translate, 0);
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        mLeftShadow.draw(canvas);
        canvas.restore();
    }

    private float dp2px(int dip, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dip * scale + 0.5f;
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        mMode = mode;
        mParallaxTransformer.setMode(mode);

        if (mode == Mode.LEFT_OVERLAY) {
            setPageTransformer(true, mParallaxTransformer);
        } else if (mode == Mode.RIGHT_OVERLAY) {
            setPageTransformer(false, mParallaxTransformer);
        }
    }

    public class ParallaxTransformer implements PageTransformer {

        private Mode mMode;
        private Interpolator mInterpolator = new LinearInterpolator();
        private FloatEvaluator mEvaluator;

        private int mOutset;
        private float mOutsetFraction = 0.5f;

        public ParallaxTransformer() {
            mEvaluator = new FloatEvaluator();
        }

        public Mode getMode() {
            return mMode;
        }

        public void setMode(Mode mode) {
            mMode = mode;
        }

        public Interpolator getInterpolator() {
            return mInterpolator;
        }

        public void setInterpolator(Interpolator interpolator) {
            this.mInterpolator = interpolator;
        }

        public int getOutset() {
            return mOutset;
        }

        public void setOutset(int outset) {
            this.mOutset = outset;
        }

        public void setOutsetFraction(float outsetFraction) {
            this.mOutsetFraction = outsetFraction;
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            page.setTranslationX(0);

            if (position == 0) {
                return;
            }

            switch (mMode) {
                case LEFT_OVERLAY:
                    if (position > 0) {
                        transform(page, position);
                    } else if (position < 0) {
                        bringViewToFront(page);
                    }
                    break;
                case RIGHT_OVERLAY:
                    if (position < 0) {
                        transform(page, position);
                    } else if (position > 0) {
                        bringViewToFront(page);
                    }
                    break;
                case NONE:
                    break;
            }
        }

        private void bringViewToFront(View view) {
            ViewGroup group = (ViewGroup) view.getParent();
            int index = group.indexOfChild(view);
            if (index != group.getChildCount() - 1) {
                view.bringToFront();

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    view.requestLayout();
                    group.invalidate();
                }
            }
        }

        private void transform(View page, float position) {
            float interpolatorPosition;
            float translationX;
            int pageWidth = page.getWidth();

            if (mOutset <= 0) {
                mOutset = (int) (mOutsetFraction * page.getWidth());
            }

            if (position < 0) {
                interpolatorPosition = mInterpolator.getInterpolation(Math.abs(position));
                translationX = -mEvaluator.evaluate(interpolatorPosition, 0, (pageWidth - mOutset));
            } else {
                interpolatorPosition = mInterpolator.getInterpolation(position);
                translationX = mEvaluator.evaluate(interpolatorPosition, 0, (pageWidth - mOutset));
            }

            translationX += -page.getWidth() * position;
            page.setTranslationX(translationX);
        }
    }
}