package org.michaelbel.fragment;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
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

    private boolean isChecked;
    private final int fabSize = 56;
    private boolean isDownloading;
    private long downloadingStartTimeMillis;
    private boolean isCompleteAnimationPending;
    private LaunchActivity activity;

    private FloatingActionButton fabPlus;
    private FloatingActionButton fabMedia;
    private FloatingActionButton fabCut;
    private FloatingActionButton fabCopy;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabTimer;
    private FloatingActionButton fabClock;
    private FloatingActionButton fabAlarm;
    private FloatingActionButton fabStopwatch;
    private FloatingActionButton fabMusicPrev;
    private FloatingActionButton fabMusicNext;
    private FloatingActionButton fabDrawer;
    private FloatingActionButton fabCrossTick;
    private FloatingActionButton fabPlusMinus;
    private FloatingActionButton fabArrowDots;
    private FloatingActionButton fabRadio;
    private FloatingActionButton fabCheckbox;
    private FloatingActionButton fabExpandCollapse;
    private FloatingActionButton fabDownloading;
    private FloatingActionButton fabAirplane;
    private FloatingActionButton fabEye;
    private FloatingActionButton fabFlashlight;
    private FloatingActionButton fabSearchBack;
    private FloatingActionButton fabHeart;

    private Drawable iconCut;
    private Drawable iconCopy;
    private Drawable iconDelete;
    private Drawable iconShare;
    private Drawable iconEdit;
    private Drawable iconTimer;
    private Drawable iconClock;
    private Drawable iconAlarm;
    private Drawable iconStopwatch;
    private Drawable iconMusicPrev;
    private Drawable iconMusicNext;
    private Drawable iconDrawer;
    private Drawable iconCrossTick;
    private Drawable iconPlusMinus;
    private Drawable iconArrowDots;
    private Drawable iconRadio;
    private Drawable iconCheckbox;
    private Drawable iconExpandCollapse;
    private Drawable iconDownloading;
    private Drawable iconAirplane;
    private Drawable iconEye;
    private Drawable iconFlashlight;
    private Drawable iconSearchBack;
    private Drawable iconHeart;

    private MediaControlDrawable mMediaControl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LaunchActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        FrameLayout fragmentView = new FrameLayout(activity);
        fragmentView.setBackgroundColor(0xFFF0F0F0);

        iconCut = ContextCompat.getDrawable(activity, R.drawable.ic_anim_cut);
        iconCopy = ContextCompat.getDrawable(activity, R.drawable.ic_anim_copy);
        iconDelete = ContextCompat.getDrawable(activity, R.drawable.ic_anim_delete);
        iconShare = ContextCompat.getDrawable(activity, R.drawable.ic_anim_share);
        iconEdit = ContextCompat.getDrawable(activity, R.drawable.ic_anim_edit);
        iconTimer = ContextCompat.getDrawable(activity, R.drawable.ic_anim_timer);
        iconClock = ContextCompat.getDrawable(activity, R.drawable.ic_anim_clock);
        iconAlarm = ContextCompat.getDrawable(activity, R.drawable.ic_anim_alarm);
        iconStopwatch = ContextCompat.getDrawable(activity, R.drawable.ic_anim_stopwatch);
        iconMusicPrev = ContextCompat.getDrawable(activity, R.drawable.ic_anim_music_prev);
        iconMusicNext = ContextCompat.getDrawable(activity, R.drawable.ic_anim_music_next);
        iconDrawer = ContextCompat.getDrawable(activity, R.drawable.ic_anim_drawer);
        iconCrossTick = ContextCompat.getDrawable(activity, R.drawable.ic_anim_crosstick);
        iconPlusMinus = ContextCompat.getDrawable(activity, R.drawable.ic_anim_plusminus);
        iconArrowDots = ContextCompat.getDrawable(activity, R.drawable.ic_anim_arrowoverflow);
        iconRadio = ContextCompat.getDrawable(activity, R.drawable.ic_anim_radiobutton);
        iconCheckbox = ContextCompat.getDrawable(activity, R.drawable.ic_anim_checkbox);
        iconExpandCollapse = ContextCompat.getDrawable(activity, R.drawable.ic_anim_expandcollapse);
        iconDownloading = ContextCompat.getDrawable(activity, R.drawable.ic_anim_downloading_begin);
        iconAirplane = ContextCompat.getDrawable(activity, R.drawable.ic_anim_airplane);
        iconEye = ContextCompat.getDrawable(activity, R.drawable.ic_anim_eye);
        iconFlashlight = ContextCompat.getDrawable(activity, R.drawable.ic_anim_flashlight);
        iconSearchBack = ContextCompat.getDrawable(activity, R.drawable.ic_anim_searchback);
        iconHeart = ContextCompat.getDrawable(activity, R.drawable.ic_anim_heart);

        Drawable plusToClose = ContextCompat.getDrawable(activity, R.drawable.ic_plus);
        mMediaControl = new MediaControlDrawable.Builder(activity).setInitialState(MediaControlDrawable.State.PAUSE).build();

