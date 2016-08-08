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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.app.application.LaunchActivity;
import org.app.application.R;
import org.app.application.cells.listview.EmptyCell;
import org.app.application.cells.listview.TextCell;
import org.app.application.dialogs.ColorPickerDialog;
import org.app.material.AndroidUtilities;
import org.app.material.picker.date.DatePickerDialog;
import org.app.material.picker.time.RadialPickerLayout;
import org.app.material.picker.time.TimePickerDialog;
import org.app.material.widget.ColorPickerHolo;
import org.app.material.widget.ColorPickerShift;
import org.app.material.widget.ColorPickerView;
import org.app.material.widget.ColorView;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.NumberPicker;
import org.app.material.widget.Palette;

import java.util.ArrayList;
import java.util.Calendar;

public class DialogsFragment extends Fragment {

    private final String TAG = DialogsFragment.class.getSimpleName();

    private ArrayList<ListItem> dialogs;
    private LaunchActivity mActivity;

    public class ListItem {

        private String dialogTitle;
        private String headerTitle;

        public ListItem() {}

        public ListItem setTitle(String title) {
            this.dialogTitle = title;
            return this;
        }

        public ListItem setHeader(String header) {
            this.headerTitle = header;
            return this;
        }

        public String getHeaderTitle() {
            return headerTitle;
        }

        public String getDialogTitle() {
            return dialogTitle;
        }

        public boolean isNotTitle() {
            return getDialogTitle() != null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getActivity());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        mActivity = (LaunchActivity) getActivity();

        dialogs = new ArrayList<>();
        dialogs.add(new ListItem().setHeader("Simple pickers"));
        dialogs.add(new ListItem().setTitle("Message"));
        dialogs.add(new ListItem().setTitle("Strings"));
        dialogs.add(new ListItem().setTitle("Single Item"));
        dialogs.add(new ListItem().setTitle("Multi Items"));
        dialogs.add(new ListItem().setHeader("Ringtone pickers"));
        dialogs.add(new ListItem().setTitle("Ringtone"));
        dialogs.add(new ListItem().setHeader("Number Pickers"));
        dialogs.add(new ListItem().setTitle("Number Picker"));
        dialogs.add(new ListItem().setTitle("Number Picker Strings"));
        dialogs.add(new ListItem().setHeader("Color pickers"));
        dialogs.add(new ListItem().setTitle("Color Accent"));
        dialogs.add(new ListItem().setTitle("Color Primary"));
        dialogs.add(new ListItem().setTitle("Color Holo"));
        dialogs.add(new ListItem().setTitle("Color View"));
        dialogs.add(new ListItem().setTitle("Color Picker RGB"));
        dialogs.add(new ListItem().setTitle("Color Picker ARGB"));
        dialogs.add(new ListItem().setTitle("Color Picker HSV"));
        dialogs.add(new ListItem().setTitle("Color Picker HSL"));
        dialogs.add(new ListItem().setTitle("Color Picker CMYK"));
        dialogs.add(new ListItem().setTitle("Color Picker CMYK255"));
        dialogs.add(new ListItem().setHeader("Voice speak"));
        dialogs.add(new ListItem().setTitle("Voice"));
        dialogs.add(new ListItem().setHeader("Date and Time Pickers"));
        dialogs.add(new ListItem().setTitle("Date Picker"));
        dialogs.add(new ListItem().setTitle("Time Picker"));
        //dialogs.add(new ListItem().setHeader("EditText pickers"));
        //dialogs.add(new ListItem().setHeader("Bottom Sheet"));
        //dialogs.add(new ListItem().setTitle("Bottom Sheet Dialog 1"));
        //dialogs.add(new ListItem().setHeader("Color Pickers"));
        //dialogs.add(new ListItem().setTitle("Bottom Sheet Dialog 2"));

