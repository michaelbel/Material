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

public class NumberPickerDialog extends DialogFragment {

    private NumberPicker picker;

    public static NumberPickerDialog newInstance() {
        return new NumberPickerDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        picker = new NumberPicker(getActivity());
        picker.setMinValue(0);
        picker.setMaxValue(100);
        picker.setValue(25);
        //picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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