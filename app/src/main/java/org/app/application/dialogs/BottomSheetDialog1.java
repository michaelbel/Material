package org.app.application.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.BottomSheet;

public class BottomSheetDialog1 extends DialogFragment {

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
        builder.setTitle("Title");
        builder.setItems(new CharSequence[]{ getString(R.string.Monday), getString(R.string.Tuesday), getString(R.string.Wednesday), getString(R.string.Thursday) },
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                if (which == 0) {
                    Toast.makeText(getActivity(), getString(R.string.Monday), Toast.LENGTH_SHORT).show();
                } else if (which == 1) {
                    Toast.makeText(getActivity(), getString(R.string.Tuesday), Toast.LENGTH_SHORT).show();
                } else if (which == 2) {
                    Toast.makeText(getActivity(), getString(R.string.Wednesday), Toast.LENGTH_SHORT).show();
                } else if (which == 3) {
                    Toast.makeText(getActivity(), getString(R.string.Thursday), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}