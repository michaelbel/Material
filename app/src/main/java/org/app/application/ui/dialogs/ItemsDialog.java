package org.app.application.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.app.application.R;

public class ItemsDialog extends DialogFragment {

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(new CharSequence[]{
                getString(R.string.Winter),
                getString(R.string.Spring),
                getString(R.string.Summer),
                getString(R.string.Autumn)
        }, new DialogInterface.OnClickListener() {
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