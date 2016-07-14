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
import org.app.material.widget.CheckBox;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.RadioButton;
import org.app.material.widget.Switch;

public class SwitchesFragment extends Fragment {

    private Switch switch1;
    private Switch switch2;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private RadioButton radioButton1;
    private RadioButton radioButton2;

    private boolean check1 = false;
    private boolean check2 = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switches, container, false);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.frameLock);
        layout.setBackgroundColor(0xFFF0F0F0);

        switch1 = (Switch) view.findViewById(R.id.m_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*check1 = !check1;
                switch1.setChecked(check1);*/
            }
        });

        switch2 = new Switch(getContext());
        switch2.withRTL(false);
        switch2.withAnimationDuration(220);
        switch2.withThumbColorNormal(0xFFF1F1F1);
        switch2.withTrackColorNormal(0xFFB0AFAF);
        switch2.withThumbColorActivated(0xFF009688);
        switch2.withTrackColorActivated(0xFF77C2BB);
        switch2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 0, 40, 40, 0));
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*check2 = !check2;
                switch2.setChecked(check2);*/
            }
        });
        layout.addView(switch2);

        checkBox1 = (CheckBox) view.findViewById(R.id.checkBox);

        checkBox2 = new CheckBox(getContext());
        checkBox2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 0, 80, 40, 0));
        layout.addView(checkBox2);

        radioButton1 = (RadioButton) view.findViewById(R.id.radioButton);

        radioButton2 = new RadioButton(getContext());
        radioButton2.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.TOP, 0, 120, 40, 0));
        layout.addView(radioButton2);

        return view;
    }


}