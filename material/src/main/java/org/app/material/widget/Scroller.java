package org.app.material.widget;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class Scroller  {

    private int mode;
    private int startX;
    private int startY;
    private int finalX;
    private int finalY;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int currY;
    private int duration;
    private long startTime;
    private float durationReciprocal;
    private float velocity;
    private float deltaY;
    private float deceleration;
    private boolean finished;
    private boolean flywheel;
    private final float ppi;
    private static float sViscousFluidScale;
    private static float sViscousFluidNormalize;
    private static final int SCROLL_MODE = 0;
    private static final int FLING_MODE = 1;
    private static final int NB_SAMPLES = 100;
    private static float DECELERATION_RATE = (float) (Math.log(0.75) / Math.log(0.9));
    private static float START_TENSION = 0.4f;
    private static float END_TENSION = 1.0f - START_TENSION;
    private static final float[] SPLINE = new float[NB_SAMPLES + 1];

    private Interpolator interpolator;

    static {
        float x_min = 0.0f;

        for (int i = 0; i <= NB_SAMPLES; i++) {
            final float t = (float) i / NB_SAMPLES;
            float x_max = 1.0f;
            float x, tx, coef;

            while (true) {
                x = x_min + (x_max - x_min) / 2.0f;
                coef = 3.0f * x * (1.0f - x);
                tx = coef * ((1.0f - x) * START_TENSION + x * END_TENSION) + x * x * x;

                if (Math.abs(tx - t) < 1E-5) {
                    break;
                }

                if (tx > t) {
                    x_max = x;
                } else {
                    x_min = x;
                }
            }

            final float d = coef + x * x * x;
            SPLINE[i] = d;
        }

        SPLINE[NB_SAMPLES] = 1.0f;
        sViscousFluidScale = 8.0f;
        sViscousFluidNormalize = 1.0f;
        sViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }

    public Scroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public Scroller(Context context, Interpolator interpolator, boolean flywheel) {
        finished = true;
        this.interpolator = interpolator;
        ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        deceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
        this.flywheel = flywheel;
    }
    
    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH * 39.37f * ppi * friction;
    }

    public final boolean isFinished() {
        return finished;
    }

    public final void forceFinished(boolean finished) {
        this.finished = finished;
    }

    public final int getCurrY() {
        return currY;
    }

    public float getCurrVelocity() {
        return velocity - deceleration * timePassed() / 2000.0f;
    }

    public final int getStartY() {
        return startY;
    }

    public final int getFinalY() {
        return finalY;
    }

    public boolean computeScrollOffset() {
        if (finished) {
            return false;
        }

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - startTime);
        int mCurrX;

        if (timePassed < duration) {
            switch (mode) {
            case SCROLL_MODE:
                float x = timePassed * durationReciprocal;
    
                if (interpolator == null) {
                    x = viscousFluid(x);
                } else {
                    x = interpolator.getInterpolation(x);
                }

                currY = startY + Math.round(x * deltaY);
                break;
            case FLING_MODE:
                final float t = (float) timePassed / duration;
                final int index = (int) (NB_SAMPLES * t);
                final float t_inf = (float) index / NB_SAMPLES;
                final float t_sup = (float) (index + 1) / NB_SAMPLES;
                final float d_inf = SPLINE[index];
                final float d_sup = SPLINE[index + 1];
                final float distanceCoef = d_inf + (t - t_inf) / (t_sup - t_inf) * (d_sup - d_inf);

                mCurrX = startX + Math.round(distanceCoef * (finalX - startX));
                mCurrX = Math.min(mCurrX, maxX);
                mCurrX = Math.max(mCurrX, minX);

                currY = startY + Math.round(distanceCoef * (finalY - startY));
                currY = Math.min(currY, maxY);
                currY = Math.max(currY, minY);

                if (mCurrX == finalX && currY == finalY) {
                    finished = true;
                }

                break;
            }
        }
        else {
            currY = finalY;
            finished = true;
        }
        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mode = SCROLL_MODE;
        finished = false;
        this.duration = duration;
        startTime = AnimationUtils.currentAnimationTimeMillis();
        this.startX = startX;
        this.startY = startY;
        finalX = startX + dx;
        finalY = startY + dy;
        deltaY = dy;
        durationReciprocal = 1.0f / (float) this.duration;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        if (flywheel && !finished) {
            float oldVel = getCurrVelocity();

            float dx = (float) (finalX - this.startX);
            float dy = (float) (finalY - this.startY);
            float hyp = (float) Math.sqrt(dx * dx + dy * dy);

            float ndx = dx / hyp;
            float ndy = dy / hyp;

            float oldVelocityX = ndx * oldVel;
            float oldVelocityY = ndy * oldVel;

            if (Math.signum(velocityX) == Math.signum(oldVelocityX) && Math.signum(velocityY) == Math.signum(oldVelocityY)) {
                velocityX += oldVelocityX;
                velocityY += oldVelocityY;
            }
        }

        mode = FLING_MODE;
        finished = false;
        float velocity = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        this.velocity = velocity;
        float ALPHA = 800;
        final double l = Math.log(START_TENSION * velocity / ALPHA);

        duration = (int) (1000.0 * Math.exp(l / (DECELERATION_RATE - 1.0)));
        startTime = AnimationUtils.currentAnimationTimeMillis();

        this.startX = startX;
        this.startY = startY;

        float coeffX = velocity == 0 ? 1.0f : velocityX / velocity;
        float coeffY = velocity == 0 ? 1.0f : velocityY / velocity;

        int totalDistance = (int) (ALPHA * Math.exp(DECELERATION_RATE / (DECELERATION_RATE - 1.0) * l));

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        finalX = startX + Math.round(totalDistance * coeffX);
        finalX = Math.min(finalX, this.maxX);
        finalX = Math.max(finalX, this.minX);
        finalY = startY + Math.round(totalDistance * coeffY);
        finalY = Math.min(finalY, this.maxY);
        finalY = Math.max(finalY, this.minY);
    }
    
    static float viscousFluid(float x) {
        x *= sViscousFluidScale;

        if (x < 1.0f) {
            x -= (1.0f - (float)Math.exp(-x));
        } else {
            float start = 0.36787944117f;
            x = 1.0f - (float)Math.exp(1.0f - x);
            x = start + x * (1.0f - start);
        }

        x *= sViscousFluidNormalize;
        return x;
    }

    public int timePassed() {
        return (int)(AnimationUtils.currentAnimationTimeMillis() - startTime);
    }
}