package org.app.application.cells.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.AndroidUtilities;

public class BaseCell extends FrameLayout {

    private boolean divider;
    private int mHeight = 52;
    private static Paint paint;

    public BaseCell(Context context) {
        super(context);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(ContextCompat.getColor(context, R.color.dividerColor));
        }
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(mHeight + (divider ? 1 : 0)),
                        MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (divider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(),
                    getHeight() - 1, paint);
        }
    }
}