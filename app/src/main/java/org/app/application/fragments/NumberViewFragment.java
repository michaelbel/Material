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
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberTextView;

public class NumberViewFragment extends Fragment {

    private int j;
    private NumberTextView numberTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getContext());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        j = 100;

        numberTextView = new NumberTextView(getActivity());
        numberTextView.setTextSize(30);
        numberTextView.setNumber(j, false);
        numberTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        numberTextView.setTextColor(AndroidUtilities.getThemeColor(R.attr.colorPrimary));
        numberTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        fragmentView.addView(numberTextView);

        FloatingActionButton fabButton1 = new FloatingActionButton(getActivity());
        fabButton1.setImageResource(R.drawable.ic_plus);
        fabButton1.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 16));
        fragmentView.addView(fabButton1);
        fabButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberTextView.setNumber(j = j + 1);
            }
        });

        FloatingActionButton fabButton2 = new FloatingActionButton(getActivity());
        fabButton2.setImageResource(R.drawable.ic_remove);
        fabButton2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 88));
        fragmentView.addView(fabButton2);
        fabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberTextView.setNumber(j = j - 1);
            }
        });

        return fragmentView;
    }
}