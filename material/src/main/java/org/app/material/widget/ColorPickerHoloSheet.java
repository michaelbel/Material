package org.app.material.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.app.material.LayoutHelper;

public class ColorPickerHoloSheet extends ScrollView {

    public ColorPickerHolo mColorPickerHolo;
    public OpacityBar mOpacityBar;
    public SaturationBar mSaturationBar;
    public SaturationValueBar mSaturationValueBar;
    public ValueBar mValueBar;

    private LinearLayout mLayout;

    public ColorPickerHoloSheet(Context context) {
        super(context);

        mLayout = new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        mColorPickerHolo = new ColorPickerHolo(context);
        mColorPickerHolo.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        mLayout.addView(mColorPickerHolo);

        mOpacityBar = new OpacityBar(getContext());
        mOpacityBar.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 16, 0, 16, 0));

        mSaturationBar = new SaturationBar(getContext());
        mSaturationBar.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 16, 0, 16, 0));

        mSaturationValueBar = new SaturationValueBar(getContext());
        mSaturationValueBar.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 16, 0, 16, 0));

        mValueBar = new ValueBar(getContext());
        mValueBar.setLayoutParams(LayoutHelper.makeLinear(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 16, 0, 16, 0));

        addView(mLayout);
    }

    public ColorPickerHoloSheet addOpacityBar() {
        mLayout.addView(mOpacityBar);
        mColorPickerHolo.addOpacityBar(mOpacityBar);
        return this;
    }

    public ColorPickerHoloSheet addSaturationBar() {
        mLayout.addView(mSaturationBar);
        mColorPickerHolo.addSaturationBar(mSaturationBar);
        return this;
    }

    public ColorPickerHoloSheet addSaturationValueBar() {
        mLayout.addView(mSaturationValueBar);
        mColorPickerHolo.addSVBar(mSaturationValueBar);
        return this;
    }

    public ColorPickerHoloSheet addValueBar() {
        mLayout.addView(mValueBar);
        mColorPickerHolo.addValueBar(mValueBar);
        return this;
    }
}