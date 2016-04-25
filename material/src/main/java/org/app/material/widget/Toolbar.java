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

package org.app.material.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

import java.util.ArrayList;

public class Toolbar extends FrameLayout {

    private OnIconClickListener iconClickListener;

    private ArrayList<ToolbarIconItem> iconItems = new ArrayList<>();

    private ToolbarBackIcon backIconImage;

    private ArrayList<BottomNavigationTab> tabs = new ArrayList<>();
    private FrameLayout toolbarContainer;
    private FrameLayout toolbarViews;
    private LinearLayout toolbarIcons;

    private Content content;

    private Drawable backIcon = null;
    private int toolbarImage = 0;
    private String toolbarTitle = null;
    private String toolbarSubTitle = null;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 4;

//--------------------------------------------------------------------------------------------------

    public static final int BACKGROUND_STYLE_DEFAULT = 0;
    public static final int BACKGROUND_STYLE_STATIC = 1;
    public static final int BACKGROUND_STYLE_RIPPLE = 2;

    //@IntDef({BACKGROUND_STYLE_DEFAULT, BACKGROUND_STYLE_STATIC, BACKGROUND_STYLE_RIPPLE})
    //@Retention(RetentionPolicy.SOURCE)
    //public @interface BackgroundStyle {}

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private static final int DEFAULT_SELECTED_POSITION = -1;
    private int mSelectedPosition = DEFAULT_SELECTED_POSITION;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Toolbar addIcon(Drawable icon) {
        iconItems.add(0, new ToolbarIconItem(icon));
        initialise();
        return this;
    }

    //public Toolbar setBackIcon(@DrawableRes Drawable backImage) {
    //    backIcon = backImage;
    //    return this;
    //}

    public Toolbar setLogo(int image) {
        this.toolbarImage = image;
        initialise();
        return this;
    }

    public Toolbar setTitle(String text) {
        this.toolbarTitle = text;
        initialise();
        return this;
    }

    public Toolbar setSubtitle(String text) {
        this.toolbarSubTitle = text;
        initialise();
        return this;
    }

    public Toolbar setNavButtonView(@DrawableRes Drawable resId) {
        backIcon = resId;
        initialise();
        return this;
    }

    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean animate) {
        setTranslationY(this.getHeight(), animate);
    }

    public void show(boolean animate) {
        setTranslationY(0, animate);
    }

    public Toolbar setToolbarColor(int color) {
        this.setBackgroundColor(color);
        return this;
    }

    public Toolbar setToolbarElevation(float elevation) {
        ViewCompat.setElevation(this, elevation);
        return this;
    }

    public Toolbar removeAllIcons() {
        iconItems.clear();
        initialise();
        return this;
    }
