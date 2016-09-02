package org.michaelbel.material.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.michaelbel.material.R;
import org.michaelbel.material.Utils;
import org.michaelbel.material.utils.Color;
import org.michaelbel.material.utils.Screen;

import java.util.ArrayList;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ActionBar extends FrameLayout {

    private SimpleTextView mTitleTextView;
    private SimpleTextView mSubtitleTextView;
    private @ColorInt int mTitleTextColor;
    private @ColorInt int mSubtitleTextColor;
    private CharSequence mTitleText;
    private CharSequence mSubtitleText;

    private @DrawableRes int navigationIcon;
    private static boolean isTablet = false;
    private boolean occupyStatusBar = false;
    private boolean actionModeVisible;
    private boolean interceptTouches = true;
    private boolean allowOverlayTitle;
    private boolean castShadows = true;
    protected boolean isSearchFieldVisible;
    private ImageView mBackButtonImageView;
    private View mActionModeTop;
    private ActionBarMenu mMenu;
    private ActionBarMenu mActionMode;
    private CharSequence mLastTitle;
    public ActionBarMenuOnItemClick mActionBarMenuOnItemClick;

    public static class ActionBarMenuOnItemClick {

        public void onItemClick(int i) {}

        public boolean canOpenMenu() {
            return true;
        }
    }

    public ActionBar(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        Utils.bind(context);

        /*TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ActionBar, defStyleAttr, 0);
        mTitleTextColor = attr.getColor(R.styleable.ActionBar_titleTextColor, 0xFFFFFFFF);
        navigationIcon = attr.getResourceId(R.styleable.ActionBar_backIcon, 0);
        attr.recycle();*/

        this.mTitleTextColor = 0xFFFFFFFF;
        this.mSubtitleTextColor = 0xFFFFFFFF;
        this.setBackgroundColor(Color.getThemeColor(context, R.attr.colorPrimary));
    }

    public ActionBar setTitle(@NonNull CharSequence value) {
        mTitleText = value;

        if (mTitleTextView == null) {
            createTitleTextView();
        }

        if (mTitleTextView != null) {
            mLastTitle = mTitleText;
            mTitleTextView.setVisibility(!isSearchFieldVisible ? VISIBLE : INVISIBLE);
            mTitleTextView.setText(mTitleText);
        }

        return this;
    }

    public ActionBar setTitle(@StringRes int stringId) {
        setTitle(getContext().getText(stringId));
        return this;
    }

    public CharSequence getTitle() {
        return mTitleText;
    }

    public ActionBar setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;

        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(mTitleTextColor);
        }

        return this;
    }

    @ColorInt
    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public SimpleTextView getTitleTextView() {
        return mTitleTextView;
    }

    public ActionBar setSubtitle(@NonNull CharSequence value) {
        mSubtitleText = value;

        if (mSubtitleTextView == null) {
            createSubtitleTextView();
        }

        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(!isSearchFieldVisible ? VISIBLE : INVISIBLE);
            mSubtitleTextView.setText(mSubtitleText);
        }

        return this;
    }

    public ActionBar setSubtitle(@StringRes int stringId) {
        setSubtitle(getContext().getText(stringId));
        return this;
    }

    public CharSequence getSubtitle() {
        return mSubtitleText;
    }

    public ActionBar setSubtitleTextColor(@ColorInt int color) {
        mSubtitleTextColor = color;

        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(mSubtitleTextColor);
        }

        return this;
    }

    @ColorInt
    public int getSubtitleTextColor() {
        return mSubtitleTextColor;
    }

    public SimpleTextView getSubtitleTextView() {
        return mSubtitleTextView;
    }












    public ActionBar setNavigationIcon(@DrawableRes int resId) {
        if (mBackButtonImageView == null) {
            createBackButtonImage();
        }

        mBackButtonImageView.setVisibility(resId == 0 ? GONE : VISIBLE);
        navigationIcon = resId;

        mBackButtonImageView.setImageResource(navigationIcon);
        return this;
    }

    public ActionBar setBackButtonDrawable(@DrawableRes Drawable resId) {
        if (mBackButtonImageView == null) {
            createBackButtonImage();
        }

        mBackButtonImageView.setVisibility(resId == null ? GONE : VISIBLE);
        //navigationIcon = resId;

        mBackButtonImageView.setImageDrawable(resId);
        return this;
    }

    public ActionBar setInterceptTouches(boolean value) {
        interceptTouches = value;
        return this;
    }

    public ActionBar setOccupyStatusBar(boolean value) {
        occupyStatusBar = value;

        if (mActionMode != null) {
            mActionMode.setPadding(0, occupyStatusBar ? Screen.getStatusBarHeight(getContext()) : 0, 0, 0);
        }

        return this;
    }

    private void createTitleTextView() {
        if (mTitleTextView != null) {
            return;
        }

        mTitleTextView = new SimpleTextView(getContext());
        mTitleTextView.setGravity(Gravity.START);
        mTitleTextView.setTextColor(mTitleTextColor);
        mTitleTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        addView(mTitleTextView, 0, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));
    }

    private void createBackButtonImage() {
        if (mBackButtonImageView != null) {
            return;
        }

        mBackButtonImageView = new ImageView(getContext());
        mBackButtonImageView.setScaleType(ImageView.ScaleType.CENTER);
        mBackButtonImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        addView(mBackButtonImageView, LayoutHelper.makeFrame(getContext(), 4, LayoutHelper.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));

        mBackButtonImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchFieldVisible) {
                    closeSearchField();
                    return;
                }

                if (mActionBarMenuOnItemClick != null) {
                    mActionBarMenuOnItemClick.onItemClick(-1);
                }
            }
        });
    }

    private void createSubtitleTextView() {
        if (mSubtitleTextView != null) {
            return;
        }

        mSubtitleTextView = new SimpleTextView(getContext());
        mSubtitleTextView.setGravity(Gravity.START);
        mSubtitleTextView.setTextColor(0xFFFFFFFF);
        addView(mSubtitleTextView, 0, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.TOP));
    }

    public ActionBarMenu createMenu() {
        if (mMenu != null) {
            return mMenu;
        }

        mMenu = new ActionBarMenu(getContext(), this);
        addView(mMenu, 0, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.END));
        return mMenu;
    }

    public ActionBar setActionBarMenuOnItemClick(ActionBarMenuOnItemClick listener) {
        mActionBarMenuOnItemClick = listener;
        return this;
    }

    public ActionBarMenu createActionMode() {
        if (mActionMode != null) {
            return mActionMode;
        }

        mActionMode = new ActionBarMenu(getContext(), this);
        mActionMode.setBackgroundColor(0xFFFFFFFF);
        addView(mActionMode, indexOfChild(mBackButtonImageView));

        mActionMode.setPadding(0, occupyStatusBar ? Screen.getStatusBarHeight(getContext()) : 0, 0, 0);

        LayoutParams layoutParams = (LayoutParams) mActionMode.getLayoutParams();
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.gravity = Gravity.END;
        mActionMode.setLayoutParams(layoutParams);
        mActionMode.setVisibility(INVISIBLE);

        if (occupyStatusBar && mActionModeTop == null) {
            mActionModeTop = new View(getContext());
            mActionModeTop.setBackgroundColor(0x99000000);
            addView(mActionModeTop);
            layoutParams = (LayoutParams) mActionModeTop.getLayoutParams();
            layoutParams.height = Screen.getStatusBarHeight(getContext());
            layoutParams.width = LayoutHelper.MATCH_PARENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            mActionModeTop.setLayoutParams(layoutParams);
            mActionModeTop.setVisibility(INVISIBLE);
        }

        return mActionMode;
    }

    public int getCurrentActionBarHeight() {
        if (isTablet) {
            return Utils.dp(64);
        } else if (Screen.isLandscape(getContext())) {
            return Utils.dp(48);
        } else {
            return Utils.dp(56);
        }
    }

    public void showActionMode() {
        if (mActionMode == null || actionModeVisible) {
            return;
        }

        actionModeVisible = true;

        if (Build.VERSION.SDK_INT >= 14) {
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(ObjectAnimator.ofFloat(mActionMode, "alpha", 0.0f, 1.0f));

            if (occupyStatusBar && mActionModeTop != null) {
                animators.add(ObjectAnimator.ofFloat(mActionModeTop, "alpha", 0.0f, 1.0f));
            }

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animators);
            animatorSet.setDuration(200);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mActionMode.setVisibility(VISIBLE);

                    if (occupyStatusBar && mActionModeTop != null) {
                        mActionModeTop.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mTitleTextView != null) {
                        mTitleTextView.setVisibility(INVISIBLE);
                    }

                    if (mSubtitleTextView != null) {
                        mSubtitleTextView.setVisibility(INVISIBLE);
                    }

                    if (mMenu != null) {
                        mMenu.setVisibility(INVISIBLE);
                    }
                }
            });
            animatorSet.start();
        } else {
            mActionMode.setVisibility(VISIBLE);

            if (occupyStatusBar && mActionModeTop != null) {
                mActionModeTop.setVisibility(VISIBLE);
            }

            if (mTitleTextView != null) {
                mTitleTextView.setVisibility(INVISIBLE);
            }

            if (mSubtitleTextView != null) {
                mSubtitleTextView.setVisibility(INVISIBLE);
            }

            if (mMenu != null) {
                mMenu.setVisibility(INVISIBLE);
            }
        }

        if (mBackButtonImageView != null) {
            Drawable drawable = mBackButtonImageView.getDrawable();

            if (drawable instanceof BackDrawable) {
                ((BackDrawable) drawable).setRotation(1, true);
            }

            mBackButtonImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }
    }

    public void hideActionMode() {
        if (mActionMode == null || !actionModeVisible) {
            return;
        }
        actionModeVisible = false;
        if (Build.VERSION.SDK_INT >= 14) {
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(ObjectAnimator.ofFloat(mActionMode, "alpha", 0.0f));
            if (occupyStatusBar && mActionModeTop != null) {
                animators.add(ObjectAnimator.ofFloat(mActionModeTop, "alpha", 0.0f));
            }
            AnimatorSet animatorSetProxy = new AnimatorSet();
            animatorSetProxy.playTogether(animators);
            animatorSetProxy.setDuration(200);
            animatorSetProxy.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mActionMode.setVisibility(INVISIBLE);
                    if (occupyStatusBar && mActionModeTop != null) {
                        mActionModeTop.setVisibility(INVISIBLE);
                    }
                }
            });
            animatorSetProxy.start();
        } else {
            mActionMode.setVisibility(INVISIBLE);

            if (occupyStatusBar && mActionModeTop != null) {
                mActionModeTop.setVisibility(INVISIBLE);
            }
        }
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(VISIBLE);
        }

        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(VISIBLE);
        }

        if (mMenu != null) {
            mMenu.setVisibility(VISIBLE);
        }

        if (mBackButtonImageView != null) {
            Drawable drawable = mBackButtonImageView.getDrawable();

            if (drawable instanceof BackDrawable) {
                ((BackDrawable) drawable).setRotation(0, true);
            }

            mBackButtonImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }
    }

    public void showActionModeTop() {
        if (occupyStatusBar && mActionModeTop == null) {
            mActionModeTop = new View(getContext());
            mActionModeTop.setBackgroundColor(0x99000000);
            addView(mActionModeTop);
            LayoutParams layoutParams = (LayoutParams) mActionModeTop.getLayoutParams();
            layoutParams.height = Screen.getStatusBarHeight(getContext());
            layoutParams.width = LayoutHelper.MATCH_PARENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            mActionModeTop.setLayoutParams(layoutParams);
        }
    }

    public boolean isActionModeShowed() {
        return mActionMode != null && actionModeVisible;
    }

    protected void onSearchFieldVisibilityChanged(boolean visible) {
        isSearchFieldVisible = visible;

        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }

        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }

        Drawable drawable = mBackButtonImageView.getDrawable();

        if (drawable != null && drawable instanceof MenuDrawable) {
            ((MenuDrawable) drawable).setRotation(visible ? 1 : 0, true);
        }
    }

    public void closeSearchField() {
        if (!isSearchFieldVisible || mMenu == null) {
            return;
        }

        mMenu.closeSearchField();
    }

    public void openSearchField(String text) {
        if (mMenu == null || text == null) {
            return;
        }

        mMenu.openSearchField(!isSearchFieldVisible, text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int actionBarHeight = getCurrentActionBarHeight();
        int actionBarHeightSpec = MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.EXACTLY);

        setMeasuredDimension(width, actionBarHeight + (occupyStatusBar ? Screen.getStatusBarHeight(getContext()) : 0));

        int textLeft;
        if (mBackButtonImageView != null && mBackButtonImageView.getVisibility() != GONE) {
            mBackButtonImageView.measure(MeasureSpec.makeMeasureSpec(Utils.dp(54), MeasureSpec.EXACTLY), actionBarHeightSpec);
            textLeft = Utils.dp(isTablet ? 80 : 56);
        } else {
            textLeft = Utils.dp(isTablet ? 26 : 16);
        }

        if (mMenu != null && mMenu.getVisibility() != GONE) {
            int menuWidth;
            if (isSearchFieldVisible) {
                menuWidth = MeasureSpec.makeMeasureSpec(width - Utils.dp(isTablet ? 74 : 66), MeasureSpec.EXACTLY);
            } else {
                menuWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            }
            mMenu.measure(menuWidth, actionBarHeightSpec);
        }

        if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE || mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
            int availableWidth = width - (mMenu != null ? mMenu.getMeasuredWidth() : 0) - Utils.dp(16) - textLeft;

            if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE) {
                mTitleTextView.setTextSize(!isTablet && Screen.isLandscape(getContext()) ? 18 : 20);
                mTitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(Utils.dp(24), MeasureSpec.AT_MOST));

            }
            if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
                mSubtitleTextView.setTextSize(!isTablet && Screen.isLandscape(getContext()) ? 12 : 14);
                mSubtitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(Utils.dp(20), MeasureSpec.AT_MOST));
            }
        }

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mTitleTextView || child == mSubtitleTextView || child == mMenu || child == mBackButtonImageView) {
                continue;
            }

            measureChildWithMargins(child, widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY), 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int additionalTop = occupyStatusBar ? Screen.getStatusBarHeight(getContext()) : 0;

        int textLeft;

        if (mBackButtonImageView != null && mBackButtonImageView.getVisibility() != GONE) {
            mBackButtonImageView.layout(0, additionalTop, mBackButtonImageView.getMeasuredWidth(), additionalTop + mBackButtonImageView.getMeasuredHeight());
            textLeft = Utils.dp(isTablet ? 80 : 56);
        } else {
            textLeft = Utils.dp(isTablet ? 26 : 12);
        }

        if (mMenu != null && mMenu.getVisibility() != GONE) {
            int menuLeft = isSearchFieldVisible ? Utils.dp(isTablet ? 74 : 56) : (right - left) - mMenu.getMeasuredWidth();
            mMenu.layout(menuLeft, additionalTop, menuLeft + mMenu.getMeasuredWidth(), additionalTop + mMenu.getMeasuredHeight());
        }

        if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE) {
            int textTop;
            if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
                textTop = (getCurrentActionBarHeight() / 2 - mTitleTextView.getTextHeight()) / 2 + Utils.dp(!isTablet && Screen.isLandscape(getContext()) ? 2 : 3);
            } else {
                textTop = (getCurrentActionBarHeight() - mTitleTextView.getTextHeight()) / 2;
            }
            mTitleTextView.layout(textLeft, additionalTop + textTop, textLeft + mTitleTextView.getMeasuredWidth(), additionalTop + textTop + mTitleTextView.getTextHeight());
        }

        if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
            int textTop = getCurrentActionBarHeight() / 2 + (getCurrentActionBarHeight() / 2 - mSubtitleTextView.getTextHeight()) / 2 - Utils.dp(!isTablet && Screen.isLandscape(getContext()) ? 1 : 1);
            mSubtitleTextView.layout(textLeft, additionalTop + textTop, textLeft + mSubtitleTextView.getMeasuredWidth(), additionalTop + textTop + mSubtitleTextView.getTextHeight());
        }

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mTitleTextView || child == mSubtitleTextView || child == mMenu || child == mBackButtonImageView) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int childLeft;
            int childTop;

            int gravity = lp.gravity;

            if (gravity == -1) {
                gravity = Gravity.TOP | Gravity.START;
            }

            final int absoluteGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = (right - left - width) / 2 + lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.END:
                    childLeft = right - width - lp.rightMargin;
                    break;
                case Gravity.START:
                default:
                    childLeft = lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = (bottom - top - height) / 2 + lp.topMargin - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = (bottom - top) - height - lp.bottomMargin;
                    break;
                default:
                    childTop = lp.topMargin;
            }

            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    public void onMenuButtonPressed() {
        if (mMenu != null) {
            mMenu.onMenuButtonPressed();
        }
    }

    protected void onPause() {
        if (mMenu != null) {
            mMenu.hideAllPopupMenus();
        }
    }

    public void setAllowOverlayTitle(boolean value) {
        allowOverlayTitle = value;
    }

    public void setTitleOverlayText(String text) {
        if (!allowOverlayTitle) {
            return;
        }

        CharSequence textToSet = text != null ? text : mLastTitle;

        if (textToSet != null && mTitleTextView == null) {
            createTitleTextView();
        }

        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(textToSet != null && !isSearchFieldVisible ? VISIBLE : INVISIBLE);
            mTitleTextView.setText(textToSet);
        }
    }

    public boolean isSearchFieldVisible() {
        return isSearchFieldVisible;
    }

    public boolean getOccupyStatusBar() {
        return occupyStatusBar;
    }

    public void setCastShadows(boolean value) {
        castShadows = value;
    }

    public boolean getCastShadows() {
        return castShadows;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event) || interceptTouches;
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}