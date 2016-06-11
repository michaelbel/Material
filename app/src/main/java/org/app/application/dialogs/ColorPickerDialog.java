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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.ColorView;

public class ColorPickerDialog extends DialogFragment {

    private final static String ARG_INITIAL_COLOR = "arg_initial_color";
    private final static String ARG_COLOR_MODE_ID = "arg_color_mode_id";
    private final static String ARG_INDICATOR_MODE = "arg_indicator_mode";

    private ColorView mColorView;

    private static ColorPickerDialog newInstance(@ColorInt int initialColor, ColorView.ColorMode colorMode, ColorView.IndicatorMode indicatorMode) {
        ColorPickerDialog fragment = new ColorPickerDialog();
        fragment.setArguments(makeArgs(initialColor, colorMode, indicatorMode));
        return fragment;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorView.ColorMode colorMode, ColorView.IndicatorMode indicatorMode) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE_ID, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        return args;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(ColorView.getCurrentColor(), mColorView.getColorMode(), mColorView.getIndicatorMode()));
        super.onSaveInstanceState(outState);
    }

    public static class Builder {

        private @ColorInt int initialColor = ColorView.DEFAULT_COLOR;
        private ColorView.ColorMode colorMode = ColorView.DEFAULT_MODE;
        private ColorView.IndicatorMode indicatorMode = ColorView.IndicatorMode.DECIMAL;

        public Builder initialColor(@ColorInt int initialColor) {
            this.initialColor = initialColor;
            return this;
        }

        public Builder colorMode(ColorView.ColorMode colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        public Builder indicatorMode(ColorView.IndicatorMode indicatorMode) {
            this.indicatorMode = indicatorMode;
            return this;
        }

        public ColorPickerDialog create() {
            ColorPickerDialog fragment;
            fragment = newInstance(initialColor, colorMode, indicatorMode);
            return fragment;
        }
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mColorView = new ColorView(
                    getArguments().getInt(ARG_INITIAL_COLOR),
                    ColorView.ColorMode.values()[getArguments().getInt(ARG_COLOR_MODE_ID)],
                    ColorView.IndicatorMode.values()[getArguments().getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        } else {
            mColorView = new ColorView(
                    savedInstanceState.getInt(ARG_INITIAL_COLOR, ColorView.DEFAULT_COLOR),
                    ColorView.ColorMode.values()[savedInstanceState.getInt(ARG_COLOR_MODE_ID)],
                    ColorView.IndicatorMode.values()[savedInstanceState.getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mColorView);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), Integer.toHexString(ColorView.getCurrentColor()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}