package org.michaelbel.app.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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

import org.michaelbel.app.R;
import org.michaelbel.app.cells.listview.EmptyCell;
import org.michaelbel.app.cells.listview.TextCell;
import org.michaelbel.app.model.DialogItem;
import org.michaelbel.material.color.ColorChooserDialog;
import org.michaelbel.material.core.util.DialogUtils;
import org.michaelbel.material.util.Utils;
import org.michaelbel.material.widget.ColorPicker.ColorMode;
import org.michaelbel.material.widget.ColorPicker.ColorPickerDialog;
import org.michaelbel.material.widget.ColorPicker.IndicatorMode;
import org.michaelbel.material.widget.HoloColorPicker;
import org.michaelbel.material.widget.LayoutHelper;
import org.michaelbel.material.widget.NumberPicker;
import org.michaelbel.material.widget.Palette;
import org.michaelbel.material.widget.ShiftColorPicker;

import java.util.ArrayList;

public class DialogsFragment extends Fragment implements ColorChooserDialog.ColorCallback {

    private static final String TAG = DialogsFragment.class.getSimpleName();
    private ArrayList<DialogItem> items;

    private int primaryPreselect;
    private int accentPreselect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getContext());
        fragmentView.setBackgroundColor(0xFFF0F0F0);


        primaryPreselect = DialogUtils.resolveColor(getContext(), R.attr.colorPrimary);
        accentPreselect = DialogUtils.resolveColor(getContext(), R.attr.colorAccent);
        items = new ArrayList<>();
        items.add(new DialogItem("Number Picker"));                  //- 0
        items.add(new DialogItem().setTitle("Number Picker"));       //- 1
        items.add(new DialogItem().setTitle("String Picker"));       //- 2
        items.add(new DialogItem("Shift Color Picker"));             //- 3
        items.add(new DialogItem().setTitle("Primary Colors"));      //- 4
        items.add(new DialogItem().setTitle("Primary Dark Colors")); //- 5
        items.add(new DialogItem().setTitle("Accent Colors"));       //- 6
        items.add(new DialogItem().setTitle("Material Colors"));     //- 7
        items.add(new DialogItem("SeekBar Color Picker"));           //- 8
        items.add(new DialogItem().setTitle("RGB"));                 //- 9
        items.add(new DialogItem().setTitle("ARGB"));                //- 10
        items.add(new DialogItem().setTitle("HSV"));                 //- 11
        items.add(new DialogItem().setTitle("HSL"));                 //- 12
        items.add(new DialogItem().setTitle("CMYK"));                //- 13
        items.add(new DialogItem().setTitle("CMYK 255"));            //- 14
        items.add(new DialogItem("Bottom Sheet"));                   //- 15
        items.add(new DialogItem().setTitle("Bottom Dialog"));       //- 16
        items.add(new DialogItem().setTitle("With Title"));          //- 17

        items.add(new DialogItem(""));                               //- 18
        items.add(new DialogItem().setTitle("Holo Color Picker"));   //- 19
        items.add(new DialogItem().setTitle("View Color Picker"));   //- 20

        ListView listView = new ListView(getContext());
        listView.setDividerHeight(0);
        listView.setDrawSelectorOnTop(true);
        listView.setAdapter(new ListViewAdapter());
        listView.setLayoutParams(LayoutHelper.makeFrame(getContext(),
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                if (i == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    final NumberPicker picker = new NumberPicker(getContext());
                    picker.setMinValue(0);
                    picker.setMaxValue(100);
                    picker.setValue(25);

                    builder.setView(picker);
                    builder.setTitle("Number Picker");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), getString(R.string.Value, picker.getValue()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                } else if (i == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    final NumberPicker picker = new NumberPicker(getContext());
                    picker.setMinValue(0);
                    picker.setMaxValue(6);
                    picker.setValue(4);
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

                            return null;
                        }
                    });

                    builder.setView(picker);
                    builder.setTitle("String Piker");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            which = picker.getValue();

                            if (which == 0) {
                                Toast.makeText(getContext(), getString(R.string.Monday), Toast.LENGTH_SHORT).show();
                            } else if (which == 1) {
                                Toast.makeText(getContext(), getString(R.string.Tuesday), Toast.LENGTH_SHORT).show();
                            } else if (which == 2) {
                                Toast.makeText(getContext(), getString(R.string.Wednesday), Toast.LENGTH_SHORT).show();
                            } else if (which == 3) {
                                Toast.makeText(getContext(), getString(R.string.Thursday), Toast.LENGTH_SHORT).show();
                            } else if (which == 4) {
                                Toast.makeText(getContext(), getString(R.string.Friday), Toast.LENGTH_SHORT).show();
                            } else if (which == 5) {
                                Toast.makeText(getContext(), getString(R.string.Saturday), Toast.LENGTH_SHORT).show();
                            } else if (which == 6) {
                                Toast.makeText(getContext(), getString(R.string.Sunday), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }

                if (i == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    RelativeLayout layout = new RelativeLayout(getContext());
                    layout.setPadding(Utils.dp(getContext(), 24), Utils.dp(getContext(), 24), Utils.dp(getContext(), 24),
                            Utils.dp(getContext(), 24));

                    final ShiftColorPicker picker = new ShiftColorPicker(getContext());
                    picker.setLayoutParams(LayoutHelper.makeRelative(getContext(), LayoutHelper.MATCH_PARENT, 60));
                    picker.setColors(Palette.PrimaryColors(getContext()));
                    picker.setSelectedColor(ContextCompat.getColor(getContext(), R.color.primary_red));
                    layout.addView(picker);

                    builder.setView(layout);
                    builder.setTitle("Primary Color");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), Integer.toHexString(picker.getColor()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                } else if (i == 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    RelativeLayout layout = new RelativeLayout(getContext());
                    layout.setPadding(Utils.dp(getContext(), 24), Utils.dp(getContext(), 24), Utils.dp(getContext(), 24),
                            Utils.dp(getContext(), 24));

                    final ShiftColorPicker picker = new ShiftColorPicker(getContext());
                    picker.setLayoutParams(LayoutHelper.makeRelative(getContext(), LayoutHelper.MATCH_PARENT, 60));
                    picker.setColors(Palette.PrimaryDarkColors(getContext()));
                    picker.setSelectedColor(ContextCompat.getColor(getActivity(), R.color.primary_dark_blue));
                    layout.addView(picker);

                    builder.setView(layout);
                    builder.setTitle("Primary Dark Color");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), Integer.toHexString(picker.getColor()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                } else if (i == 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    RelativeLayout layout = new RelativeLayout(getContext());
                    layout.setPadding(Utils.dp(getContext(), 24), Utils.dp(getContext(), 24), Utils.dp(getContext(), 24),
                            Utils.dp(getContext(), 24));

                    final ShiftColorPicker picker = new ShiftColorPicker(getContext());
                    picker.setLayoutParams(LayoutHelper.makeRelative(getContext(), LayoutHelper.MATCH_PARENT, 60));
                    picker.setColors(Palette.AccentColors(getContext()));
                    picker.setSelectedColor(ContextCompat.getColor(getContext(), R.color.accent_blue));
                    layout.addView(picker);

                    builder.setView(layout);
                    builder.setTitle("Accent Color");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), Integer.toHexString(picker.getColor()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                } else if (i == 7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT,
                            LayoutHelper.WRAP_CONTENT));
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(Utils.dp(getContext(), 24), Utils.dp(getContext(), 24), Utils.dp(getContext(), 24), 0);

                    final ShiftColorPicker picker1 = new ShiftColorPicker(getContext());
                    picker1.setColors(Palette.PrimaryColors(getContext()));
                    picker1.setLayoutParams(LayoutHelper.makeLinear(getContext(), LayoutHelper.MATCH_PARENT, 60));
                    layout.addView(picker1);

                    final ShiftColorPicker picker2 = new ShiftColorPicker(getContext());
                    picker2.setLayoutParams(LayoutHelper.makeLinear(getContext(), LayoutHelper.MATCH_PARENT, 40, 0, 10, 0, 0));

                    for (int i1 : picker1.getColors()) {
                        for (int i2 : Palette.MaterialColors(getActivity(), i1)) {
                            if (i2 == 0xff4CaF50) {
                                picker1.setSelectedColor(i1);
                                picker2.setColors(Palette.MaterialColors(getContext(), i1));
                                picker2.setSelectedColor(i2);
                                break;
                            }
                        }
                    }
                    layout.addView(picker2);

                    picker1.setOnColorChangedListener(new ShiftColorPicker.OnColorChangedListener() {
                        @Override
                        public void onColorChanged(int c) {
                            picker2.setColors(Palette.MaterialColors(getContext(), picker1.getColor()));
                            picker2.setSelectedColor(picker1.getColor());
                        }
                    });

                    builder.setView(layout);
                    builder.setTitle("Material Color");
                    builder.setNegativeButton(R.string.Cancel, null);
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), Integer.toHexString(picker2.getColor()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                
                if (i == 9) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.RGB);
                    builder.setIndicatorMode(IndicatorMode.HEX);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 10) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.ARGB);
                    builder.setIndicatorMode(IndicatorMode.HEX);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 110) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.HSV);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 120) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.HSL);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 130) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.CMYK);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 140) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.CMYK255);
                    builder.setIndicatorMode(IndicatorMode.HEX);
                    builder.create().show(getFragmentManager(), TAG);
                } /*else if (i == 11) {
                    new ColorChooserDialog.Builder(this, org.michaelbel.material.R.string.color_palette)
                            .titleSub(org.michaelbel.material.R.string.colors)
                            .preselect(primaryPreselect)
                            .show();
                } else if (i == 12) {
                    new ColorChooserDialog.Builder(this, org.michaelbel.material.R.string.color_palette)
                            .titleSub(org.michaelbel.material.R.string.colors)
                            .accentMode(true)
                            .preselect(accentPreselect)
                            .show();
                } else if (i == 13) {
                    int[][] subColors = new int[][]{
                            new int[]{Color.parseColor("#EF5350"), Color.parseColor("#F44336"), Color.parseColor("#E53935")},
                            new int[]{Color.parseColor("#EC407A"), Color.parseColor("#E91E63"), Color.parseColor("#D81B60")},
                            new int[]{Color.parseColor("#AB47BC"), Color.parseColor("#9C27B0"), Color.parseColor("#8E24AA")},
                            new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#673AB7"), Color.parseColor("#5E35B1")},
                            new int[]{Color.parseColor("#5C6BC0"), Color.parseColor("#3F51B5"), Color.parseColor("#3949AB")},
                            new int[]{Color.parseColor("#42A5F5"), Color.parseColor("#2196F3"), Color.parseColor("#1E88E5")}
                    };

                    new ColorChooserDialog.Builder(this, org.michaelbel.material.R.string.color_palette)
                            .titleSub(org.michaelbel.material.R.string.colors)
                            .preselect(primaryPreselect)
                            .customColors(org.michaelbel.material.R.array.custom_colors, subColors)
                            .show();
                } else if (i == 14) {
                    new ColorChooserDialog.Builder(this, org.michaelbel.material.R.string.color_palette)
                            .titleSub(org.michaelbel.material.R.string.colors)
                            .preselect(0xF44336)
                            .customColors(org.michaelbel.material.R.array.custom_colors, null)
                            .show();
                }*/

                /*if (i == 16) {
                    Calendar now = Calendar.getInstance();

                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
                                    Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
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
                } else if (i == 17) {
                    Calendar now = Calendar.getInstance();

                    TimePickerDialog time = TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                    String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                    String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                    String secondString = second < 10 ? "0" + second : "" + second;
                                    String time1 = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";

                                    Toast.makeText(getContext(), time1, Toast.LENGTH_LONG).show();
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
                }*/

                if (i == 19) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final HoloColorPicker picker = new HoloColorPicker(getActivity());
                    picker.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT,
                            LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
                    layout.addView(picker);
                    picker.setOldCenterColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));

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
                    builder.show();
                }

                if (i == -3) {
                    new SingleChoiceDialog().show(getFragmentManager(), TAG);
                } else if (i == -4) {
                    new MultiChoiceDialog().show(getFragmentManager(), TAG);
                }
            }
        });
        fragmentView.addView(listView);
        return fragmentView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*@Override
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

                Toast.makeText(getContext(), time1, Toast.LENGTH_LONG).show();
            }
        });

        if (dpd != null)
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
                Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
            }
        });
    }*/

    //public void showDialog(Dialog dialog) {
    //    ((LaunchActivity) getActivity()).showDialog(dialog);
    //}

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public boolean isEnabled(int i) {
            DialogItem item = items.get(i);
            return item.isNotTitle();
        }

        @Override
        public int getCount() {
            return items.size();
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

            DialogItem item = items.get(i);

            if (type == 0) {
                if (view == null) {
                    view = new TextCell(getContext());
                }

                TextCell cell = (TextCell) view;
                cell.setText(item.getDialogTitle()).setDivider(true);

            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(getContext());
                }

                EmptyCell cell = (EmptyCell) view;
                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                cell.setTextToUpperCase(true);
                cell.setHeader(item.getHeaderTitle());
            }

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            DialogItem item = items.get(i);

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

    public static class SingleChoiceDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), getString(R.string.Winter), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Toast.makeText(getContext(), getString(R.string.Spring), Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getContext(), getString(R.string.Summer), Toast.LENGTH_SHORT).show();
                    } else if (i == 3) {
                        Toast.makeText(getContext(), getString(R.string.Autumn), Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), getString(R.string.Winter), Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Toast.makeText(getContext(), getString(R.string.Spring), Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Toast.makeText(getContext(), getString(R.string.Summer), Toast.LENGTH_SHORT).show();
                    } else if (i == 3) {
                        Toast.makeText(getContext(), getString(R.string.Autumn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return builder.create();
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {
        Toast.makeText(getContext(), Integer.toHexString(color), Toast.LENGTH_SHORT).show();
    }
}