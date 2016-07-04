/*
 * Copyright 2015 Michael Bel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.app.material.AndroidUtilities;
import org.app.material.widget.ColorPickerHolo;
import org.app.material.widget.LayoutHelper;

public class ColorPickerHoloDialog extends DialogFragment {

    private ColorPickerHolo picker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        picker = new ColorPickerHolo(getActivity());
        picker.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        layout.addView(picker);
        picker.setOldCenterColor(AndroidUtilities.getContextColor(R.attr.colorAccent));

        builder.setView(layout);
        builder.setTitle(R.string.ColorPickerHolo);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "" + Integer.toHexString(picker.getColor()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}