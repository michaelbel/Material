/*
 * Copyright 2016 Michael Bel
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

package org.app.application.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.ColorPicker;
import org.app.material.widget.FilePicker;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberPicker;

public class DialogFragment extends Fragment implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFECEFF1);

        button1 = new Button(getActivity());
        button1.setText(getResources().getString(R.string.NumberPicker));
        button1.setOnClickListener(this);
        layout.addView(button1, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        button2 = new Button(getActivity());
        button2.setText(R.string.NumberPickerStrings);
        button2.setOnClickListener(this);
        layout.addView(button2, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        button3 = new Button(getActivity());
        button3.setText(getResources().getString(R.string.FilePicker));
        button3.setOnClickListener(this);
        layout.addView(button3, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        button4 = new Button(getActivity());
        button4.setText(R.string.AlertStrings);
        button4.setOnClickListener(this);
        layout.addView(button4, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        button5 = new Button(getActivity());
        button5.setText("Color Picker");
        button5.setOnClickListener(this);
        layout.addView(button5, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == button1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.NumberPickerTitle));

            final NumberPicker numberPicker = new NumberPicker(getActivity());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(10);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setSelectionDividerColor(0xff4285f4);

            builder.setView(numberPicker);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Value = " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
        } else if (view == button2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("NumberPicker Strings");

            final NumberPicker numberPickerStrings = new NumberPicker(getActivity());
            numberPickerStrings.setMinValue(0);
            numberPickerStrings.setMaxValue(6);
            numberPickerStrings.setValue(0);
            numberPickerStrings.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPickerStrings.setSelectionDividerColor(0xff4285f4);
            numberPickerStrings.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    if (value == 0) {
                        return "Monday";
                    } else if (value == 1) {
                        return "Tuesday";
                    } else if (value == 2) {
                        return "Wednesday";
                    } else if (value == 3) {
                        return  "Thursday";
                    } else if (value == 4) {
                        return "Friday";
                    } else if (value == 5) {
                        return "Saturday";
                    } else if (value == 6) {
                        return "Sunday";
                    }

                    return null;
                }
            });

            builder.setView(numberPickerStrings);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Day = " + numberPickerStrings.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
        } else if (view == button3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            FilePicker filePicker = new FilePicker(getActivity());
            filePicker.showDivider();

            builder.setView(filePicker);
            builder.show();
        } else if (view == button4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new CharSequence[]{
                    "Winter", "Spring", "Summer", "Autumn"
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "Season = " + i, Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (view == button5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select color");

            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            final ColorPicker colorPicker = new ColorPicker(getActivity());
            linearLayout.addView(colorPicker, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
            colorPicker.setOldCenterColor(0xFF00FF00);
            colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                @Override
                public void onColorChanged(int i) {

                }
            });

            builder.setView(linearLayout);
            builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Toast.makeText(getActivity(), "Color = " + colorPicker.getColor(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNeutralButton("Disabled", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(0xff4285f4);
        }
    }
}