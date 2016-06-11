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

package org.app.application.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.dialogs.BottomSheetDialog;
import org.app.material.widget.BottomPickerLayout;
import org.app.material.widget.LayoutHelper;

public class BottomsFragment extends Fragment implements View.OnClickListener {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;

    private boolean plVisible = false;

    private BottomPickerLayout pickerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        mButton1 = new Button(getActivity());
        mButton1.setOnClickListener(this);
        mButton1.setText(R.string.BottomBar);
        mButton1.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1, 0, 0));
        layout.addView(mButton1);

        mButton2 = new Button(getActivity());
        mButton2.setOnClickListener(this);
        mButton2.setText(R.string.BottomSheetDialog);
        mButton2.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 45, 0, 0));
        layout.addView(mButton2);

        mButton3 = new Button(getActivity());
        mButton3.setOnClickListener(this);
        mButton3.setText(R.string.BottomPickerLayout);
        mButton3.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 90, 0, 0));
        layout.addView(mButton3);

        pickerLayout = new BottomPickerLayout(getActivity());
        pickerLayout.setVisibility(View.INVISIBLE);
        pickerLayout.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));
        pickerLayout.setPositiveButton(R.string.Done, new BottomPickerLayout.ClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.Done), Toast.LENGTH_SHORT).show();
            }
        });
        pickerLayout.setNegativeButton(R.string.Cancel, new BottomPickerLayout.ClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.Cancel), Toast.LENGTH_SHORT).show();
            }
        });

        layout.addView(pickerLayout);

        return layout;
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            Toast.makeText(getActivity(), "Not implemented", Toast.LENGTH_SHORT).show();
        } else if (v == mButton2) {
            DialogFragment dialog = new BottomSheetDialog();
            dialog.show(getFragmentManager(), "bottomSheet");
        } else if (v == mButton3){
            plVisible = !plVisible;

            if (plVisible) {
                pickerLayout.setVisibility(View.VISIBLE);
            } else {
                pickerLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}