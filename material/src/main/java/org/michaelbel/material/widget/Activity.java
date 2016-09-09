package org.michaelbel.material.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Activity extends FragmentActivity {

    private static final String TAG = Activity.class.getSimpleName();

    protected Dialog alertDialog = null;

    public Dialog showDialog(Dialog dialog) {
        try {
            alertDialog = dialog;
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    alertDialog = null;
                }
            });
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return null;
    }
}