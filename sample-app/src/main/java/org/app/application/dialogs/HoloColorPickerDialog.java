package org.app.application.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.utils.AndroidUtilities;
import org.app.material.widget.LayoutHelper;

public class HoloColorPickerDialog extends DialogFragment {

    private org.app.material.widget.HoloColorPicker picker;

    public static HoloColorPickerDialog newInstance() {
        return new HoloColorPickerDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        picker = new org.app.material.widget.HoloColorPicker(getActivity());
        picker.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        layout.addView(picker);
        picker.setOldCenterColor(AndroidUtilities.getThemeColor(R.attr.colorAccent));

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