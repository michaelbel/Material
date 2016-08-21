package org.app.application.cells;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class CardCell extends CardView {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private ImageView imageView;
    private ImageView optionButton;

    private Rect rect = new Rect();

    public CardCell(Context context) {
        super(context);

        imageView = new ImageView(context);
        imageView.setFocusable(false);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 10, 0, 10, 0));
        addView(imageView);

        textView1 = new TextView(context);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        textView1.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        textView1.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 100, 17, 21, 0));
        addView(textView1);

        textView2 = new TextView(context);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView2.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));
        textView2.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 100, 0, 21, 0));
        addView(textView2);

        textView3 = new TextView(context);
        textView3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView3.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        textView3.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM, 100, 0, 21, 17));
        addView(textView3);

        optionButton = new ImageView(context);
        optionButton.setFocusable(false);
        optionButton.setScaleType(ImageView.ScaleType.CENTER);
        optionButton.setBackgroundResource(AndroidUtilities.selectableItemBackgroundBorderless());
        optionButton.setImageDrawable(AndroidUtilities.getIcon(R.drawable.ic_dots_menu,
                ContextCompat.getColor(context, R.color.textColorSecondary)));
        optionButton.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 5, 5, 5, 5));
        addView(optionButton);
    }

    public CardCell setText1(String text) {
        textView1.setText(text);
        return this;
    }

    public CardCell setText2 (String text) {
        textView2.setText(text);
        return this;
    }

    public CardCell setText3 (String text) {
        textView3.setText(text);
        return this;
    }

    public CardCell setImage(int image) {
        imageView.setImageResource(image);
        return this;
    }

    public void setOnOptionsClick(OnClickListener listener) {
        optionButton.setOnClickListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100), MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_MOVE) {
                getBackground().setHotspot(event.getX(), event.getY());
            }
        }

        return super.onTouchEvent(event);
    }
}