//--------------------------------------------------------------------------------------------------

        fabCut = new FloatingActionButton(activity);
        fabCut.setOnClickListener(this);
        fabCut.setImageDrawable(iconCut);
        fabCut.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16, 0, 0));
        fragmentView.addView(fabCut);

        fabCopy = new FloatingActionButton(activity);
        fabCopy.setOnClickListener(this);
        fabCopy.setImageDrawable(iconCopy);
        fabCopy.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16, 0, 0));
        fragmentView.addView(fabCopy);

        fabDelete = new FloatingActionButton(activity);
        fabDelete.setOnClickListener(this);
        fabDelete.setImageDrawable(iconDelete);
        fabDelete.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16, 0, 0));
        fragmentView.addView(fabDelete);

        fabShare = new FloatingActionButton(activity);
        fabShare.setOnClickListener(this);
        fabShare.setImageDrawable(iconShare);
        fabShare.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16, 0, 0));
        fragmentView.addView(fabShare);

//--------------------------------------------------------------------------------------------------

        fabEdit = new FloatingActionButton(activity);
        fabEdit.setOnClickListener(this);
        fabEdit.setImageDrawable(iconEdit);
        fabEdit.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16 + 56, 0, 0));
        fragmentView.addView(fabEdit);

        fabTimer = new FloatingActionButton(activity);
        fabTimer.setOnClickListener(this);
        fabTimer.setImageDrawable(iconTimer);
        fabTimer.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16 + 56, 0, 0));
        fragmentView.addView(fabTimer);

        fabClock = new FloatingActionButton(activity);
        fabClock.setOnClickListener(this);
        fabClock.setImageDrawable(iconClock);
        fabClock.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16 + 56, 0, 0));
        fragmentView.addView(fabClock);

        fabAlarm = new FloatingActionButton(activity);
        fabAlarm.setOnClickListener(this);
        fabAlarm.setImageDrawable(iconAlarm);
        fabAlarm.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16 + 56, 0, 0));
        fragmentView.addView(fabAlarm);

//--------------------------------------------------------------------------------------------------

        fabStopwatch = new FloatingActionButton(activity);
        fabStopwatch.setOnClickListener(this);
        fabStopwatch.setImageDrawable(iconStopwatch);
        fabStopwatch.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16 + 56 + 56, 0, 0));
        fragmentView.addView(fabStopwatch);

        fabMusicPrev = new FloatingActionButton(activity);
        fabMusicPrev.setOnClickListener(this);
        fabMusicPrev.setImageDrawable(iconMusicPrev);
        fabMusicPrev.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16 + 56 + 56, 0, 0));
        fragmentView.addView(fabMusicPrev);

        fabMusicNext = new FloatingActionButton(activity);
        fabMusicNext.setOnClickListener(this);
        fabMusicNext.setImageDrawable(iconMusicNext);
        fabMusicNext.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16 + 56 + 56, 0, 0));
        fragmentView.addView(fabMusicNext);

        fabDrawer = new FloatingActionButton(activity);
        fabDrawer.setOnClickListener(this);
        fabDrawer.setImageDrawable(iconDrawer);
        fabDrawer.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16 + 56 + 56, 0, 0));
        fragmentView.addView(fabDrawer);

//--------------------------------------------------------------------------------------------------

        fabCrossTick = new FloatingActionButton(activity);
        fabCrossTick.setOnClickListener(this);
        fabCrossTick.setImageDrawable(iconCrossTick);
        fabCrossTick.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabCrossTick);

        fabPlusMinus = new FloatingActionButton(activity);
        fabPlusMinus.setOnClickListener(this);
        fabPlusMinus.setImageDrawable(iconPlusMinus);
        fabPlusMinus.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabPlusMinus);

        fabArrowDots = new FloatingActionButton(activity);
        fabArrowDots.setOnClickListener(this);
        fabArrowDots.setImageDrawable(iconArrowDots);
        fabArrowDots.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabArrowDots);

        fabRadio = new FloatingActionButton(activity);
        fabRadio.setOnClickListener(this);
        fabRadio.setImageDrawable(iconRadio);
        fabRadio.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabRadio);

