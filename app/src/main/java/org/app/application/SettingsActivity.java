package org.app.application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.material.AndroidUtilities;
import org.app.material.timepicker.date.DatePickerDialog;
import org.app.material.timepicker.time.RadialPickerLayout;
import org.app.material.timepicker.time.TimePickerDialog;
import org.app.material.widget.ActionBar;
import org.app.material.widget.ActionBarMenu;

import java.util.Calendar;

public class SettingsActivity extends FragmentActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private FrameLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new FrameLayout(this);
        layout.setId(R.id.layout);
        layout.setBackgroundColor(0xFFF0F0F0);

        ActionBar actionBar = new ActionBar(this)
                .setBackGroundColor(AndroidUtilities.getContextColor(R.attr.colorPrimary))
                .setBackButtonImage(R.drawable.ic_arrow_back)
                .setTitle(R.string.Settings)
                .setOccupyStatusBar(false)
                .setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                    @Override
                    public void onItemClick(int id) {
                        if (id == -1) {
                            //finish();
                            Calendar now = Calendar.getInstance();
                            TimePickerDialog tpd = TimePickerDialog.newInstance(
                                    SettingsActivity.this,
                                    now.get(Calendar.HOUR_OF_DAY),
                                    now.get(Calendar.MINUTE),
                                    true
                            );
                            tpd.setThemeDark(false);
                            tpd.vibrate(false);
                            tpd.dismissOnPause(false);
                            tpd.enableSeconds(false);
                            //tpd.setAccentColor(Color.parseColor("#9C27B0"));
                            //tpd.setTitle("TimePicker Title");
                            //tpd.setTimeInterval(2, 5, 10);
                            tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    Log.d("TimePicker", "Dialog was cancelled");
                                }
                            });
                            tpd.show(getFragmentManager(), "Timepickerdialog");
                        } else if (id == 2) {
                            Calendar now = Calendar.getInstance();
                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    SettingsActivity.this,
                                    now.get(Calendar.YEAR),
                                    now.get(Calendar.MONTH),
                                    now.get(Calendar.DAY_OF_MONTH)
                            );
                            //dpd.setThemeDark(modeDarkDate.isChecked());
                            //dpd.vibrate(vibrateDate.isChecked());
                            //dpd.dismissOnPause(dismissDate.isChecked());
                            //dpd.showYearPickerFirst(showYearFirst.isChecked());
                    /*if (modeCustomAccentDate.isChecked()) {
                        dpd.setAccentColor(Color.parseColor("#9C27B0"));
                    }*/
                    /*if (titleDate.isChecked()) {
                        dpd.setTitle("DatePicker Title");
                    }*/
                    /*if (limitDates.isChecked()) {
                        Calendar[] dates = new Calendar[13];
                        for(int i = -6; i <= 6; i++) {
                            Calendar date = Calendar.getInstance();
                            date.add(Calendar.MONTH, i);
                            dates[i+6] = date;
                        }
                        dpd.setSelectableDays(dates);
                    }*/
                    /*if (highlightDates.isChecked()) {
                        Calendar[] dates = new Calendar[13];
                        for(int i = -6; i <= 6; i++) {
                            Calendar date = Calendar.getInstance();
                            date.add(Calendar.WEEK_OF_YEAR, i);
                            dates[i+6] = date;
                        }
                        dpd.setHighlightedDays(dates);
                    }*/
                            dpd.show(getFragmentManager(), "Datepickerdialog");
                            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
                                }
                            });
                        }
                    }
                });
        actionBar.setElevation(AndroidUtilities.dp(2.0F));

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(2, R.drawable.ic_github);

        layout.addView(actionBar);

        setContentView(layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void presentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .commit();
    }

    public void presentFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void presentFragment(Fragment fragment, Bundle args, String tag) {
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void finishFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        Toast.makeText(SettingsActivity.this, time, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        Toast.makeText(SettingsActivity.this, date, Toast.LENGTH_SHORT).show();
    }
}