//--------------------------------------------------------------------------------------------------

    public Toolbar(Context context) {
        super(context);

        toolbarContainer = new FrameLayout(context);
        toolbarContainer.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 56));

        toolbarViews = new FrameLayout(context);
        toolbarViews.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));
        toolbarContainer.addView(toolbarViews, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        toolbarIcons = new LinearLayout(context);
        toolbarIcons.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));
        toolbarIcons.setGravity(Gravity.CENTER);
        toolbarIcons.setOrientation(LinearLayout.HORIZONTAL);
        toolbarContainer.addView(toolbarIcons, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));

        content = new Content(context);
        toolbarViews.addView(content, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START, 0, 0, 0, 0));

        backIconImage = new ToolbarBackIcon(context);
        toolbarContainer.addView(backIconImage, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START));

        addView(toolbarContainer, LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 56));
        setClipToPadding(false);
    }

    public void initialise() {
        if (iconItems.size() > MIN_SIZE - 1 || iconItems.size() < MAX_SIZE + 1) {
            toolbarIcons.removeAllViews();

            for (ToolbarIconItem item : iconItems) {
                ShiftingBottomNavigationTab tab = new ShiftingBottomNavigationTab(getContext());
                setUpTab(tab, item);
            }

            if (tabs.size() > 0) {
                selectTabInternal(0, true);
            }
        }

        if (toolbarTitle != null) {
            content.setTitle(toolbarTitle);
        }

        if (toolbarSubTitle != null) {
            content.setSubtitle(toolbarSubTitle);
        }

        if (toolbarImage != 0) {
            content.setLogo(toolbarImage);
        }

        if (backIcon != null) {
            content.setTitlePadding();

            backIconImage.setIcon(backIcon);
            backIconImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
        }
    }

    private void setUpTab(BottomNavigationTab tab, ToolbarIconItem item) {
        tab.setPosition(iconItems.indexOf(item));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationTab bottomNavigationTabView = (BottomNavigationTab) v;
                selectTabInternal(bottomNavigationTabView.getPosition(), true);
            }
        });

        tabs.add(tab);
        bindTabWithData(item, tab, this);
        tab.initialise();
        toolbarIcons.addView(tab, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END));
    }

    public void bindTabWithData(ToolbarIconItem bottomNavigationItem, BottomNavigationTab bottomNavigationTab, Toolbar bottomNavigationBar) {
        Context context = bottomNavigationBar.getContext();
        bottomNavigationTab.setIcon(bottomNavigationItem.getIcon(context));
    }

    public class ToolbarIconItem {

        private int mIconResource;
        private Drawable mIcon;

        public ToolbarIconItem(Drawable mIcon) {
            this.mIcon = mIcon;
        }

        protected Drawable getIcon(Context context) {
            if (this.mIconResource != 0) {
                return ContextCompat.getDrawable(context, this.mIconResource);
            } else {
                return this.mIcon;
            }
        }
    }


        private void selectTabInternal(int newPosition, boolean callListener) {
        if (callListener) {
            sendListenerCall(mSelectedPosition, newPosition);
        }

        int mAnimationDuration = 200;
        int mBackgroundStyle = BACKGROUND_STYLE_DEFAULT;
        if (mBackgroundStyle == BACKGROUND_STYLE_STATIC) {
            tabs.get(newPosition).select(mAnimationDuration);
        } else if (mBackgroundStyle == BACKGROUND_STYLE_RIPPLE) {
            if (mSelectedPosition != -1) {
                tabs.get(mSelectedPosition).unSelect(mAnimationDuration);
            }

            tabs.get(newPosition).select(mAnimationDuration);
        }

        mSelectedPosition = newPosition;
    }

    private void setTranslationY(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }

            this.setTranslationY(offset);
        }
    }

    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            int mRippleAnimationDuration = 600;
            mTranslationAnimator.setDuration(mRippleAnimationDuration);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }

        mTranslationAnimator.translationY(offset).start();
    }


    private void sendListenerCall(int oldPosition, int newPosition) {
        if (iconClickListener != null && oldPosition != -1) {
            iconClickListener.onIconClick(newPosition);
        }
    }

    public Toolbar setOnIconClickListener(OnIconClickListener listener) {
        this.iconClickListener = listener;
        return this;
    }

    public Toolbar setNavButtonClickListener(OnClickListener listener) {
        backIconImage.setOnClickListener(listener);
        return this;
    }

    public interface OnIconClickListener {
        void onIconClick(int i);
    }

    public class ToolbarBackIcon extends BottomNavigationTab {

        public View containerView;
        public ImageView iconView;

        public ToolbarBackIcon(Context context) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.toolbar_back_icon, this, true);

            containerView = view.findViewById(R.id.back_icon_container);
            iconView = (ImageView) view.findViewById(R.id.toolbar_back_icon);
        }

        public void setIcon(Drawable icon) {
            Drawable mCompactIcon = DrawableCompat.wrap(icon);
            iconView.setImageDrawable(mCompactIcon);
        }
    }

    public class ShiftingBottomNavigationTab extends BottomNavigationTab {

        //private ImageView iconView;

        public ShiftingBottomNavigationTab(Context context) {
            super(context);

            //this.setPadding(AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16), AndroidUtilities.dp(context, 12), AndroidUtilities.dp(context, 16));
            //this.setBackgroundColor(AndroidUtilities.getContextColor(context, R.attr.selectableItemBackgroundBorderless));

            //iconView = new ImageView(context);
            //iconView.setScaleType(ImageView.ScaleType.CENTER);
            //addView(iconView, LayoutHelper.makeFrame(context, 24, 24, Gravity.END | Gravity.CENTER_VERTICAL));
        }

        //public ShiftingBottomNavigationTab setIcon(int icon) {
        //    iconView.setImageResource(icon);
        //    return this;
        //}

        @Override
        void init() {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.icon_row, this, true);
            containerView = view.findViewById(R.id.icon_container);
            iconView = (ImageView) view.findViewById(R.id.toolbar_icon);
            super.init();
        }

        @Override
        public void initialise() {
            super.initialise();
        }

        public void seet(Drawable icon) {
            iconView.setImageDrawable(icon);
        }
    }

    public class BottomNavigationTab extends FrameLayout {

        private int mPosition;
        private Drawable mCompactIcon;
        public View containerView;
        public ImageView iconView;

        public BottomNavigationTab(Context context) {
            super(context);
            init();
        }

        void init() {
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public void setIcon(Drawable icon) {
            mCompactIcon = DrawableCompat.wrap(icon);
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }

        public void select(int animationDuration) {
            ValueAnimator animator = ValueAnimator.ofInt(containerView.getPaddingTop(), 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    containerView.setPadding(containerView.getPaddingLeft(),
                            (Integer) valueAnimator.getAnimatedValue(),
                            containerView.getPaddingRight(),
                            containerView.getPaddingBottom());
                }
            });
            animator.setDuration(animationDuration);
            iconView.setSelected(true);
        }

        public void unSelect(int animationDuration) {
            ValueAnimator animator = ValueAnimator.ofInt(containerView.getPaddingTop(), 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    containerView.setPadding(containerView.getPaddingLeft(), (Integer) valueAnimator.getAnimatedValue(),
                            containerView.getPaddingRight(), containerView.getPaddingBottom());
                }
            });
            animator.setDuration(animationDuration);
            iconView.setSelected(false);
        }

        public void initialise() {
            iconView.setSelected(false);
            iconView.setImageDrawable(mCompactIcon);
        }
    }

    public class Content extends FrameLayout {

        private TextView mTitleTextView;
        private TextView mSubtitleTextView;
        private ImageView mLogoView;

        public Content(Context context) {
            super(context);

            mLogoView = new ImageView(context);
            mLogoView.setVisibility(INVISIBLE);
            mLogoView.setScaleType(ImageView.ScaleType.CENTER);
            mLogoView.setPadding(AndroidUtilities.dp(context, 2), AndroidUtilities.dp(context, 2), AndroidUtilities.dp(context, 2), AndroidUtilities.dp(context, 2));
            mLogoView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
            addView(mLogoView);

            mTitleTextView = new TextView(context);
            mTitleTextView.setSingleLine(true);
            mTitleTextView.setVisibility(INVISIBLE);
            mTitleTextView.setTextColor(0xFFFFFFFF);
            mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleTextView.setTypeface(AndroidUtilities.getTypeface(context, "medium.ttf"));
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            if (AndroidUtilities.isLandscape(context)) {
                mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            mTitleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            mTitleTextView.setPadding(AndroidUtilities.dp(context, 16), 0, 0, 0);
            mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
            addView(mTitleTextView);

            mSubtitleTextView = new TextView(context);
            mSubtitleTextView.setVisibility(INVISIBLE);
            mSubtitleTextView.setSingleLine(true);
            mSubtitleTextView.setTextColor(0xFFFFFFFF);
            mSubtitleTextView.setPadding(AndroidUtilities.dp(context, 16), 0, 0, AndroidUtilities.dp(context, 8));
            mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            if (AndroidUtilities.isLandscape(context)) {
                mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                mSubtitleTextView.setPadding(AndroidUtilities.dp(context, 16), 0, 0, AndroidUtilities.dp(context, 13));
            }
            mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mSubtitleTextView.setGravity(Gravity.START | Gravity.BOTTOM);
            mSubtitleTextView.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM));
            addView(mSubtitleTextView);
        }

        public Content setLogo(int image) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), AndroidUtilities.dp(getContext(), 8), 0, 0);
            mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));
            if (AndroidUtilities.isLandscape(getContext())) {
                mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), AndroidUtilities.dp(getContext(), 13), 0, 0);
            }

            mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, AndroidUtilities.dp(getContext(), 8));
            if (AndroidUtilities.isLandscape(getContext())) {
                mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, AndroidUtilities.dp(getContext(), 13));
            }

            mLogoView.setVisibility(VISIBLE);
            mLogoView.setImageResource(image);
            return this;
        }

        public Content setTitle(String text) {
            mTitleTextView.setVisibility(VISIBLE);
            mTitleTextView.setText(text);
            return this;
        }

        public Content setSubtitle(String text) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 8), 0, 0);
            if (AndroidUtilities.isLandscape(getContext())) {
                mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 13), 0, 0);
            }
            mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));

            mSubtitleTextView.setVisibility(VISIBLE);
            mSubtitleTextView.setText(text);
            return this;
        }

        public Content setTitlePadding() {
            mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, 0);
            return this;
        }
    }
}