package org.michaelbel.app.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.michaelbel.app.R;
import org.michaelbel.app.cells.listview.EmptyCell;
import org.michaelbel.app.cells.listview.TextCell;
import org.michaelbel.app.dialogs.ColorPickerDialog;
import org.michaelbel.app.dialogs.HoloColorPickerDialog;
import org.michaelbel.app.dialogs.ViewColorPickerDialog;
import org.michaelbel.app.dialogs.number.NumberPickerDialog;
import org.michaelbel.app.dialogs.number.StringPickerDialog;
import org.michaelbel.app.dialogs.shift.AccentColorDialog;
import org.michaelbel.app.dialogs.shift.MaterialColorDialog;
import org.michaelbel.app.dialogs.shift.PrimaryColorDialog;
import org.michaelbel.app.dialogs.shift.PrimaryDarkColorDialog;
import org.michaelbel.app.model.DialogItem;
import org.michaelbel.material.Utils;
import org.michaelbel.material.picker.date.DatePickerDialog;
import org.michaelbel.material.picker.time.RadialPickerLayout;
import org.michaelbel.material.picker.time.TimePickerDialog;
import org.michaelbel.material.widget.ColorPicker.ColorMode;
import org.michaelbel.material.widget.ColorPicker.IndicatorMode;
import org.michaelbel.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class DialogsFragment extends Fragment {

    private static final String TAG = DialogsFragment.class.getSimpleName();
    
    private ArrayList<DialogItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getContext());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

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
                    NumberPickerDialog dialog = NumberPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 2) {
                    StringPickerDialog dialog = StringPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                }

                if (i == 4) {
                    PrimaryColorDialog dialog = PrimaryColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 5) {
                    PrimaryDarkColorDialog dialog = PrimaryDarkColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 6) {
                    AccentColorDialog dialog = AccentColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 7) {
                    MaterialColorDialog dialog = MaterialColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
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
                } else if (i == 11) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.HSV);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 12) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.HSL);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 13) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.CMYK);
                    builder.setIndicatorMode(IndicatorMode.DECIMAL);
                    builder.create().show(getFragmentManager(), TAG);
                } else if (i == 14) {
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext());
                    builder.setInitialColor(Utils.getAttrColor(getContext(), R.attr.colorAccent));
                    builder.setColorMode(ColorMode.CMYK255);
                    builder.setIndicatorMode(IndicatorMode.HEX);
                    builder.create().show(getFragmentManager(), TAG);
                }

                if (i == 16) {

                } else if (i == 17) {

                }

                if (i == 19) {
                    HoloColorPickerDialog dialog = HoloColorPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 20) {
                    ViewColorPickerDialog dialog = ViewColorPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                }



                
                


                if (i == -3) {
                    new SingleChoiceDialog().show(getFragmentManager(), TAG);
                } else if (i == -4) {
                    new MultiChoiceDialog().show(getFragmentManager(), TAG);
                } else if (i == -6) {
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                            RingtoneManager.TYPE_NOTIFICATION | RingtoneManager.TYPE_RINGTONE);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    startActivityForResult(intent, 0);
                } else if (i == -24) {
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
                } else if (i == -25) {
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
    }

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
}