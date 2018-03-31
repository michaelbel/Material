package org.michaelbel.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.michaelbel.LaunchActivity;
import org.michaelbel.app.R;
import org.michaelbel.material.widget2.LayoutHelper;
import org.michaelbel.material.widget2.MediaControlDrawable;

public class FabFragment extends Fragment implements View.OnClickListener {

    private boolean isFabPlusState = true;
    private boolean isFabMediaState = true;

    private int fab = 56 + 16;
    private LaunchActivity activity;

    private FloatingActionButton fabPlus;
    private FloatingActionButton fabMedia;
    private FloatingActionButton fabCut;
    private FloatingActionButton fabCopy;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabMenu;

    private Drawable iconCut;
    private Drawable iconCopy;
    private Drawable iconDelete;
    private Drawable iconShare;
    private Drawable iconEdit;
    private MediaControlDrawable mMediaControl;

    private FrameLayout fragmentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LaunchActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        fragmentView = new FrameLayout(activity);
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        iconCut = ContextCompat.getDrawable(activity, R.drawable.ic_anim_cut);
        iconCopy = ContextCompat.getDrawable(activity, R.drawable.ic_anim_copy);
        iconDelete = ContextCompat.getDrawable(activity, R.drawable.ic_anim_delete);
        iconShare = ContextCompat.getDrawable(activity, R.drawable.ic_anim_share);
        iconEdit = ContextCompat.getDrawable(activity, R.drawable.ic_anim_edit);

        Drawable plusToClose = ContextCompat.getDrawable(activity, R.drawable.ic_plus);

        mMediaControl = new MediaControlDrawable.Builder(activity).setInitialState(MediaControlDrawable.State.PAUSE).build();

        fabCut = new FloatingActionButton(activity);
        fabCut.setOnClickListener(this);
        fabCut.setImageDrawable(iconCut);
        fabCut.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16, 16, 0, 0));
        fragmentView.addView(fabCut);

        fabCopy = new FloatingActionButton(activity);
        fabCopy.setOnClickListener(this);
        fabCopy.setImageDrawable(iconCopy);
        fabCopy.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16 + fab, 16, 0, 0));
        fragmentView.addView(fabCopy);

        fabDelete = new FloatingActionButton(activity);
        fabDelete.setOnClickListener(this);
        fabDelete.setImageDrawable(iconDelete);
        fabDelete.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16 + fab + fab, 16, 0, 0));
        fragmentView.addView(fabDelete);

        fabShare = new FloatingActionButton(activity);
        fabShare.setOnClickListener(this);
        fabShare.setImageDrawable(iconShare);
        fabShare.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16 + fab + fab + fab, 16, 0, 0));
        fragmentView.addView(fabShare);

        fabEdit = new FloatingActionButton(activity);
        fabEdit.setOnClickListener(this);
        fabEdit.setImageDrawable(iconEdit);
        fabEdit.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16, fab + 16, 0, 0));
        fragmentView.addView(fabEdit);

        fabMenu = new FloatingActionButton(activity);
        //fabMenu.setOnClickListener(this);
        //fabMenu.setImageDrawable(iconMenu);
        //fabMenu.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP, 16, 16 + 56, 0, 0));
        //fragmentView.addView(fabMenu);

        fabPlus = new FloatingActionButton(activity);
        fabPlus.setOnClickListener(this);
        fabPlus.setImageDrawable(plusToClose);
        fabPlus.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 16, 16));
        fragmentView.addView(fabPlus);

        fabMedia = new FloatingActionButton(activity);
        fabMedia.setOnClickListener(this);
        fabMedia.setImageDrawable(mMediaControl);
        fabMedia.setLayoutParams(LayoutHelper.makeFrame(activity, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END, 0, 0, 84, 16));
        fragmentView.addView(fabMedia);

        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        if (view == fabPlus) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(fabPlus, "rotation", isFabPlusState ? 0F : 135F, isFabPlusState ? 135F : 0);
            animator.setInterpolator(MediaControlDrawable.AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR);
            animator.setDuration(250);
            animator.start();

            isFabPlusState = !isFabPlusState;
        } else if (view == fabMedia) {
            mMediaControl.setMediaControlState(getNextState(mMediaControl.getMediaControlState()));
        } else if (view == fabCut) {
            if (iconCut instanceof Animatable) {
                ((Animatable) iconCut).start();
            }
        } else if (view == fabCopy) {
            if (iconCopy instanceof Animatable) {
                ((Animatable) iconCopy).start();
            }
        } else if (view == fabDelete) {
            if (iconDelete instanceof Animatable) {
                ((Animatable) iconDelete).start();
            }
        } else if (view == fabShare) {
            if (iconShare instanceof Animatable) {
                ((Animatable) iconShare).start();
            }
        } else if (view == fabEdit) {
            if (iconEdit instanceof Animatable) {
                ((Animatable) iconEdit).start();
            }

            ArgbEvaluator colorEvaluator = new ArgbEvaluator();
            ObjectAnimator colorAnimator = ObjectAnimator.ofObject(fragmentView, "backgroundColor", colorEvaluator, 0, 0);

            int currentColor = 0xFFF0F0F0;
            colorAnimator.setObjectValues(currentColor, Color.BLUE);
            colorAnimator.setDuration(300);
            colorAnimator.start();
        }
    }

    private MediaControlDrawable.State getNextState(MediaControlDrawable.State state) {
        switch (state) {
            case PLAY:
                isFabMediaState = !isFabMediaState;
                return isFabMediaState ?
                        MediaControlDrawable.State.PAUSE :
                        MediaControlDrawable.State.STOP;
            case STOP:
                return isFabMediaState ?
                        MediaControlDrawable.State.PLAY :
                        MediaControlDrawable.State.PAUSE;
            case PAUSE:
                return isFabMediaState ?
                        MediaControlDrawable.State.STOP :
                        MediaControlDrawable.State.PLAY;
        }

        return null;
    }
}