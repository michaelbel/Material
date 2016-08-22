package org.app.application.fragments;

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

import org.app.application.LaunchActivity;
import org.app.application.R;
import org.app.application.cells.listview.EmptyCell;
import org.app.application.cells.listview.TextCell;
import org.app.application.dialogs.BottomSheetDialog1;
import org.app.application.dialogs.ColorPickerDialog;
import org.app.application.dialogs.HoloColorPickerDialog;
import org.app.application.dialogs.ViewColorPickerDialog;
import org.app.application.dialogs.number.NumberPickerDialog;
import org.app.application.dialogs.number.StringPickerDialog;
import org.app.application.dialogs.shift.AccentColorDialog;
import org.app.application.dialogs.shift.MaterialColorDialog;
import org.app.application.dialogs.shift.PrimaryColorDialog;
import org.app.application.dialogs.shift.PrimaryDarkColorDialog;
import org.app.application.model.DialogItem;
import org.app.material.utils.AndroidUtilities;
import org.app.material.picker.date.DatePickerDialog;
import org.app.material.picker.time.RadialPickerLayout;
import org.app.material.picker.time.TimePickerDialog;
import org.app.material.widget.ColorView;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class DialogsFragment extends Fragment {

    private static final String TAG = DialogsFragment.class.getSimpleName();
    private ArrayList<DialogItem> dialogItems;

    private LaunchActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(getActivity());
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        mActivity = (LaunchActivity) getActivity();

        dialogItems = new ArrayList<>();
        dialogItems.add(new DialogItem("Shift Color Picker"));
        dialogItems.add(new DialogItem().setTitle("Primary Colors"));
        dialogItems.add(new DialogItem().setTitle("Primary Dark Colors"));
        dialogItems.add(new DialogItem().setTitle("Accent Colors"));
        dialogItems.add(new DialogItem().setTitle("Material Colors"));
        dialogItems.add(new DialogItem("Number Picker"));
        dialogItems.add(new DialogItem().setTitle("Number Picker"));
        dialogItems.add(new DialogItem().setTitle("String Picker"));
        dialogItems.add(new DialogItem(""));
        dialogItems.add(new DialogItem().setTitle("Holo Color Picker"));
        dialogItems.add(new DialogItem().setTitle("View Color Picker"));

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
                    PrimaryColorDialog dialog = PrimaryColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 2) {
                    PrimaryDarkColorDialog dialog = PrimaryDarkColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 3) {
                    AccentColorDialog dialog = AccentColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 4) {
                    MaterialColorDialog dialog = MaterialColorDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                }

                if (i == 6) {
                    NumberPickerDialog dialog = NumberPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 7) {
                    StringPickerDialog dialog = StringPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                }

                if (i == 9) {
                    HoloColorPickerDialog dialog = HoloColorPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == 10) {
                    ViewColorPickerDialog dialog = ViewColorPickerDialog.newInstance();
                    dialog.show(getFragmentManager(), TAG);
                }





                if (i == -2) {
                    BottomSheetDialog1 dialog = new BottomSheetDialog1();
                    dialog.show(getFragmentManager(), TAG);
                } else if (i == -3) {
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
                } else if (i == -15) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.RGB)
                            .indicatorMode(ColorView.IndicatorMode.HEX)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 16) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.ARGB)
                            .indicatorMode(ColorView.IndicatorMode.HEX)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 17) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.HSV)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 18) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.HSL)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 19 ) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
                            .colorMode(ColorView.ColorMode.CMYK)
                            .indicatorMode(ColorView.IndicatorMode.DECIMAL)
                            .create()
                            .show(getFragmentManager(), TAG);
                } else if (i == 20) {
                    new ColorPickerDialog.Builder()
                            .initialColor(AndroidUtilities.getThemeColor(R.attr.colorAccent))
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
            DialogItem item = dialogItems.get(i);

            return item.isNotTitle();
        }

        @Override
        public int getCount() {
            return dialogItems.size();
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

            DialogItem item = dialogItems.get(i);

            if (type == 0) {
                if (view == null) {
                    view = new TextCell(getActivity());
                }

                TextCell cell = (TextCell) view;
                cell.setText(item.getDialogTitle()).setDivider(true);

            } else if (type == 1) {
                if (view == null) {
                    view = new EmptyCell(getActivity());
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
            DialogItem item = dialogItems.get(i);

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
}