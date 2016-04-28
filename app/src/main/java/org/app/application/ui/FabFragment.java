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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.application.ViewController;
import org.app.material.AndroidUtilities;
import org.app.material.FabButton;
import org.app.material.FabMenu;
import org.app.material.drawable.MediaControlDrawable;
import org.app.material.widget.LayoutHelper;

public class FabFragment extends Fragment implements View.OnClickListener {

    private boolean isFabPlusState = true;
    private boolean isFabMediaState = true;

    private FloatingActionButton fabPlus;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabMedia;

    private FabMenu fabMenu;

    private MediaControlDrawable mediaControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        fabPlus = new FloatingActionButton(getActivity());
        fabPlus.setOnClickListener(this);
        fabPlus.setImageResource(R.drawable.ic_plus);
        fabPlus.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 16, 16));
        layout.addView(fabPlus);

        fabEdit = new FloatingActionButton(getActivity());
        fabEdit.setOnClickListener(this);
        fabEdit.setImageResource(R.drawable.ic_edit);
        fabEdit.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 84, 16));
        layout.addView(fabEdit);

        mediaControl = new MediaControlDrawable.Builder(getActivity()).setInitialState(MediaControlDrawable.State.PAUSE).build();

        fabMedia = new FloatingActionButton(getActivity());
        fabMedia.setOnClickListener(this);
        fabMedia.setImageDrawable(mediaControl);
        fabMedia.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 152, 16));
        layout.addView(fabMedia);

        fabMenu = new FabMenu(getActivity());
        fabMenu.setIcon(AndroidUtilities.getIcon(getActivity(), R.drawable.ic_search, 0xFFFFFFFF));
        fabMenu.setMenuButtonColorNormal(0xFF000000);
        fabMenu.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        layout.addView(fabMenu);

        final FabButton programFab1 = new FabButton(getActivity());
        programFab1.setButtonSize(FabButton.SIZE_MINI);
        programFab1.setLabelText("Label text");
        programFab1.setImageResource(R.drawable.ic_edit);
        fabMenu.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                        ContextCompat.getColor(getActivity(), R.color.colorAccent),
                        ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            }
        });

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    Toast.makeText(getActivity(), fabMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                fabMenu.toggle(true);
            }
        });

        FabButton button = new FabButton(getActivity());
        button.setImageResource(R.drawable.ic_github);
        button.setButtonSize(FabButton.SIZE_NORMAL);
        button.setLabelText("Label");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        //layout.addView(button);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == fabPlus) {
            fabPlus.startAnimation(isFabPlusState ? toClose() : toPlus());
            isFabPlusState = !isFabPlusState;
        } else if (view == fabEdit) {
            startActivity(new Intent(getActivity(), ViewController.class));
        } else if (view == fabMedia) {
            mediaControl.setMediaControlState(getNextState(mediaControl.getMediaControlState()));
        }
    }

    private RotateAnimation toClose() {
        RotateAnimation rotate = new RotateAnimation(0, 135, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        rotate.setDuration(200);
        return rotate;
    }

    private RotateAnimation toPlus() {
        RotateAnimation rotate = new RotateAnimation(135, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        rotate.setDuration(200);
        return rotate;
    }

    private MediaControlDrawable.State getNextState(MediaControlDrawable.State state) {
        switch (state) {
            case PLAY:
                isFabMediaState = !isFabMediaState;
                return isFabMediaState ? MediaControlDrawable.State.PAUSE : MediaControlDrawable.State.STOP;
            case STOP:
                return isFabMediaState ? MediaControlDrawable.State.PLAY : MediaControlDrawable.State.PAUSE;
            case PAUSE:
                return isFabMediaState ? MediaControlDrawable.State.STOP : MediaControlDrawable.State.PLAY;
        }

        return null;
    }
}