        ListView listView = new ListView(getActivity());
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setAdapter(new ListViewAdapter());
        listView.setLayoutParams(LayoutHelper.makeFrame(getActivity(),
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == 1) {
                    new SimpleMessageDialog().show(getFragmentManager(), TAG);
                } else if (i == 2) {
                    new ItemsDialog().show(getFragmentManager(), TAG);
                } else if (i == 3) {
                    new SingleChoiceDialog().show(getFragmentManager(), TAG);
                } else if (i == 4) {
                    new MultiChoiceDialog().show(getFragmentManager(), TAG);
                } else if (i == 6) {
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                            RingtoneManager.TYPE_NOTIFICATION | RingtoneManager.TYPE_RINGTONE);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    startActivityForResult(intent, 0);
                } else if (i == 8) {
                    NumberPickerDialog dialog = new NumberPickerDialog();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 9) {
                    new NumberPickerStringDialog().show(getFragmentManager(), TAG);
                } else if (i == 11) {
                    new ColorPickerAccentDialog().show(getFragmentManager(), TAG);
                } else if (i == 12) {
                    new ColorPickerPrimaryDialog().show(getFragmentManager(), TAG);
                } else if (i == 13) {
                    new ColorPickerHoloDialog().show(getFragmentManager(), TAG);
                } else if (i == 14) {
                    new ColorPickerViewDialog().show(getFragmentManager(), TAG);
                } else if (i == 15) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.RGB)
                            .indicatorMode(ColorView.IndicatorMode.HEX)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 16) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.ARGB)
                            .indicatorMode(ColorView.IndicatorMode.HEX)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 17) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.HSV)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 18) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.HSL)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 19 ) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.CMYK)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 20) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getContextColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.CMYK255)
                            .indicatorMode(ColorView.IndicatorMode.HEX)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 24) {
                    Calendar now = Calendar.getInstance();

                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
                                    Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setOkText("Set");
                    dpd.setAccentColor(0xFFFF5252);
                    dpd.setCancelText("Cancel");
                    dpd.setThemeDark(true);
                    dpd.show(getActivity().getFragmentManager(), "Date");
                } else if (i == 25) {
                    Calendar now = Calendar.getInstance();

                    TimePickerDialog time = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                    String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                    String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                    String secondString = second < 10 ? "0" + second : "" + second;
                                    String time1 = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";

                                    Toast.makeText(getActivity(), time1, Toast.LENGTH_LONG).show();
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );
                    time.setOkText("Set");
                    time.setAccentColor(0xFFFF5252);
                    time.setCancelText("Cancel");
                    time.setThemeDark(true);
                    time.show(getActivity().getFragmentManager(), "Time");
                }
            }
        });
        fragmentView.addView(listView);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Time");
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Date");

        if (tpd != null)
            tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String secondString = second < 10 ? "0" + second : "" + second;
                String time1 = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";

                Toast.makeText(getActivity(), time1, Toast.LENGTH_LONG).show();
            }
        });

        if (dpd != null)
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
                Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public boolean isEnabled(int i) {
            ListItem item = dialogs.get(i);

            return item.isNotTitle();
        }

        @Override
        public int getCount() {
            return dialogs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);

            ListItem item = dialogs.get(i);

            if (type == 0) {
                if (view == null) {
                    view = new TextCell(getActivity());
                }

                TextCell cell = (TextCell) view;
                cell.withText(item.getDialogTitle()).withDivider(true);

            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(getActivity());
                }

                EmptyCell cell = (EmptyCell) view;
                cell.withGravity(Gravity.CENTER_HORIZONTAL);
                cell.withTextToUpperCase(true);
                cell.withHeader(item.getHeaderTitle());
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            ListItem item = dialogs.get(i);

            if (item.isNotTitle()) {
                return 0;
            } else if (!item.isNotTitle()) {
                return 1;
            }

            return -1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    public static class SimpleMessageDialog extends DialogFragment {

        private String message = "Simple message text";

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.AppName);
            builder.setMessage(message);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }
    }

    public static class ItemsDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                        Toast.makeText(getActivity(), getString(R.string.Winter), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Toast.makeText(getActivity(), getString(R.string.Spring), Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getActivity(), getString(R.string.Summer), Toast.LENGTH_SHORT).show();
                    } else if (i == 3) {
                        Toast.makeText(getActivity(), getString(R.string.Autumn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return builder.create();
        }
    }

    public static class SingleChoiceDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setSingleChoiceItems(new CharSequence[]{
                    getString(R.string.Winter),
                    getString(R.string.Spring),
                    getString(R.string.Summer),
                    getString(R.string.Autumn)
            }, 0, null);
            builder.setPositiveButton("Choice", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Toast.makeText(getActivity(), getString(R.string.Winter), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Toast.makeText(getActivity(), getString(R.string.Spring), Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getActivity(), getString(R.string.Summer), Toast.LENGTH_SHORT).show();
                    } else if (i == 3) {
                        Toast.makeText(getActivity(), getString(R.string.Autumn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return builder.create();
        }
    }

    public static class MultiChoiceDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMultiChoiceItems(new CharSequence[]{
                    getString(R.string.Winter),
                    getString(R.string.Spring),
                    getString(R.string.Summer),
                    getString(R.string.Autumn)
            }, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                }
            });
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        Toast.makeText(getActivity(), getString(R.string.Winter), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Toast.makeText(getActivity(), getString(R.string.Spring), Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getActivity(), getString(R.string.Summer), Toast.LENGTH_SHORT).show();
                    } else if (i == 3) {
                        Toast.makeText(getActivity(), getString(R.string.Autumn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return builder.create();
        }
    }

    public static class NumberPickerDialog extends DialogFragment {

        @NonNull @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setMinValue(0);
            picker.setMaxValue(100);
            picker.setValue(25);
            picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            builder.setView(picker);
            builder.setTitle(R.string.NumberPicker);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), getString(R.string.Value, picker.getValue()),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }

    public static class NumberPickerStringDialog extends DialogFragment {

        @NonNull @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setMinValue(0);
            picker.setMaxValue(6);
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
                        Toast.makeText(getActivity(), getString(R.string.Monday), Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        Toast.makeText(getActivity(), getString(R.string.Tuesday), Toast.LENGTH_SHORT).show();
                    } else if (which == 2) {
                        Toast.makeText(getActivity(), getString(R.string.Wednesday), Toast.LENGTH_SHORT).show();
                    } else if (which == 3) {
                        Toast.makeText(getActivity(), getString(R.string.Thursday), Toast.LENGTH_SHORT).show();
                    } else if (which == 4) {
                        Toast.makeText(getActivity(), getString(R.string.Friday), Toast.LENGTH_SHORT).show();
                    } else if (which == 5) {
                        Toast.makeText(getActivity(), getString(R.string.Saturday), Toast.LENGTH_SHORT).show();
                    } else if (which == 6) {
                        Toast.makeText(getActivity(), getString(R.string.Sunday), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return builder.create();
        }
    }

    public static class ColorPickerAccentDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            RelativeLayout layout = new RelativeLayout(getActivity());
            layout.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(24),
                    AndroidUtilities.dp(24), AndroidUtilities.dp(24));

            final ColorPickerShift picker = new ColorPickerShift(getActivity());
            picker.setLayoutParams(LayoutHelper.makeRelative(getActivity(),
                    LayoutHelper.MATCH_PARENT, 60));
            picker.setSelectedColorPosition(0);
            picker.setColors(Palette.AccentColors(getActivity()));
            picker.setSelectedColor(ContextCompat.getColor(getActivity(), R.color.primary_red));
            layout.addView(picker);

            builder.setView(layout);
            builder.setTitle(R.string.AccentColor);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), Integer.toHexString(picker.getColor()),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }

    public static class ColorPickerPrimaryDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT,
                    LayoutHelper.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(24),
                    AndroidUtilities.dp(24), AndroidUtilities.dp(24));

            final ColorPickerShift picker1 = new ColorPickerShift(getActivity());
            picker1.setColors(Palette.AccentColors(getActivity()));
            picker1.setLayoutParams(LayoutHelper.makeLinear(getActivity(),
                    LayoutHelper.MATCH_PARENT, 60));
            layout.addView(picker1);

            final ColorPickerShift picker2 = new ColorPickerShift(getActivity());
            picker2.setLayoutParams(LayoutHelper.makeLinear(getActivity(),
                    LayoutHelper.MATCH_PARENT, 40, 0, 10, 0, 0));

            for (int i : picker1.getColors()) {
                for (int i2 : Palette.PrimaryColors(getActivity(), i)) {
                    if (i2 == 0xff4CaF50) {
                        picker1.setSelectedColor(i);
                        picker2.setColors(Palette.PrimaryColors(getActivity(), i));
                        picker2.setSelectedColor(i2);
                        break;
                    }
                }
            }
            layout.addView(picker2);

            picker1.setOnColorChangedListener(new ColorPickerShift.OnColorChangedListener() {
                @Override
                public void onColorChanged(int c) {
                    picker2.setColors(Palette.PrimaryColors(getActivity(), picker1.getColor()));
                    picker2.setSelectedColor(picker1.getColor());
                }
            });

            builder.setView(layout);
            builder.setTitle(R.string.PrimaryColor);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), Integer.toHexString(picker2.getColor()),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }

    public static class ColorPickerHoloDialog extends DialogFragment {

        private ColorPickerHolo picker;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            picker = new ColorPickerHolo(getActivity());
            picker.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT,
                    LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
            layout.addView(picker);
            picker.setOldCenterColor(AndroidUtilities.getContextColor(R.attr.colorAccent));

            builder.setView(layout);
            builder.setTitle(R.string.ColorPickerHolo);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getColor()),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }

    public static class ColorPickerViewDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final ColorPickerView picker = new ColorPickerView(getActivity());
            picker.setDensity(12);
            picker.setType(ColorPickerView.CIRCLE);
            picker.setInitialColor(AndroidUtilities.getContextColor(R.attr.colorAccent));
            layout.addView(picker, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT,
                    LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

            builder.setView(layout);
            builder.setTitle(R.string.ColorPickerView);
            builder.setNegativeButton(R.string.Cancel, null);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getSelectedColor()),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }
}