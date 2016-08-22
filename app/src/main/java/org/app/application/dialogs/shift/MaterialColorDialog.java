package org.app.application.dialogs.shift;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.utils.AndroidUtilities;
import org.app.material.widget.ColorPickerShift;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Palette;

public class MaterialColorDialog extends DialogFragment {

    private ColorPickerShift picker1;
    private ColorPickerShift picker2;

    public static MaterialColorDialog newInstance() {
        return new MaterialColorDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT,
                LayoutHelper.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(24), AndroidUtilities.dp(24), 0);

        picker1 = new ColorPickerShift(getContext());
        picker1.setColors(Palette.PrimaryColors(getContext()));
        picker1.setLayoutParams(LayoutHelper.makeLinear(getContext(), LayoutHelper.MATCH_PARENT, 60));
        layout.addView(picker1);

        picker2 = new ColorPickerShift(getContext());
        picker2.setLayoutParams(LayoutHelper.makeLinear(getContext(), LayoutHelper.MATCH_PARENT, 40, 0, 10, 0, 0));

        for (int i : picker1.getColors()) {
            for (int i2 : Palette.MaterialColors(getActivity(), i)) {
                if (i2 == 0xff4CaF50) {
                    picker1.setSelectedColor(i);
                    picker2.setColors(Palette.MaterialColors(getContext(), i));
                    picker2.setSelectedColor(i2);
                    break;
                }
            }
        }
        layout.addView(picker2);

        picker1.setOnColorChangedListener(new ColorPickerShift.OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                picker2.setColors(Palette.MaterialColors(getContext(), picker1.getColor()));
                picker2.setSelectedColor(picker1.getColor());
            }
        });

        builder.setView(layout);
        builder.setTitle(R.string.PrimaryColor);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), Integer.toHexString(picker2.getColor()),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}