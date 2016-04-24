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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;

public class FabFragment extends Fragment implements View.OnClickListener {

    private Boolean isFabOpen = true;

    private FloatingActionButton fabPlus;
    private FloatingActionButton fabEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        fabPlus = new FloatingActionButton(getActivity());
        fabPlus.setOnClickListener(this);
        fabPlus.setImageResource(R.drawable.ic_plus);
        layout.addView(fabPlus, LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 16, 16));

        fabEdit = new FloatingActionButton(getActivity());
        fabEdit.setOnClickListener(this);
        fabEdit.setImageResource(R.drawable.ic_edit);
        layout.addView(fabEdit, LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 84, 16));

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == fabPlus) {
            fabPlus.startAnimation(isFabOpen ? toClose() : toPlus());
            isFabOpen = !isFabOpen;
        }
    }

    private RotateAnimation toClose() {
        RotateAnimation rotate = new RotateAnimation(0, 135, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        rotate.setDuration(160);
        return rotate;
    }

    private RotateAnimation toPlus() {
        RotateAnimation rotate = new RotateAnimation(135, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        rotate.setDuration(160);
        return rotate;
    }
}