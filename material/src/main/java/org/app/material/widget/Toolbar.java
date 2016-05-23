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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

import java.util.ArrayList;

@Deprecated
public class Toolbar extends FrameLayout {

    private TextView mTitleTextView;
    private TextView mSubtitleTextView;

    private LinearLayout mToolbarIcons;
//==================================================================================================

    private float mElevation;
    private int mToolbarColor;
    private EditText mSearchView;
    private OnIconClickListener onIconClickListener;
    private ArrayList<ToolbarIconItem> mIconItems = new ArrayList<>();
    private ArrayList<BottomNavigationTab> mTabs = new ArrayList<>();

//--------------------------------------------------------------------------------------------------

    public static final int BACKGROUND_STYLE_DEFAULT = 0;
    public static final int BACKGROUND_STYLE_STATIC = 1;
    public static final int BACKGROUND_STYLE_RIPPLE = 2;

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private static final int DEFAULT_SELECTED_POSITION = -1;
    private int mSelectedPosition = DEFAULT_SELECTED_POSITION;

    // Done.
    public Toolbar(Context context) {
        super(context);
        init(context, null, 0);
    }

    // Done.
    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    // Done.
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.MATCH_PARENT, 56, Gravity.TOP));

        //FrameLayout mToolbarViews = new FrameLayout(context);
        //mToolbarViews.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));
        //addView(mToolbarViews, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        mToolbarIcons = new LinearLayout(context);
        mToolbarIcons.setLayoutParams(LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));
        mToolbarIcons.setGravity(Gravity.CENTER);
        mToolbarIcons.setOrientation(LinearLayout.HORIZONTAL);
        addView(mToolbarIcons, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));

        //content = new Content(context);
        //mToolbarViews.addView(content, LayoutHelper.makeFrame(context, LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.START, 0, 0, 0, 0));

        setClipToPadding(false);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyleAttr, 0);
        mElevation = attr.getDimensionPixelSize(R.styleable.Toolbar_toolbar_elevation, 0);
        mToolbarColor =  attr.getDimensionPixelSize(R.styleable.Toolbar_toolbar_color, AndroidUtilities.getContextColor(context, R.attr.colorPrimary));
        attr.recycle();

        this.setBackgroundColor(mToolbarColor);
        this.setElevation(mElevation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTitle(String title) {
        createTitleTextView();
        mTitleTextView.setText(title);
    }

    public void setTitle(@StringRes int resId) {
        createTitleTextView();
        mTitleTextView.setText(getContext().getResources().getText(resId));
    }

    private void createTitleTextView() {
        if (mTitleTextView != null) {
            return;
        }
        mTitleTextView = new TextView(getContext());
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setTextColor(0xFFFFFFFF);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTitleTextView.setTypeface(AndroidUtilities.getTypeface(getContext(), "medium.ttf"));
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        if (AndroidUtilities.isLandscape(getContext())) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        mTitleTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, 0);
        mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mTitleTextView);
    }

    public void setNavIcon(int navIcon) {
        createNavIconView(navIcon);
    }

    private void createNavIconView(int mNavIcon) {
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, 0);
        //mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, 0, 0);

        NavIcon backIcon = new NavIcon(getContext());
        backIcon.setIcon(mNavIcon);
        addView(backIcon);
    }

    public class NavIcon extends FrameLayout {
        private ImageView iconView;

        public NavIcon(Context context) {
            super(context);
            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iconView = (ImageView) LayoutInflater.from(context).inflate(R.layout.toolbar_nav_icon, this, true).findViewById(R.id.nav_icon);
        }

        public void setIcon(int icon) {
            iconView.setImageResource(icon);
        }
    }

    public void setSubtitle(String text) {
        createSubTitle();
        mSubtitleTextView.setText(text);
    }

    public void setSubtitle(@StringRes int resId) {
        createSubTitle();
        mSubtitleTextView.setText(getContext().getResources().getText(resId));
    }

    private void createSubTitle() {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 8), 0, 0);
        if (AndroidUtilities.isLandscape(getContext())) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), AndroidUtilities.dp(getContext(), 13), 0, 0);
        }
        mTitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));

        if (mSubtitleTextView != null) {
            return;
        }
        mSubtitleTextView = new TextView(getContext());
        mSubtitleTextView.setSingleLine(true);
        mSubtitleTextView.setTextColor(0xFFFFFFFF);
        mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, AndroidUtilities.dp(getContext(), 8));
        mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        if (AndroidUtilities.isLandscape(getContext())) {
            mSubtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            mSubtitleTextView.setPadding(AndroidUtilities.dp(getContext(), 16), 0, 0, AndroidUtilities.dp(getContext(), 13));
        }
        mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        mSubtitleTextView.setGravity(Gravity.START | Gravity.BOTTOM);
        mSubtitleTextView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.BOTTOM));
        addView(mSubtitleTextView);
    }






















































    public void openSearchField() {
        mSearchView = new EditText(getContext());
        mSearchView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mSearchView.setHintTextColor(0x88ffffff);
        mSearchView.setTextColor(0xffffffff);
        mSearchView.setSingleLine(true);
        mSearchView.setBackgroundResource(0);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.setPadding(AndroidUtilities.dp(getContext(), 56), 0, AndroidUtilities.dp(getContext(), 56), 0);
        mSearchView.setLayoutParams(LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL));
        addView(mSearchView);

        mTitleTextView.setVisibility(INVISIBLE);
        mSearchView.requestFocus();
    }

    public Toolbar addIcon(int icon) {
        mIconItems.add(0, new ToolbarIconItem(AndroidUtilities.getIcon(getContext(), icon, 0xFFFFFFFF)));
        initialise();
        return this;
    }

    public Toolbar setLogo(int image) {
        //this.mToolbarImage = image;
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
        mIconItems.clear();
        initialise();
        return this;
    }
//--------------------------------------------------------------------------------------------------



    public void initialise() {
        if (mIconItems.size() > 0 || mIconItems.size() < 5) {
            mToolbarIcons.removeAllViews();

            for (int i = 0; i < mIconItems.size(); i++) {
                if (i >= 4) {
                    mIconItems.remove(i);
                }
            }

            for (ToolbarIconItem item : mIconItems) {
                ShiftingBottomNavigationTab tab = new ShiftingBottomNavigationTab(getContext());
                setUpTab(tab, item);
            }

            if (mTabs.size() > 0) {
                selectTabInternal(0, true);
            }
        }
    }

    private void setUpTab(BottomNavigationTab tab, ToolbarIconItem item) {
        tab.setPosition(mIconItems.indexOf(item));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationTab bottomNavigationTabView = (BottomNavigationTab) v;
                selectTabInternal(bottomNavigationTabView.getPosition(), true);
            }
        });

        mTabs.add(tab);
        bindTabWithData(item, tab, this);
        tab.initialise();
        mToolbarIcons.addView(tab, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END));
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
        if (onIconClickListener != null && oldPosition != -1) {
            onIconClickListener.onIconClick(newPosition);
        }
    }

    public Toolbar setOnIconClickListener(OnIconClickListener listener) {
        this.onIconClickListener = listener;
        return this;
    }



    public interface OnIconClickListener {
        void onIconClick(int i);
    }



    public class ShiftingBottomNavigationTab extends BottomNavigationTab {

        public ShiftingBottomNavigationTab(Context context) {
            super(context);
        }

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