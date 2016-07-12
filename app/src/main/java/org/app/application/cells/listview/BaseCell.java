package org.app.application.cells.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.FrameLayout;

import org.app.material.AndroidUtilities;

public class BaseCell extends FrameLayout {

    private boolean divider;
    private static Paint paint;
    private int mHeight = 52;

    public BaseCell(Context context) {
        super(context);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(0xffd9d9d9);
            paint.setStrokeWidth(1);
        }
    }

    public BaseCell withBackgroundColor(int color) {
        this.setBackgroundColor(color);
        return this;
    }

    public BaseCell withDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public BaseCell withHeight(int height) {
        this.mHeight = height;
        return this;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(mHeight + (divider ? 1 : 0)), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (divider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
        }
    }
}