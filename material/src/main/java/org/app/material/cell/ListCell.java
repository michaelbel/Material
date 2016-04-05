package org.app.material.cell;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.material.widget.Circle;
import org.app.material.DimenUtil;

public class ListCell extends FrameLayout {

    private View listDivider;
    private View listShortDivider;

    private Circle circle;
    private TextView textView;
    private TextView valueText;
    private ImageView iconView;
    //private RadioButton radioButton;

    public ListCell(Context context) {
        super(context);

        //this.setBackgroundColor(AppController.isTheme ? Colors.viewLight : Colors.viewDark);
        //this.setTextDirection(LocaleController.isRTL ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);

        circle = new Circle(context);
        circle.setVisibility(INVISIBLE);
        //addView(circle, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, LocaleController.isRTL ? 12 : 0, 0, LocaleController.isRTL ? 0 : 12, 0, (LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER_VERTICAL));

        iconView = new ImageView(context);
        iconView.setVisibility(INVISIBLE);
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        //iconView.setPadding(LocaleController.isRTL ? 0 : AppController.dp(16), 0, LocaleController.isRTL ? AppController.dp(16) : 0, 0);
        //addView(iconView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.END : Gravity.START) | Gravity.CENTER_VERTICAL));

        textView = new TextView(context);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setVisibility(INVISIBLE);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        //textView.setTextColor(AppController.isTheme ? Colors.textLight : Colors.textDark);
        //textView.setGravity((LocaleController.isRTL ? Gravity.END : Gravity.START) | Gravity.CENTER_VERTICAL);
        //textView.setPadding(LocaleController.isRTL ? 0 : AppController.dp(16), 0, LocaleController.isRTL ? AppController.dp(16) : 0, 0);
        //addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.END : Gravity.START) | Gravity.CENTER_VERTICAL));

        valueText = new TextView(context);
        valueText.setLines(1);
        valueText.setMaxLines(1);
        valueText.setSingleLine(true);
        valueText.setVisibility(INVISIBLE);
        valueText.setEllipsize(TextUtils.TruncateAt.END);
        valueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        //valueText.setPadding(AppController.dp(16), 0, AppController.dp(16), 0);
        //valueText.setGravity((LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER_VERTICAL);
        //valueText.setTextColor(AppController.isTheme ? Colors.valueLight : Colors.valueDark);
        //addView(valueText, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER_VERTICAL));

        //radioButton = new RadioButton(context);
        //radioButton.setClickable(false);
        //radioButton.setVisibility(INVISIBLE);
        //radioButton.setGravity(Gravity.CENTER_VERTICAL);
        //radioButton.setPadding(AppController.dp(16), 0, AppController.dp(16), 0);
        //addView(radioButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER));

        listDivider = new View(context);
        listDivider.setVisibility(INVISIBLE);
        //listDivider.setBackgroundColor(AppController.isTheme ? Colors.layoutLight : Colors.layoutDark);
        //addView(listDivider, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, Colors.listDividerHeight, Gravity.BOTTOM));

        listShortDivider = new View(context);
        listShortDivider.setVisibility(INVISIBLE);
        //listShortDivider.setBackgroundColor(AppController.isTheme ? Colors.layoutLight : Colors.layoutDark);
        //addView(listShortDivider, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, Colors.listDividerHeight, LocaleController.isRTL ? 0 : 70, 0, LocaleController.isRTL ? 70 : 0, 0, Gravity.BOTTOM));
    }

    public ListCell addTitle(String text) {
        textView.setVisibility(VISIBLE);
        textView.setText(text);
        return this;
    }

    public ListCell addDivider() {
        listDivider.setVisibility(VISIBLE);
        return this;
    }

    public ListCell addShortDivider() {
        listShortDivider.setVisibility(VISIBLE);
        return this;
    }

    public ListCell addCircle(int color) {
        circle.setVisibility(VISIBLE);
        circle.setColor(color);
        return this;
    }

    public ListCell addValue(String value) {
        valueText.setVisibility(VISIBLE);
        valueText.setText(value);
        return this;
    }

    public ListCell addRadio(boolean radio) {
        //radioButton.setVisibility(VISIBLE);
        //radioButton.setChecked(radio);
        return this;
    }

    public ListCell addIcon(Drawable icon) {
        iconView.setVisibility(VISIBLE);
        iconView.setImageDrawable(icon);

        //textView.setPadding(LocaleController.isRTL ? 0 : AppController.dp(72), 0, LocaleController.isRTL ? AppController.dp(72) : 0, 0);
        return this;
    }

    public ListCell addNavTitle(String text) {
        textView.setVisibility(VISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        //textView.setTypeface(AppController.getTypeface("medium.ttf"));
        textView.setTextColor(0xff464646);
        textView.setText(text);
        return this;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, MeasureSpec.makeMeasureSpec(DimenUtil.dp(52), MeasureSpec.EXACTLY));
    }
}