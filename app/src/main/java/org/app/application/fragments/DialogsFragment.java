/*
 * Copyright 2015 Michael Bel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.app.application.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.app.application.R;
import org.app.application.dialogs.ColorPickerAccentDialog;
import org.app.application.dialogs.ColorPickerDialog;
import org.app.application.dialogs.ColorPickerHoloDialog;
import org.app.application.dialogs.ColorPickerPrimaryDialog;
import org.app.application.dialogs.ColorPickerViewDialog;
import org.app.application.dialogs.ItemsDialog;
import org.app.application.dialogs.NumberPickerDialog;
import org.app.application.dialogs.StringPickerDialog;
import org.app.material.AndroidUtilities;
import org.app.material.widget.ColorView;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class DialogsFragment extends Fragment implements View.OnClickListener {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton13;
    private Button mButton14;

    private DialogFragment dialog;

    private List<Button> buttons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView sView = new ScrollView(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFF0F0F0);

        sView.addView(layout);

        mButton1 = new Button(getActivity());
        mButton1.setText(R.string.Strings);
        layout.addView(mButton1);

        mButton2 = new Button(getActivity());
        mButton2.setText(R.string.NumberPicker);
        layout.addView(mButton2);

        mButton3 = new Button(getActivity());
        mButton3.setText(R.string.StringPicker);
        layout.addView(mButton3);

        mButton4 = new Button(getActivity());
        mButton4.setText("");
        //layout.addView(mButton4);

        mButton5 = new Button(getActivity());
        mButton5.setText(R.string.ColorPickerHolo);
        layout.addView(mButton5);

        mButton6 = new Button(getActivity());
        mButton6.setText(R.string.ColorPickerView);
        layout.addView(mButton6);

        mButton7 = new Button(getActivity());
        mButton7.setText(R.string.ColorPickerPrimary);
        layout.addView(mButton7);

        mButton8 = new Button(getActivity());
        mButton8.setText(R.string.ColorPickerAccent);
        layout.addView(mButton8);

        mButton9 = new Button(getActivity());
        mButton9.setText(R.string.ColorPickerRGB);
        layout.addView(mButton9);

        mButton10 = new Button(getActivity());
        mButton10.setText(R.string.ColorPickerARGB);
        layout.addView(mButton10);

        mButton11 = new Button(getActivity());
        mButton11.setText(R.string.ColorPickerHSV);
        layout.addView(mButton11);

        mButton12 = new Button(getActivity());
        mButton12.setText(R.string.ColorPickerHSL);
        layout.addView(mButton12);

        mButton13 = new Button(getActivity());
        mButton13.setText(R.string.ColorPickerCMYK);
        layout.addView(mButton13);

        mButton14 = new Button(getActivity());
        mButton14.setText(R.string.ColorPickerCMYK255);
        layout.addView(mButton14);

        buttons.add(mButton1);
        buttons.add(mButton2);
        buttons.add(mButton3);
        buttons.add(mButton4);
        buttons.add(mButton5);
        buttons.add(mButton6);
        buttons.add(mButton7);
        buttons.add(mButton8);
        buttons.add(mButton9);
        buttons.add(mButton10);
        buttons.add(mButton11);
        buttons.add(mButton12);
        buttons.add(mButton13);
        buttons.add(mButton14);

        for (Button button : buttons) {
            button.setOnClickListener(this);
            button.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        }

        return sView;
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            dialog = new ItemsDialog();
            dialog.show(getFragmentManager(), "items");
        } else if (v == mButton2) {
            dialog = new NumberPickerDialog();
            dialog.show(getFragmentManager(), "numberPicker");
        } else if (v == mButton3) {
            dialog = new StringPickerDialog();
            dialog.show(getFragmentManager(), "stringPicker");
        } else if (v == mButton4) {

        } else if (v == mButton5) {
            dialog = new ColorPickerHoloDialog();
            dialog.show(getFragmentManager(), "pickerHolo");
        } else if (v == mButton6) {
            dialog = new ColorPickerViewDialog();
            dialog.show(getFragmentManager(), "pickerView");
        } else if (v == mButton7) {
            dialog = new ColorPickerPrimaryDialog();
            dialog.show(getFragmentManager(), "pickerPrimary");
        } else if (v == mButton8) {
            dialog = new ColorPickerAccentDialog();
            dialog.show(getFragmentManager(), "pickerAccent");
        } else if (v == mButton9) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.RGB)
                    .indicatorMode(ColorView.IndicatorMode.HEX)
                    .create()
                    .show(getFragmentManager(), "dialog1");
        } else if (v == mButton10) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.ARGB)
                    .indicatorMode(ColorView.IndicatorMode.HEX)
                    .create()
                    .show(getFragmentManager(), "dialog2");
        } else if (v == mButton11) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.HSV)
                    .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                    .create()
                    .show(getFragmentManager(), "dialog3");
        } else if (v == mButton12) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.HSL)
                    .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                    .create()
                    .show(getFragmentManager(), "dialog4");
        } else if (v == mButton13) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.CMYK)
                    .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                    .create()
                    .show(getFragmentManager(), "dialog5");
        } else if (v == mButton14) {
            new ColorPickerDialog.Builder()
                    .initialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent))
                    .colorMode(ColorView.ColorMode.CMYK255)
                    .indicatorMode(ColorView.IndicatorMode.HEX)
                    .create()
                    .show(getFragmentManager(), "dialog6");
        }
    }
}