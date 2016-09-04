package org.michaelbel.app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.michaelbel.app.R;
import org.michaelbel.material.Utils;
import org.michaelbel.material.widget.ColorPickerView;
import org.michaelbel.material.widget.LayoutHelper;

public class ViewColorPickerDialog extends DialogFragment {

    public static ViewColorPickerDialog newInstance() {
        return new ViewColorPickerDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final ColorPickerView picker = new ColorPickerView(getActivity());
        picker.setDensity(12);
        picker.setType(ColorPickerView.CIRCLE);
        picker.setInitialColor(Utils.getAttrColor(R.attr.colorAccent));
        layout.addView(picker, LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        builder.setView(layout);
        builder.setTitle(R.string.ColorPickerView);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getSelectedColor()),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}