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
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.NumberPicker;

public class NumberPickerDialog extends DialogFragment {

    private final static String ARG_VALUE = "arg_initial_value";
    private final static String ARG_MIN_VALUE = "arg_min_value";
    private final static String ARG_MAX_VALUE = "arg_max_value";

    private static NumberPicker picker;

    private static NumberPickerDialog newInstance(int value, int minValue, int maxValue) {
        NumberPickerDialog fragment = new NumberPickerDialog();
        fragment.setArguments(makeArgs(value, minValue, maxValue));
        return fragment;
    }

    private static Bundle makeArgs(int value, int minValue, int maxValue) {
        Bundle args = new Bundle();
        args.putInt(ARG_VALUE, value);
        args.putInt(ARG_MIN_VALUE, minValue);
        args.putInt(ARG_MAX_VALUE, maxValue);
        return args;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(picker.getValue(), picker.getMinValue(), picker.getMaxValue()));
        super.onSaveInstanceState(outState);
    }

    public static class Builder {

        private int mValue;
        private int mMinValue;
        private int mMaxValue;

        public Builder withValue(int value) {
            this.mValue = value;
            return this;
        }

        public Builder withMinValue(int minvalue) {
            this.mMinValue = minvalue;
            return this;
        }

        public Builder withMaxValue(int maxvalue) {
            this.mMaxValue = maxvalue;
            return this;
        }

        public NumberPickerDialog create() {
            NumberPickerDialog fragment;
            fragment = newInstance(mValue, mMinValue, mMaxValue);
            return fragment;
        }
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            picker = new NumberPicker(getActivity());
            picker.setValue(getArguments().getInt(ARG_VALUE));
            picker.setMinValue(getArguments().getInt(ARG_MIN_VALUE));
            picker.setMaxValue(getArguments().getInt(ARG_MAX_VALUE));
        } else {
            picker = new NumberPicker(getActivity());
            picker.setValue(savedInstanceState.getInt(ARG_VALUE, 0));
            picker.setMinValue(savedInstanceState.getInt(ARG_MIN_VALUE, 0));
            picker.setMaxValue(savedInstanceState.getInt(ARG_MAX_VALUE, 0));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        builder.setView(picker);
        builder.setTitle(R.string.NumberPicker);
        builder.setNegativeButton(R.string.Cancel, null);
        builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), getString(R.string.Value, picker.getValue()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}