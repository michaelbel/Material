/*
 * Copyright 2016 Michael Bel
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

package org.app.application.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.BottomPickerLayout;
import org.app.material.widget.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class BottomsFragment extends Fragment implements View.OnClickListener {

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private List<Button> buttons = new ArrayList<>();

    private boolean plVisible = false;

    private BottomPickerLayout pickerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        mButton1 = new Button(getActivity());
        mButton1.setText(R.string.BottomPickerLayout);
        layout.addView(mButton1);

        mButton2 = new Button(getActivity());
        mButton2.setText("");
        //layout.addView(mButton2);

        mButton3 = new Button(getActivity());
        mButton3.setText("");
        //layout.addView(mButton3);

        buttons.add(mButton1);
        buttons.add(mButton2);
        buttons.add(mButton3);

        for (Button button : buttons) {
            button.setOnClickListener(this);
            button.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        }

        pickerLayout = new BottomPickerLayout(getActivity());
        pickerLayout.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, 48, Gravity.BOTTOM));
        pickerLayout.setPositiveButton(R.string.Done, new BottomPickerLayout.ClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        pickerLayout.setNegativeButton(R.string.Cancel, new BottomPickerLayout.ClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        layout.addView(pickerLayout);
        return layout;
    }

    @Override
    public void onClick(View v) {
        if (v == mButton1) {
            plVisible = !plVisible;

            if (plVisible) {
                pickerLayout.hide();
            } else {
                pickerLayout.show();
            }
        } else if (v == mButton2) {

        } else if (v == mButton3) {

        }
    }
}