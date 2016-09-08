package org.michaelbel.material.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.michaelbel.material.R;
import org.michaelbel.material.Utils;

import java.util.ArrayList;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ActionBar extends FrameLayout {

    public static final int MENU_ICON = -1;

    private ImageView mNavigationIconImageView;
    private SimpleTextView mTitleTextView;
    private SimpleTextView mSubtitleTextView;

    private ActionBarMenu menu;

    private @ColorInt int mTitleTextColor;
    private @ColorInt int mSubtitleTextColor;

    public static class ActionBarMenuOnItemClick {

        public void onItemClick(int id) {}

        public boolean canOpenMenu() {
            return true;
        }
    }

    private View actionModeTop;
    private ActionBarMenu actionMode;
    //private boolean occupyStatusBar = Build.VERSION.SDK_INT >= 21;
    private boolean occupyStatusBar = false;
    private boolean actionModeVisible;
    private boolean addToContainer = true;
    private boolean interceptTouches = true;
    private int extraHeight;
    private AnimatorSet actionModeAnimation;

    private boolean allowOverlayTitle;
    private CharSequence lastTitle;
    private boolean castShadows = true;

    protected boolean isSearchFieldVisible;
    protected int itemsBackgroundColor;
    private boolean isBackOverlayVisible;
    public ActionBarMenuOnItemClick actionBarMenuOnItemClick;

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
        this.setBackgroundColor(Utils.getAttrColor(context, R.attr.colorPrimary));

        this.mTitleTextColor = ContextCompat.getColor(context, R.color.md_white);
        this.mSubtitleTextColor = ContextCompat.getColor(context, R.color.md_white);
    }

    public SimpleTextView getTitleTextView() {
        return mTitleTextView;
    }

    public SimpleTextView getSubtitleTextView() {
        return mSubtitleTextView;
    }

    public ImageView getNavigationIconImageView() {
        return mNavigationIconImageView;
    }

    public ActionBar setNavigationIcon(@DrawableRes int icon) {
        if (mNavigationIconImageView == null) {
            createBackButtonImage();
        }
        mNavigationIconImageView.setVisibility(icon == 0 ? GONE : VISIBLE);
        mNavigationIconImageView.setImageResource(icon);

        return this;
    }

    public ActionBar setNavigationIcon(@DrawableRes Drawable resId) {
        if (mNavigationIconImageView == null) {
            createBackButtonImage();
        }
        mNavigationIconImageView.setVisibility(resId == null ? GONE : VISIBLE);
        mNavigationIconImageView.setImageDrawable(resId);

        if (resId instanceof BackDrawable) {
            ((BackDrawable) resId).setRotation(isActionModeShowed() ? 1 : 0, false);
        }

        return this;
    }

    private void createBackButtonImage() {
        if (mNavigationIconImageView != null) {
            return;
        }
        mNavigationIconImageView = new ImageView(getContext());
        mNavigationIconImageView.setScaleType(ImageView.ScaleType.CENTER);
        mNavigationIconImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        mNavigationIconImageView.setPadding(Utils.dp(getContext(), 1), 0, 0, 0);
        addView(mNavigationIconImageView, LayoutHelper.makeFrame(getContext(), 54, 54, Gravity.START | Gravity.TOP));

        mNavigationIconImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchFieldVisible) {
                    closeSearchField();
                    return;
                }
                if (actionBarMenuOnItemClick != null) {
                    actionBarMenuOnItemClick.onItemClick(MENU_ICON);
                }
            }
        });
    }

    public ActionBar setTitle(@NonNull CharSequence value) {
        if (mTitleTextView == null) {
            createTitleTextView();
        }
        if (mTitleTextView != null) {
            lastTitle = value;
            mTitleTextView.setVisibility(!isSearchFieldVisible ? VISIBLE : INVISIBLE);
            mTitleTextView.setText(value);
        }

        return this;
    }

    public ActionBar setTitle(@StringRes int stringId) {
        setTitle(getContext().getText(stringId));
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

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.START | Gravity.TOP;

        addView(mTitleTextView, 0, params);
    }

    public ActionBar setSubtitle(@NonNull CharSequence value) {
        if (mSubtitleTextView == null) {
            createSubtitleTextView();
        }
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(!isSearchFieldVisible ? VISIBLE : INVISIBLE);
            mSubtitleTextView.setText(value);
        }

        return this;
    }

    public ActionBar setSubtitle(@StringRes int stringId) {
        setSubtitle(getContext().getText(stringId));
        return this;
    }

    private void createSubtitleTextView() {
        if (mSubtitleTextView != null) {
            return;
        }
        mSubtitleTextView = new SimpleTextView(getContext());
        mSubtitleTextView.setGravity(Gravity.START);
        mSubtitleTextView.setTextColor(mSubtitleTextColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.START | Gravity.TOP;

        addView(mSubtitleTextView, 0, params);
    }

    @NonNull
    public String getTitle() {
        return mTitleTextView.getText().toString();
    }

    @NonNull
    public String getSubtitle() {
        return mSubtitleTextView.getText().toString();
    }

    public ActionBarMenu createMenu() {
        if (menu != null) {
            return menu;
        }
        menu = new ActionBarMenu(getContext(), this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.END;

        addView(menu, 0, params);
        //addLayout(menu, 0, LayoutHelper.makeFrame(getContext(), LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.RIGHT));
        return menu;
    }

    public void setActionBarMenuOnItemClick(ActionBarMenuOnItemClick listener) {
        actionBarMenuOnItemClick = listener;
    }

    public int getActionBarHeight() {
        if (Utils.isTablet(getContext())) {
            return Utils.dp(getContext(), 64);
        } else if (Utils.isLandscape(getContext())) {
            return Utils.dp(getContext(), 48);
        } else {
            return Utils.dp(getContext(), 56);
        }
    }

    public int getActionBarHeightPx() {
        if (Utils.isTablet(getContext())) {
            return 64;
        } else if (Utils.isLandscape(getContext())) {
            return 48;
        } else {
            return 56;
        }
    }

    public ActionBar setTitleTextColor(@ColorInt int color) {
        this.mTitleTextColor = color;
        return this;
    }

    @ColorInt
    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public ActionBar setSubtitleTextColor(@ColorInt int color) {
        this.mSubtitleTextColor = color;
        return this;
    }

    @ColorInt
    public int getSubtitleTextColor() {
        return mSubtitleTextColor;
    }











    public void setAddToContainer(boolean value) {
        addToContainer = value;
    }

    public boolean getAddToContainer() {
        return addToContainer;
    }

    public ActionBarMenu createActionMode() {
        if (actionMode != null) {
            return actionMode;
        }
        actionMode = new ActionBarMenu(getContext(), this);
        actionMode.setBackgroundColor(0xFFFFFFFF);
        addView(actionMode, indexOfChild(mNavigationIconImageView));
        actionMode.setPadding(0, occupyStatusBar ? Utils.getStatusBarHeight(getContext()) : 0, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionMode.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.END;
        actionMode.setLayoutParams(layoutParams);
        actionMode.setVisibility(INVISIBLE);

        if (occupyStatusBar && actionModeTop == null) {
            actionModeTop = new View(getContext());
            actionModeTop.setBackgroundColor(0x99000000);
            addView(actionModeTop);
            layoutParams = (FrameLayout.LayoutParams)actionModeTop.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getContext());
            layoutParams.width = LayoutHelper.MATCH_PARENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            actionModeTop.setLayoutParams(layoutParams);
            actionModeTop.setVisibility(INVISIBLE);
        }

        return actionMode;
    }

    public void showActionMode() {
        if (actionMode == null || actionModeVisible) {
            return;
        }
        actionModeVisible = true;
        ArrayList<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(actionMode, "alpha", 0.0F, 1.0F));
        if (occupyStatusBar && actionModeTop != null) {
            animators.add(ObjectAnimator.ofFloat(actionModeTop, "alpha", 0.0F, 1.0F));
        }
        if (actionModeAnimation != null) {
            actionModeAnimation.cancel();
        }
        actionModeAnimation = new AnimatorSet();
        actionModeAnimation.playTogether(animators);
        actionModeAnimation.setDuration(200);
        actionModeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                actionMode.setVisibility(VISIBLE);
                if (occupyStatusBar && actionModeTop != null) {
                    actionModeTop.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (actionModeAnimation != null && actionModeAnimation.equals(animation)) {
                    actionModeAnimation = null;
                    if (mTitleTextView != null) {
                        mTitleTextView.setVisibility(INVISIBLE);
                    }
                    if (mSubtitleTextView != null) {
                        mSubtitleTextView.setVisibility(INVISIBLE);
                    }
                    if (menu != null) {
                        menu.setVisibility(INVISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (actionModeAnimation != null && actionModeAnimation.equals(animation)) {
                    actionModeAnimation = null;
                }
            }
        });
        actionModeAnimation.start();
        if (mNavigationIconImageView != null) {
            Drawable drawable = mNavigationIconImageView.getDrawable();
            if (drawable instanceof BackDrawable) {
                ((BackDrawable) drawable).setRotation(1, true);
            }
            mNavigationIconImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }
    }

    public void hideActionMode() {
        if (actionMode == null || !actionModeVisible) {
            return;
        }
        actionModeVisible = false;
        ArrayList<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(actionMode, "alpha", 0.0f));
        if (occupyStatusBar && actionModeTop != null) {
            animators.add(ObjectAnimator.ofFloat(actionModeTop, "alpha", 0.0f));
        }
        if (actionModeAnimation != null) {
            actionModeAnimation.cancel();
        }
        actionModeAnimation = new AnimatorSet();
        actionModeAnimation.playTogether(animators);
        actionModeAnimation.setDuration(200);
        actionModeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (actionModeAnimation != null && actionModeAnimation.equals(animation)) {
                    actionModeAnimation = null;
                    actionMode.setVisibility(INVISIBLE);
                    if (occupyStatusBar && actionModeTop != null) {
                        actionModeTop.setVisibility(INVISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (actionModeAnimation != null && actionModeAnimation.equals(animation)) {
                    actionModeAnimation = null;
                }
            }
        });
        actionModeAnimation.start();
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(VISIBLE);
        }
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(VISIBLE);
        }
        if (menu != null) {
            menu.setVisibility(VISIBLE);
        }
        if (mNavigationIconImageView != null) {
            Drawable drawable = mNavigationIconImageView.getDrawable();
            if (drawable instanceof BackDrawable) {
                ((BackDrawable) drawable).setRotation(0, true);
            }
            mNavigationIconImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }
    }

    public void showActionModeTop() {
        if (occupyStatusBar && actionModeTop == null) {
            actionModeTop = new View(getContext());
            actionModeTop.setBackgroundColor(0x99000000);
            addView(actionModeTop);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionModeTop.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getContext());
            layoutParams.width = LayoutHelper.MATCH_PARENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            actionModeTop.setLayoutParams(layoutParams);
        }
    }

    public boolean isActionModeShowed() {
        return actionMode != null && actionModeVisible;
    }

    protected void onSearchFieldVisibilityChanged(boolean visible) {
        isSearchFieldVisible = visible;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }
        Drawable drawable = mNavigationIconImageView.getDrawable();
        if (drawable != null && drawable instanceof MenuDrawable) {
            ((MenuDrawable) drawable).setRotation(visible ? 1 : 0, true);
        }
    }

    public void setInterceptTouches(boolean value) {
        interceptTouches = value;
    }

    public void setExtraHeight(int value) {
        extraHeight = value;
    }

    public void closeSearchField() {
        if (!isSearchFieldVisible || menu == null) {
            return;
        }
        menu.closeSearchField();
    }

    public void openSearchField(String text) {
        if (menu == null || text == null) {
            return;
        }
        menu.openSearchField(!isSearchFieldVisible, text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int actionBarHeight = getActionBarHeight();
        int actionBarHeightSpec = MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.EXACTLY);

        setMeasuredDimension(width, actionBarHeight + (occupyStatusBar ? Utils.getStatusBarHeight(getContext()) : 0) + extraHeight);

        int textLeft;
        if (mNavigationIconImageView != null && mNavigationIconImageView.getVisibility() != GONE) {
            mNavigationIconImageView.measure(MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 54), MeasureSpec.EXACTLY), actionBarHeightSpec);
            textLeft = Utils.dp(getContext(), Utils.isTablet() ? 80 : 72);
        } else {
            textLeft = Utils.dp(getContext(), Utils.isTablet() ? 26 : 18);
        }

        if (menu != null && menu.getVisibility() != GONE) {
            int menuWidth;
            if (isSearchFieldVisible) {
                menuWidth = MeasureSpec.makeMeasureSpec(width - Utils.dp(getContext(), Utils.isTablet() ? 74 : 66), MeasureSpec.EXACTLY);
            } else {
                menuWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            }
            menu.measure(menuWidth, actionBarHeightSpec);
        }

        if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE || mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
            int availableWidth = width - (menu != null ? menu.getMeasuredWidth() : 0) - Utils.dp(getContext(), 16) - textLeft;

            if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE) {
                mTitleTextView.setTextSize(!Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 18 : 20);
                mTitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 24), MeasureSpec.AT_MOST));

            }
            if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
                mSubtitleTextView.setTextSize(!Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 14 : 16);
                mSubtitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 20), MeasureSpec.AT_MOST));
            }
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE || child == mTitleTextView || child == mSubtitleTextView || child == menu || child == mNavigationIconImageView) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY), 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int additionalTop = occupyStatusBar ? Utils.getStatusBarHeight(getContext()) : 0;

        int textLeft;
        if (mNavigationIconImageView != null && mNavigationIconImageView.getVisibility() != GONE) {
            mNavigationIconImageView.layout(0, additionalTop, mNavigationIconImageView.getMeasuredWidth(), additionalTop + mNavigationIconImageView.getMeasuredHeight());
            textLeft = Utils.dp(getContext(), Utils.isTablet() ? 80 : 72);
        } else {
            textLeft = Utils.dp(getContext(), Utils.isTablet() ? 26 : 18);
        }

        if (menu != null && menu.getVisibility() != GONE) {
            int menuLeft = isSearchFieldVisible ? Utils.dp(getContext(), Utils.isTablet() ? 74 : 66) : (right - left) - menu.getMeasuredWidth();
            menu.layout(menuLeft, additionalTop, menuLeft + menu.getMeasuredWidth(), additionalTop + menu.getMeasuredHeight());
        }

        if (mTitleTextView != null && mTitleTextView.getVisibility() != GONE) {
            int textTop;
            if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
                textTop = (getActionBarHeight() / 2 - mTitleTextView.getTextHeight()) / 2 + Utils.dp(getContext(), !Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 3);
            } else {
                textTop = (getActionBarHeight() - mTitleTextView.getTextHeight()) / 2;
            }
            mTitleTextView.layout(textLeft, additionalTop + textTop, textLeft + mTitleTextView.getMeasuredWidth(), additionalTop + textTop + mTitleTextView.getTextHeight());
        }
        if (mSubtitleTextView != null && mSubtitleTextView.getVisibility() != GONE) {
            int textTop = getActionBarHeight() / 2 + (getActionBarHeight() / 2 - mSubtitleTextView.getTextHeight()) / 2 - Utils.dp(getContext(), !Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 1 : 1);
            mSubtitleTextView.layout(textLeft, additionalTop + textTop, textLeft + mSubtitleTextView.getMeasuredWidth(), additionalTop + textTop + mSubtitleTextView.getTextHeight());
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE || child == mTitleTextView || child == mSubtitleTextView || child == menu || child == mNavigationIconImageView) {
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
        if (menu != null) {
            menu.onMenuButtonPressed();
        }
    }

    protected void onPause() {
        if (menu != null) {
            menu.hideAllPopupMenus();
        }
    }

    public void setAllowOverlayTitle(boolean value) {
        allowOverlayTitle = value;
    }

    public void setTitleOverlayText(String text) {
        if (!allowOverlayTitle) {
            return;
        }
        CharSequence textToSet = text != null ? text : lastTitle;
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

    public void setOccupyStatusBar(boolean value) {
        occupyStatusBar = value;
        if (actionMode != null) {
            actionMode.setPadding(0, occupyStatusBar ? Utils.getStatusBarHeight(getContext()) : 0, 0, 0);
        }
    }

    public boolean getOccupyStatusBar() {
        return occupyStatusBar;
    }

    public void setItemsBackgroundColor(int color) {
        itemsBackgroundColor = color;
        if (mNavigationIconImageView != null) {
            mNavigationIconImageView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }
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
    
    public static class BackDrawable extends Drawable {

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private boolean reverseAngle = false;
        private long lastFrameTime;
        private boolean animationInProgress;
        private float finalRotation;
        private float currentRotation;
        private int currentAnimationTime;
        private boolean alwaysClose;
        private DecelerateInterpolator interpolator = new DecelerateInterpolator();
        private Context context;

        public BackDrawable(Context context, boolean close) {
            super();
            this.context = context;

            paint.setColor(0xFFFFFFFF);
            paint.setStrokeWidth(Utils.dp(context, 2));
            alwaysClose = close;
        }

        public void setRotation(float rotation, boolean animated) {
            lastFrameTime = 0;
            if (currentRotation == 1) {
                reverseAngle = true;
            } else if (currentRotation == 0) {
                reverseAngle = false;
            }
            lastFrameTime = 0;
            if (animated) {
                if (currentRotation < rotation) {
                    currentAnimationTime = (int) (currentRotation * 300);
                } else {
                    currentAnimationTime = (int) ((1.0f - currentRotation) * 300);
                }
                lastFrameTime = System.currentTimeMillis();
                finalRotation = rotation;
            } else {
                finalRotation = currentRotation = rotation;
            }
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            if (currentRotation != finalRotation) {
                if (lastFrameTime != 0) {
                    long dt = System.currentTimeMillis() - lastFrameTime;

                    currentAnimationTime += dt;
                    if (currentAnimationTime >= 300) {
                        currentRotation = finalRotation;
                    } else {
                        if (currentRotation < finalRotation) {
                            currentRotation = interpolator.getInterpolation(currentAnimationTime / 300.0f) * finalRotation;
                        } else {
                            currentRotation = 1.0f - interpolator.getInterpolation(currentAnimationTime / 300.0f);
                        }
                    }
                }
                lastFrameTime = System.currentTimeMillis();
                invalidateSelf();
            }

            int rD = (int) ((117 - 255) * currentRotation);
            int c = Color.rgb(255 + rD, 255 + rD, 255 + rD);
            paint.setColor(c);

            canvas.save();
            canvas.translate(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
            float rotation = currentRotation;
            if (!alwaysClose) {
                canvas.rotate(currentRotation * (reverseAngle ? -225 : 135));
            } else {
                canvas.rotate(135 + currentRotation * (reverseAngle ? -180 : 180));
                rotation = 1.0f;
            }
            canvas.drawLine(-Utils.dp(context, 7) - Utils.dp(context, 1) * rotation, 0, Utils.dp(context, 8), 0, paint);
            float startYDiff = -Utils.dp(context, 0.5f);
            float endYDiff = Utils.dp(context, 7) + Utils.dp(context, 1) * rotation;
            float startXDiff = -Utils.dp(context, 7.0f) + Utils.dp(context, 7.0f) * rotation;
            float endXDiff = Utils.dp(context, 0.5f) - Utils.dp(context, 0.5f) * rotation;
            canvas.drawLine(startXDiff, -startYDiff, endXDiff, -endYDiff, paint);
            canvas.drawLine(startXDiff, startYDiff, endXDiff, endYDiff, paint);
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {}

        @Override
        public void setColorFilter(ColorFilter cf) {}

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return Utils.dp(context, 24);
        }

        @Override
        public int getIntrinsicHeight() {
            return Utils.dp(context, 24);
        }
    }

    public class MenuDrawable extends Drawable {

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private boolean reverseAngle = false;
        private long lastFrameTime;
        private boolean animationInProgress;
        private float finalRotation;
        private float currentRotation;
        private int currentAnimationTime;
        private DecelerateInterpolator interpolator = new DecelerateInterpolator();

        public MenuDrawable() {
            super();
            paint.setColor(0xFFFFFFFF);
            paint.setStrokeWidth(Utils.dp(getContext(), 2));
        }

        public void setRotation(float rotation, boolean animated) {
            lastFrameTime = 0;
            if (currentRotation == 1) {
                reverseAngle = true;
            } else if (currentRotation == 0) {
                reverseAngle = false;
            }
            lastFrameTime = 0;
            if (animated) {
                if (currentRotation < rotation) {
                    currentAnimationTime = (int) (currentRotation * 300);
                } else {
                    currentAnimationTime = (int) ((1.0f - currentRotation) * 300);
                }
                lastFrameTime = System.currentTimeMillis();
                finalRotation = rotation;
            } else {
                finalRotation = currentRotation = rotation;
            }
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            if (currentRotation != finalRotation) {
                if (lastFrameTime != 0) {
                    long dt = System.currentTimeMillis() - lastFrameTime;

                    currentAnimationTime += dt;
                    if (currentAnimationTime >= 300) {
                        currentRotation = finalRotation;
                    } else {
                        if (currentRotation < finalRotation) {
                            currentRotation = interpolator.getInterpolation(currentAnimationTime / 300.0f) * finalRotation;
                        } else {
                            currentRotation = 1.0f - interpolator.getInterpolation(currentAnimationTime / 300.0f);
                        }
                    }
                }
                lastFrameTime = System.currentTimeMillis();
                invalidateSelf();
            }

            canvas.save();
            canvas.translate(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
            canvas.rotate(currentRotation * (reverseAngle ? -180 : 180));
            canvas.drawLine(-Utils.dp(getContext(), 9), 0, Utils.dp(getContext(), 9) - Utils.dp(getContext(), 3.0f) * currentRotation, 0, paint);
            float endYDiff = Utils.dp(getContext(), 5) * (1 - Math.abs(currentRotation)) - Utils.dp(getContext(), 0.5f) * Math.abs(currentRotation);
            float endXDiff = Utils.dp(getContext(), 9) - Utils.dp(getContext(), 2.5f) * Math.abs(currentRotation);
            float startYDiff = Utils.dp(getContext(), 5) + Utils.dp(getContext(), 2.0f) * Math.abs(currentRotation);
            float startXDiff = -Utils.dp(getContext(), 9) + Utils.dp(getContext(), 7.5f) * Math.abs(currentRotation);
            canvas.drawLine(startXDiff, -startYDiff, endXDiff, -endYDiff, paint);
            canvas.drawLine(startXDiff, startYDiff, endXDiff, endYDiff, paint);
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {}

        @Override
        public void setColorFilter(ColorFilter cf) {}

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return Utils.dp(getContext(), 24);
        }

        @Override
        public int getIntrinsicHeight() {
            return Utils.dp(getContext(), 24);
        }
    }
}