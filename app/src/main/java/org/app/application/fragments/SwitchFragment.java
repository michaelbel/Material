package org.app.application.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Switch;

public class SwitchFragment extends Fragment {

    private Switch mSwitch1;
    private Switch mSwitch2;

    private boolean check1 = false;
    private boolean check2 = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch, container, false);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.frameLock);
        layout.setBackgroundColor(0xFFF0F0F0);

        mSwitch1 = (Switch) view.findViewById(R.id.m_switch);
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check1 = !check1;
                mSwitch1.setChecked(check1);
            }
        });

        mSwitch2 = new Switch(getContext());
        //mSwitch2.setClickable(true);
        mSwitch2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 150, 0, 0));
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check2 = !check2;
                mSwitch2.setChecked(check2);
            }
        });
        layout.addView(mSwitch2);

        mSwitch2
            .withRTL(false)
            .withAnimationDuration(220);

        return view;
    }


}