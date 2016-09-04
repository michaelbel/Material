package org.michaelbel.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.michaelbel.app.R;
import org.michaelbel.material.Utils;
import org.michaelbel.material.widget.ColorPicker.ColorMode;
import org.michaelbel.material.widget.ColorPicker.IndicatorMode;
import org.michaelbel.material.widget.ColorView;

public class ColorPickerDialog extends DialogFragment {

    private final static String ARG_INITIAL_COLOR = "initial_color";
    private final static String ARG_COLOR_MODE = "color_mode";
    private final static String ARG_INDICATOR_MODE = "indicator_mode";

    private ColorView colorView;

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
        args.putInt(ARG_COLOR_MODE, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        return args;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(
                colorView.getInitialColor(),
                colorView.getColorMode(),
                colorView.getIndicatorMode())
        );

        super.onSaveInstanceState(outState);
    }

    public static class Builder {

        private Context context;
        private @ColorInt int initialColor;
        private ColorMode colorMode;
        private IndicatorMode indicatorMode;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setInitialColor(@ColorInt int color) {
            if (color == 0) {
                color = Utils.getAttrColor(context, R.attr.colorAccent);
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
        @ColorInt int initColor;
        ColorMode colorMode;
        IndicatorMode indicatorMode;

        if (savedInstanceState == null) {
            initColor = getArguments().getInt(ARG_INITIAL_COLOR);
            colorMode = ColorMode.values()[getArguments().getInt(ARG_COLOR_MODE)];
            indicatorMode = IndicatorMode.values()[getArguments().getInt(ARG_INDICATOR_MODE)];
        } else {
            initColor = savedInstanceState.getInt(ARG_INITIAL_COLOR, ColorView.DEFAULT_COLOR);
            colorMode = ColorMode.values()[savedInstanceState.getInt(ARG_COLOR_MODE)];
            indicatorMode = IndicatorMode.values()[savedInstanceState.getInt(ARG_INDICATOR_MODE)];
        }

        colorView = new ColorView(getContext(), initColor, colorMode, indicatorMode);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(colorView);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), Integer.toHexString(colorView.getInitialColor()),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}