package org.michaelbel.app.dialogs.bottom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import org.michaelbel.material.widget.BottomSheet;

public class BottomSheetDialog1 extends DialogFragment {

    public static BottomSheetDialog1 newInstance() {
        return new BottomSheetDialog1();
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] strings = new CharSequence[] {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday"
        };

        BottomSheet.Builder builder = new BottomSheet.Builder(getContext());
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                Toast.makeText(getContext(), strings[which], Toast.LENGTH_SHORT).show();
                /*if (which == 0) {
                    Toast.makeText(getContext(), getString(R.string.Monday), Toast.LENGTH_SHORT).show();
                } else if (which == 1) {
                    Toast.makeText(getContext(), getString(R.string.Tuesday), Toast.LENGTH_SHORT).show();
                } else if (which == 2) {
                    Toast.makeText(getContext(), getString(R.string.Wednesday), Toast.LENGTH_SHORT).show();
                } else if (which == 3) {
                    Toast.makeText(getContext(), getString(R.string.Thursday), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        return builder.create();
    }
}