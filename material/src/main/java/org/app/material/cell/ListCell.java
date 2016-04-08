package org.app.material.cell;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCell extends FrameLayout {

    private View listShortDivider;
    private TextView valueText;
    private ImageView iconView;
    //private RadioButton radioButton;

    public ListCell(Context context) {
        super(context);

        //this.setBackgroundColor(AppController.isTheme ? Colors.viewLight : Colors.viewDark);
        //this.setTextDirection(LocaleController.isRTL ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);

        //circle = new Circle(context);
        //circle.setVisibility(INVISIBLE);
        //addView(circle, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, LocaleController.isRTL ? 12 : 0, 0, LocaleController.isRTL ? 0 : 12, 0, (LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER_VERTICAL));

        iconView = new ImageView(context);
        iconView.setVisibility(INVISIBLE);
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        //iconView.setPadding(LocaleController.isRTL ? 0 : AppController.dp(16), 0, LocaleController.isRTL ? AppController.dp(16) : 0, 0);
        //addView(iconView, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.END : Gravity.START) | Gravity.CENTER_VERTICAL));

        //radioButton = new RadioButton(context);
        //radioButton.setClickable(false);
        //radioButton.setVisibility(INVISIBLE);
        //radioButton.setGravity(Gravity.CENTER_VERTICAL);
        //radioButton.setPadding(AppController.dp(16), 0, AppController.dp(16), 0);
        //addView(radioButton, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.START : Gravity.END) | Gravity.CENTER));

        listShortDivider = new View(context);
        listShortDivider.setVisibility(INVISIBLE);
        //listShortDivider.setBackgroundColor(AppController.isTheme ? Colors.layoutLight : Colors.layoutDark);
        //addView(listShortDivider, LayoutHelper.makeFrame(LayoutHelper.WRAP_CONTENT, Colors.listDividerHeight, LocaleController.isRTL ? 0 : 70, 0, LocaleController.isRTL ? 70 : 0, 0, Gravity.BOTTOM));
    }

    public ListCell addShortDivider() {
        listShortDivider.setVisibility(VISIBLE);
        return this;
    }

    //public ListCell addCircle(int color) {
    //    circle.setVisibility(VISIBLE);
    //    circle.setColor(color);
    //    return this;
    //}

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
}