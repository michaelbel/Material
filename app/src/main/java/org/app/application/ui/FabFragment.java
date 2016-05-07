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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.application.ViewController;
import org.app.material.drawable.MediaControlDrawable;
import org.app.material.widget.LayoutHelper;

public class FabFragment extends Fragment implements View.OnClickListener {

    private boolean isFabPlusState = true;
    private boolean isFabMediaState = true;

    private FloatingActionButton mFabPlus;
    private FloatingActionButton mFabEdit;
    private FloatingActionButton mFabMedia;

    private MediaControlDrawable mMediaControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFECEFF1);

        mFabPlus = new FloatingActionButton(getActivity());
        mFabPlus.setOnClickListener(this);
        mFabPlus.setImageResource(R.drawable.ic_plus);
        mFabPlus.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 16, 16));
        layout.addView(mFabPlus);

        mFabEdit = new FloatingActionButton(getActivity());
        mFabEdit.setOnClickListener(this);
        mFabEdit.setImageResource(R.drawable.ic_edit);
        mFabEdit.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 84, 16));
        layout.addView(mFabEdit);

        mMediaControl = new MediaControlDrawable.Builder(getActivity()).setInitialState(MediaControlDrawable.State.PAUSE).build();

        mFabMedia = new FloatingActionButton(getActivity());
        mFabMedia.setOnClickListener(this);
        mFabMedia.setImageDrawable(mMediaControl);
        mFabMedia.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 152, 16));
        layout.addView(mFabMedia);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == mFabPlus) {
            mFabPlus.startAnimation(isFabPlusState ? toClose() : toPlus());
            isFabPlusState = !isFabPlusState;
        } else if (view == mFabEdit) {
            startActivity(new Intent(getActivity(), ViewController.class));
        } else if (view == mFabMedia) {
            mMediaControl.setMediaControlState(getNextState(mMediaControl.getMediaControlState()));
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