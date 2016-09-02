package org.michaelbel.app.dialogs.shift;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.michaelbel.app.R;
import org.michaelbel.material.Utils;
import org.michaelbel.material.widget.ShiftColorPicker;
import org.michaelbel.material.widget.LayoutHelper;
import org.michaelbel.material.widget.Palette;

public class PrimaryColorDialog extends DialogFragment {

    private ShiftColorPicker picker;

    public static PrimaryColorDialog newInstance() {
        return new PrimaryColorDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setPadding(Utils.dp(24), Utils.dp(24), Utils.dp(24),
                Utils.dp(24));

        picker = new ShiftColorPicker(getContext());
        picker.setLayoutParams(LayoutHelper.makeRelative(getContext(), LayoutHelper.MATCH_PARENT, 60));
        picker.setColors(Palette.PrimaryColors(getContext()));
        picker.setSelectedColor(ContextCompat.getColor(getContext(), R.color.primary_red));
        layout.addView(picker);

        builder.setView(layout);
        builder.setTitle("Primary Color");
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), Integer.toHexString(picker.getColor()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}