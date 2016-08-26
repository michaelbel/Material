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
import org.app.material.utils.Color;
import org.app.material.widget.ColorPicker.ColorMode;
import org.app.material.widget.ColorPicker.IndicatorMode;
import org.app.material.widget.ColorView;

public class ColorPickerDialog extends DialogFragment {

    public int DEFAULT_COLOR = Color.getThemeColor(getContext(), R.attr.colorAccent);

    private final static String ARG_INITIAL_COLOR = "arg_initial_color";
    private final static String ARG_COLOR_MODE_ID = "arg_color_mode_id";
    private final static String ARG_INDICATOR_MODE = "arg_indicator_mode";

    private ColorView mColorView;

    private static ColorPickerDialog newInstance(@ColorInt int initialColor,
                                                 ColorMode colorMode,
                                                 IndicatorMode indicatorMode) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        dialog.setArguments(makeArgs(initialColor, colorMode, indicatorMode));
        return dialog;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorMode colorMode,
                                   IndicatorMode indicatorMode) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE_ID, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        return args;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(ColorView.getCurrentColor(), mColorView.getColorMode(),
                mColorView.getIndicatorMode()));
        super.onSaveInstanceState(outState);
    }

    public static class Builder {

        private @ColorInt int initialColor;
        private ColorMode colorMode;
        private IndicatorMode indicatorMode;

        public Builder setInitialColor(@ColorInt int color) {
            if (color == 0) {
                color = 0xFFFF5252;
            }

            this.initialColor = color;
            return this;
        }

        public Builder setColorMode(ColorMode mode) {
            if (mode == null) {
                mode = ColorMode.RGB;
            }

            this.colorMode = mode;
            return this;
        }

        public Builder setIndicatorMode(IndicatorMode mode) {
            if (mode == null) {
                mode = IndicatorMode.DECIMAL;
            }

            this.indicatorMode = mode;
            return this;
        }

        public ColorPickerDialog create() {
            ColorPickerDialog dialog;
            dialog = newInstance(initialColor, colorMode, indicatorMode);
            return dialog;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mColorView = new ColorView(
                    getArguments().getInt(ARG_INITIAL_COLOR),
                    ColorMode.values()[getArguments().getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[getArguments().getInt(ARG_INDICATOR_MODE)],
                    getContext());
        } else {
            mColorView = new ColorView(
                    savedInstanceState.getInt(ARG_INITIAL_COLOR, ColorView.DEFAULT_COLOR),
                    ColorMode.values()[savedInstanceState.getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[savedInstanceState.getInt(ARG_INDICATOR_MODE)],
                    getContext());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(mColorView);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), Integer.toHexString(ColorView.getCurrentColor()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}