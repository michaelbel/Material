package org.app.application.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.IconicsDrawable;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberView;

public class NumberViewFragment extends Fragment {

    private int j;
    private NumberView mNumberView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getContext());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        j = 100;

        mNumberView = new NumberView(getActivity());
        mNumberView.setTextSize(30);
        mNumberView.setNumber(j, false);
        mNumberView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mNumberView.setTextColor(AndroidUtilities.getContextColor(R.attr.colorPrimary));
        mNumberView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        fragmentView.addView(mNumberView);

        FloatingActionButton fabButton1 = new FloatingActionButton(getActivity());
        fabButton1.setImageResource(R.drawable.ic_plus);
        fabButton1.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 16));
        fragmentView.addView(fabButton1);
        fabButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberView.setNumber(j = j + 1, true);
                //Toast.makeText(getContext(), SwitchPalette.getColor200(getContext(), AndroidUtilities.getContextColor(R.attr.colorAccent)), Toast.LENGTH_LONG).show();
            }
        });

        IconicsDrawable icon = new IconicsDrawable()
                .with(getContext())
                .icon(R.drawable.ic_remove)
                .color(0xFF424242)
                .sizeDp(56);

        FloatingActionButton fabButton2 = new FloatingActionButton(getActivity());
        fabButton2.setImageDrawable(icon);
        fabButton2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 88));
        fragmentView.addView(fabButton2);
        fabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberView.setNumber(j = j - 1, true);
                //Toast.makeText(getContext(), SwitchPalette.getColor500(getContext(), AndroidUtilities.getContextColor(R.attr.colorAccent)), Toast.LENGTH_LONG).show();
            }
        });

        return fragmentView;
    }
}