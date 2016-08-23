package org.app.application.dialogs.number;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.NumberPicker;

public class StringPickerDialog extends DialogFragment {

    private NumberPicker picker;

    public static StringPickerDialog newInstance() {
        return new StringPickerDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        picker = new NumberPicker(getContext());
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

        return builder.create();
    }
}