//--------------------------------------------------------------------------------------------------

        fabCheckbox = new FloatingActionButton(activity);
        fabCheckbox.setOnClickListener(this);
        fabCheckbox.setImageDrawable(iconCheckbox);
        fabCheckbox.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabCheckbox);

        fabExpandCollapse = new FloatingActionButton(activity);
        fabExpandCollapse.setOnClickListener(this);
        fabExpandCollapse.setImageDrawable(iconExpandCollapse);
        fabExpandCollapse.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabExpandCollapse);

        fabDownloading = new FloatingActionButton(activity);
        fabDownloading.setOnClickListener(this);
        fabDownloading.setImageDrawable(iconDownloading);
        fabDownloading.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabDownloading);

        fabAirplane = new FloatingActionButton(activity);
        fabAirplane.setOnClickListener(this);
        fabAirplane.setImageDrawable(iconAirplane);
        fabAirplane.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabAirplane);

//--------------------------------------------------------------------------------------------------

        fabEye = new FloatingActionButton(activity);
        fabEye.setOnClickListener(this);
        fabEye.setImageDrawable(iconEye);
        fabEye.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16, 16 + 56 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabEye);

        fabFlashlight = new FloatingActionButton(activity);
        fabFlashlight.setOnClickListener(this);
        fabFlashlight.setImageDrawable(iconFlashlight);
        fabFlashlight.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56, 16 + 56 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabFlashlight);

        fabSearchBack = new FloatingActionButton(activity);
        fabSearchBack.setOnClickListener(this);
        fabSearchBack.setImageDrawable(iconSearchBack);
        fabSearchBack.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56, 16 + 56 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabSearchBack);

        fabHeart = new FloatingActionButton(activity);
        fabHeart.setOnClickListener(this);
        fabHeart.setImageDrawable(iconHeart);
        fabHeart.setLayoutParams(LayoutHelper.makeFrame(activity, fabSize, fabSize,Gravity.START | Gravity.TOP,
                16 + 56 + 56 + 56, 16 + 56 + 56 + 56 + 56 + 56, 0, 0));
        fragmentView.addView(fabHeart);

//--------------------------------------------------------------------------------------------------

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
        } else if (view == fabTimer) {
            if (iconTimer instanceof Animatable) {
                ((Animatable) iconTimer).start();
            }
        } else if (view == fabClock) {
            if (iconClock instanceof Animatable) {
                ((Animatable) iconClock).start();
            }
        } else if (view == fabAlarm) {
            if (iconAlarm instanceof Animatable) {
                ((Animatable) iconAlarm).start();
            }
        } else if (view == fabStopwatch) {
            if (iconStopwatch instanceof Animatable) {
                ((Animatable) iconStopwatch).start();
            }
        } else if (view == fabMusicPrev) {
            if (iconMusicPrev instanceof Animatable) {
                ((Animatable) iconMusicPrev).start();
            }
        } else if (view == fabMusicNext) {
            if (iconMusicNext instanceof Animatable) {
                ((Animatable) iconMusicNext).start();
            }
        } else if (view == fabDrawer) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabDrawer.setImageState(stateSet, true);
        } else if (view == fabPlusMinus) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabPlusMinus.setImageState(stateSet, true);
        } else if (view == fabCrossTick) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabCrossTick.setImageState(stateSet, true);
        } else if (view == fabArrowDots) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabArrowDots.setImageState(stateSet, true);
        } else if (view == fabRadio) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabRadio.setImageState(stateSet, true);
        } else if (view == fabCheckbox) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabCheckbox.setImageState(stateSet, true);
        } else if (view == fabExpandCollapse) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabExpandCollapse.setImageState(stateSet, true);
        } else if (view == fabDownloading) {
            if (isCompleteAnimationPending) {
                return;
            }
            if (isDownloading) {
                final long delayMillis = 2666 - ((System.currentTimeMillis() - downloadingStartTimeMillis) % 2666);
                fabDownloading.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swapAnimation(R.drawable.avd_downloading_finish);
                        isCompleteAnimationPending = false;
                    }
                }, delayMillis);
                isCompleteAnimationPending = true;
            } else {
                swapAnimation(R.drawable.ic_anim_downloading_begin);
                downloadingStartTimeMillis = System.currentTimeMillis();
            }
            isDownloading = !isDownloading;
        } else if (view == fabAirplane) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabAirplane.setImageState(stateSet, true);
        } else if (view == fabEye) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabEye.setImageState(stateSet, true);
        } else if (view == fabFlashlight) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabFlashlight.setImageState(stateSet, true);
        } else if (view == fabSearchBack) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabSearchBack.setImageState(stateSet, true);
        } else if (view == fabHeart) {
            isChecked = !isChecked;
            final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
            fabHeart.setImageState(stateSet, true);
        }
    }

    private void swapAnimation(@DrawableRes int drawableResId) {
        final Drawable avd = AnimatedVectorDrawableCompat.create(activity, drawableResId);
        fabDownloading.setImageDrawable(avd);
        ((Animatable) avd).start();
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