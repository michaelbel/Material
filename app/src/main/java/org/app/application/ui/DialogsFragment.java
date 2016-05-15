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
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.ColorPickerShift;
import org.app.material.widget.ColorPicker;
import org.app.material.widget.ColorPickerHolo;
import org.app.material.widget.ColorPickerView;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberPicker;

public class DialogsFragment extends Fragment implements View.OnClickListener {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFFECEFF1);

        mButton1 = new Button(getActivity());
        mButton1.setOnClickListener(this);
        mButton1.setText(R.string.Strings);
        mButton1.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton1);

        mButton2 = new Button(getActivity());
        mButton2.setOnClickListener(this);
        mButton2.setText(R.string.NumberPicker);
        mButton2.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton2);

        mButton3 = new Button(getActivity());
        mButton3.setOnClickListener(this);
        mButton3.setText(R.string.StringPicker);
        mButton3.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton3);

        mButton4 = new Button(getActivity());
        mButton4.setOnClickListener(this);
        mButton4.setText(R.string.ColorPickerRGB);
        mButton4.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        //layout.addView(mButton4);

        mButton5 = new Button(getActivity());
        mButton5.setOnClickListener(this);
        mButton5.setText(R.string.ColorPickerHolo);
        mButton5.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton5);

        mButton6 = new Button(getActivity());
        mButton6.setOnClickListener(this);
        mButton6.setText(R.string.ColorPickerView);
        mButton6.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton6);

        mButton7 = new Button(getActivity());
        mButton7.setOnClickListener(this);
        mButton7.setText("Color Picker Primary ");
        mButton7.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton7);

        mButton8 = new Button(getActivity());
        mButton8.setOnClickListener(this);
        mButton8.setText("Color Picker Accent");
        mButton8.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        layout.addView(mButton8);

        return layout;
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new CharSequence[]{
                    getString(R.string.Winter),
                    getString(R.string.Spring),
                    getString(R.string.Summer),
                    getString(R.string.Autumn)
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        makeToast(R.string.Winter);
                    } else if (i == 1) {
                        makeToast(R.string.Spring);
                    } else if (i == 2) {
                        makeToast(R.string.Summer);
                    } else if (i == 3) {
                        makeToast(R.string.Autumn);
                    }
                }
            });
            builder.show();
        } else if (v == mButton2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setmMinValue(0);
            picker.setmMaxValue(100);
            picker.setValue(10);
            picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            builder.setView(picker);
            builder.setTitle(R.string.NumberPicker);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), getString(R.string.Value, picker.getValue()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (v == mButton3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setmMinValue(0);
            picker.setmMaxValue(6);
            picker.setValue(4);
            picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            picker.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    if (value == 0) {
                        return getString(R.string.Monday);
                    } else if (value == 1) {
                        return getString(R.string.Tuesday);
                    } else if (value == 2) {
                        return getString(R.string.Wednesday);
                    } else if (value == 3) {
                        return getString(R.string.Thursday);
                    } else if (value == 4) {
                        return getString(R.string.Friday);
                    } else if (value == 5) {
                        return getString(R.string.Saturday);
                    } else if (value == 6) {
                        return getString(R.string.Sunday);
                    }

                    return "";
                }
            });

            builder.setView(picker);
            builder.setTitle(R.string.StringPicker);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    which = picker.getValue();

                    if (which == 0) {
                        makeToast(R.string.Monday);
                    } else if (which == 1) {
                        makeToast(R.string.Tuesday);
                    } else if (which == 2) {
                        makeToast(R.string.Wednesday);
                    } else if (which == 3) {
                        makeToast(R.string.Thursday);
                    } else if (which == 4) {
                        makeToast(R.string.Friday);
                    } else if (which == 5) {
                        makeToast(R.string.Saturday);
                    } else if (which == 6) {
                        makeToast(R.string.Sunday);
                    }
                }
            });
            builder.show();
        } else if (v == mButton4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final ColorPicker picker = new ColorPicker(getActivity());
            //picker.setColor(25, 50, 75);

            builder.setView(picker);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getNewColor()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (v == mButton5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final ColorPickerHolo picker = new ColorPickerHolo(getActivity());
            picker.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
            layout.addView(picker);
            picker.setOldCenterColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent));

            builder.setView(layout);
            builder.setTitle(R.string.ColorPickerHolo);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getColor()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (v == mButton6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final ColorPickerView picker = new ColorPickerView(getActivity());
            picker.setDensity(11);
            picker.setType(ColorPickerView.CIRCLE);
            picker.setInitialColor(AndroidUtilities.getContextColor(getActivity(), R.attr.colorAccent));
            layout.addView(picker, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

            builder.setView(layout);
            builder.setTitle(R.string.ColorPickerView);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getSelectedColor()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (v == mButton7) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24));

            final ColorPickerShift picker1 = new ColorPickerShift(getActivity());
            picker1.setColors(ColorPickerShift.Palette.getBaseColors(getActivity()));
            picker1.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, 60));
            layout.addView(picker1);

            final ColorPickerShift picker2 = new ColorPickerShift(getActivity());
            picker2.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, 40, 0, 10, 0, 0));

            for (int i : picker1.getColors()) {
                for (int i2 : ColorPickerShift.Palette.getColors(getActivity(), i)) {
                    if (i2 == 0xff4CaF50) {
                        picker1.setSelectedColor(i);
                        picker2.setColors(ColorPickerShift.Palette.getColors(getActivity(), i));
                        picker2.setSelectedColor(i2);
                        break;
                    }
                }
            }
            layout.addView(picker2);

            picker1.setOnColorChangedListener(new ColorPickerShift.OnColorChangedListener() {
                @Override
                public void onColorChanged(int c) {
                    picker2.setColors(ColorPickerShift.Palette.getColors(getActivity(), picker1.getColor()));
                    picker2.setSelectedColor(picker1.getColor());
                }
            });

            builder.setView(layout);
            builder.setTitle(R.string.PrimaryColor);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), Integer.toHexString(picker2.getColor()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (v == mButton8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            RelativeLayout layout = new RelativeLayout(getActivity());
            layout.setPadding(AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24));

            final ColorPickerShift picker = new ColorPickerShift(getActivity());
            picker.setLayoutParams(LayoutHelper.makeRelative(getActivity(), LayoutHelper.MATCH_PARENT, 60));
            picker.setSelectedColorPosition(0);
            picker.setColors(ColorPickerShift.Palette.getAccentColors(getActivity()));
            picker.setSelectedColor(0xffFF5252);
            layout.addView(picker);

            builder.setView(layout);
            builder.setTitle(R.string.AccentColor);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), Integer.toHexString(picker.getColor()), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
    }

    private void makeToast(@StringRes int resId) {
        Toast.makeText(getActivity(), getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }
}