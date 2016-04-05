package org.app.material.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import org.app.material.DimenUtil;

public class Circle extends View {

    private int color = 0xffffffff;
    private CirclePainter cPainter;
    private ShadowPainter sPainter;

    public void setColor(int color) {
        this.color = color;
        init();
    }

    public Circle(Context context) {
        super(context);
        init();
    }

    private void init() {
        sPainter = new ShadowPainter();
        cPainter = new CirclePainter(color);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sPainter.draw(canvas);
        cPainter.draw(canvas);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(DimenUtil.dp(35), DimenUtil.dp(35));
    }

    public interface Painter {
        void draw(Canvas canvas);
    }

    public class CirclePainter implements Painter {

        protected Paint paint;
        protected Paint colorPaint;
        protected int radius = DimenUtil.dp(12);
        protected int width = DimenUtil.dp(35);
        protected int height = DimenUtil.dp(35);
        protected int ballPositionX = DimenUtil.dp(16);
        private int middle = height / 2;

        public CirclePainter(int bgColor) {
            paint = new Paint();
            paint.setColor(bgColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);

            colorPaint = new Paint();
            colorPaint.setStyle(Paint.Style.FILL);
            colorPaint.setAntiAlias(true);
            colorPaint.setAlpha(0);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(ballPositionX, middle, radius, paint);
            canvas.drawCircle(ballPositionX, middle, radius, colorPaint);
        }
    }

    public class ShadowPainter extends CirclePainter {

        public ShadowPainter() {
            super(0x99000000);
            paint.setColor(0x99000000);
            paint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(ballPositionX, (height / 2) + 2, radius , paint);
        }
    }
}