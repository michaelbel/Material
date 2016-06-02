package org.app.application.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.ColorPickerShift;
import org.app.material.widget.LayoutHelper;

public class ColorPickerAccentDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), Integer.toHexString(picker.getColor()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}