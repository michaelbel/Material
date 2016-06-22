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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.AndroidUtilities;
import org.app.material.widget.ColorPickerShift;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.Palette;

public class ColorPickerPrimaryDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24), AndroidUtilities.dp(getActivity(), 24));

        final ColorPickerShift picker1 = new ColorPickerShift(getActivity());
        picker1.setColors(Palette.getBaseColors(getActivity()));
        picker1.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, 60));
        layout.addView(picker1);

        final ColorPickerShift picker2 = new ColorPickerShift(getActivity());
        picker2.setLayoutParams(LayoutHelper.makeLinear(getActivity(), LayoutHelper.MATCH_PARENT, 40, 0, 10, 0, 0));

        for (int i : picker1.getColors()) {
            for (int i2 : Palette.getColors(getActivity(), i)) {
                if (i2 == 0xff4CaF50) {
                    picker1.setSelectedColor(i);
                    picker2.setColors(Palette.getColors(getActivity(), i));
                    picker2.setSelectedColor(i2);
                    break;
                }
            }
        }
        layout.addView(picker2);

        picker1.setOnColorChangedListener(new ColorPickerShift.OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                picker2.setColors(Palette.getColors(getActivity(), picker1.getColor()));
                picker2.setSelectedColor(picker1.getColor());
            }
        });

        builder.setView(layout);
        builder.setTitle(R.string.PrimaryColor);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), Integer.toHexString(picker2.getColor()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}