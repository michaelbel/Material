package org.app.application.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.NumberTextView;
import org.app.material.widget.LayoutHelper;

public class NumberTextFragment extends Fragment {

    private int j;
    private NumberTextView mCountTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        j = 100;

        mCountTextView = new NumberTextView(getActivity());
        mCountTextView.withTextSize(40);
        mCountTextView.withNumber(j, false);
        mCountTextView.withTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mCountTextView.withTextColor(AndroidUtilities.getContextColor(R.attr.colorPrimary));
        mCountTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER));
        layout.addView(mCountTextView);

        FloatingActionButton fabButton1 = new FloatingActionButton(getActivity());
        fabButton1.setImageResource(R.drawable.ic_plus);
        fabButton1.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 16));
        layout.addView(fabButton1);
        fabButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountTextView.withNumber(j = j + 1, true);
            }
        });

        FloatingActionButton fabButton2 = new FloatingActionButton(getActivity());
        fabButton2.setImageResource(R.drawable.ic_remove);
        fabButton2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 16, 16, 16, 88));
        layout.addView(fabButton2);
        fabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountTextView.withNumber(j = j - 1, true);
            }
        });

        return layout;
    }
}