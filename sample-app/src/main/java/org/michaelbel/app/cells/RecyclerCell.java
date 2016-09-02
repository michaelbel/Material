package org.michaelbel.app.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.michaelbel.app.R;
import org.michaelbel.material.Utils;
import org.michaelbel.material.widget.AvatarImageView;
import org.michaelbel.material.widget.LayoutHelper;

public class RecyclerCell extends FrameLayout {

    private TextView textView1;
    private TextView textView2;
    private ImageView optionButton;
    private AvatarImageView imageView;

    private Paint paint;
    private boolean divider = false;

    private Rect rect = new Rect();

    public RecyclerCell(Context context) {
        super(context);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(0xFFD9D9D9);
        }

        imageView = new AvatarImageView(context);
        imageView.setFocusable(false);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setShapeDrawable(AvatarImageView.CIRCLE);
        imageView.setLayoutParams(LayoutHelper.makeFrame(context, 48, 48, Gravity.START |
                Gravity.CENTER_VERTICAL, 16, 0, 16, 0));
        addView(imageView);

        textView1 = new TextView(context);
        textView1.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        textView1.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 80, 10, 21, 0));
        addView(textView1);

        textView2 = new TextView(context);
        textView2.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView2.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 80, 0, 21, 10));
        addView(textView2);

        optionButton = new ImageView(context);
        optionButton.setFocusable(false);
        optionButton.setScaleType(ImageView.ScaleType.CENTER);
        optionButton.setBackgroundResource(Utils.selectableItemBackgroundBorderless(context));
        optionButton.setImageDrawable(Utils.getIcon(R.drawable.ic_dots_menu,
                ContextCompat.getColor(context, R.color.textColorSecondary)));
        optionButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 5, 5, 5, 5));
        addView(optionButton);
    }

    public RecyclerCell setImage(int image) {
        imageView.setImageResource(image);
        return this;
    }

    public RecyclerCell setText1(String text) {
        textView1.setText(text);
        return this;
    }

    public RecyclerCell setText2 (String text) {
        textView2.setText(text);
        return this;
    }

    public RecyclerCell setDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public void setOnOptionsClick(OnClickListener listener) {
        optionButton.setOnClickListener(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (divider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(),
                    getHeight() - 1, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(Utils.dp(64) + (divider ? 1 : 0),
                        MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
            optionButton.getHitRect(rect);

            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() ==
                    MotionEvent.ACTION_MOVE) {
                getBackground().setHotspot(event.getX(), event.getY());
            }
        }

        return super.onTouchEvent(event);
    }
}