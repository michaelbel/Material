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

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import org.app.application.R;
import org.app.material.widget.LayoutHelper;
import org.app.material.widget.MediaControlDrawable;

public class FabFragment extends Fragment implements View.OnClickListener {

    private boolean isFabPlusState = true;
    private boolean isFabMediaState = true;

    private FloatingActionButton mFabPlus;
    private FloatingActionButton mFabEdit;
    private FloatingActionButton mFabMedia;

    private MediaControlDrawable mMediaControl;

    private ObjectAnimator mObjectAnimator;
    private final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

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
            if (isFabPlusState) {
                startAnimation();
            } else {
                endAnimation();
            }

            isFabPlusState = !isFabPlusState;
        } else if (view == mFabEdit) {

        } else if (view == mFabMedia) {
            mMediaControl.setMediaControlState(getNextState(mMediaControl.getMediaControlState()));
        }
    }

    private ObjectAnimator startAnimation() {
        mObjectAnimator = ObjectAnimator.ofFloat(mFabPlus, "rotation", 0F, 135F);
        mObjectAnimator.setInterpolator(INTERPOLATOR);
        mObjectAnimator.setDuration(250);
        mObjectAnimator.start();
        return mObjectAnimator;
    }

    private ObjectAnimator endAnimation() {
        mObjectAnimator = ObjectAnimator.ofFloat(mFabPlus, "rotation", 135F, 0F);
        mObjectAnimator.setInterpolator(INTERPOLATOR);
        mObjectAnimator.setDuration(250);
        mObjectAnimator.start();
        return mObjectAnimator;
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