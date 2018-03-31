package org.michaelbel.material.widget2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.michaelbel.material.R;
import org.michaelbel.material.util2.Utils;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class ActionBarMenuItem extends FrameLayout {

    private static final String TAG = ActionBarMenuItem.class.getSimpleName();
    private static final int CLOSE_ICON_ANIMATION_DURATION = 220;

    private ImageView mClearIcon;
    private EditText mSearchEditText;
    private @ColorInt int popupMenuBackgroundColor;

    public static class ActionBarMenuItemSearchListener {

        public void onSearchExpand() {}

        public boolean canCollapseSearch() {
            return true;
        }

        public void onSearchCollapse() {}

        public void onTextChanged(EditText editText) {}

        public void onSearchPressed(EditText editText) {}
    }

    public interface ActionBarMenuItemDelegate {
        void onItemClick(int id);
    }

    private ActionBarPopupMenu.ActionBarPopupMenuLayout popupLayout;
    private ActionBarMenu parentMenu;
    private ActionBarPopupMenu popupWindow;
    protected ImageView iconView;
    private FrameLayout searchContainer;
    private boolean isSearchField = false;
    private ActionBarMenuItemSearchListener listener;
    private Rect rect;
    private int[] location;
    private View selectedMenuView;
    private Runnable showMenuRunnable;
    private boolean showFromBottom;
    private int menuHeight = Utils.dp(getContext(), 16);
    private int subMenuOpenSide = 0;
    private ActionBarMenuItemDelegate delegate;
    private boolean allowCloseAnimation = true;
    protected boolean overrideMenuClick;
    private boolean processedPopupClick;

    public static Handler handler = new Handler(Looper.getMainLooper());

    public ActionBarMenuItem(Context context) {
        super(context);
    }

    public ActionBarMenuItem(Context context, ActionBarMenu menu, int backgroundColor) {
        super(context);
        if (backgroundColor != 0) {
            this.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        }

        parentMenu = menu;

        iconView = new ImageView(context);
        iconView.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        iconView.setScaleType(ImageView.ScaleType.CENTER);
        addView(iconView);
        LayoutParams layoutParams = (LayoutParams) iconView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        iconView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (hasSubMenu() && (popupWindow == null || popupWindow != null && !popupWindow.isShowing())) {
                showMenuRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        toggleSubMenu();
                    }
                };
                runOnUIThread(showMenuRunnable, 200);
            }
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (hasSubMenu() && (popupWindow == null || popupWindow != null && !popupWindow.isShowing())) {
                if (event.getY() > getHeight()) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    toggleSubMenu();
                    return true;
                }
            } else if (popupWindow != null && popupWindow.isShowing()) {
                getLocationOnScreen(location);
                float x = event.getX() + location[0];
                float y = event.getY() + location[1];
                popupLayout.getLocationOnScreen(location);
                x -= location[0];
                y -= location[1];
                selectedMenuView = null;
                for (int a = 0; a < popupLayout.getItemsCount(); a++) {
                    View child = popupLayout.getItemAt(a);
                    child.getHitRect(rect);
                    if ((Integer) child.getTag() < 100) {
                        if (!rect.contains((int) x, (int) y)) {
                            child.setPressed(false);
                            child.setSelected(false);
                            if (Build.VERSION.SDK_INT == 21) {
                                child.getBackground().setVisible(false, false);
                            }
                        } else {
                            child.setPressed(true);
                            child.setSelected(true);
                            if (Build.VERSION.SDK_INT >= 21) {
                                if (Build.VERSION.SDK_INT == 21) {
                                    child.getBackground().setVisible(true, false);
                                }
                                child.drawableHotspotChanged(x, y - child.getTop());
                            }
                            selectedMenuView = child;
                        }
                    }
                }
            }
        } else if (popupWindow != null && popupWindow.isShowing() && event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (selectedMenuView != null) {
                selectedMenuView.setSelected(false);
                if (parentMenu != null) {
                    parentMenu.onItemClick((Integer) selectedMenuView.getTag());
                } else if (delegate != null) {
                    delegate.onItemClick((Integer) selectedMenuView.getTag());
                }
                popupWindow.dismiss(allowCloseAnimation);
            } else {
                popupWindow.dismiss();
            }
        } else {
            if (selectedMenuView != null) {
                selectedMenuView.setSelected(false);
                selectedMenuView = null;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setDelegate(ActionBarMenuItemDelegate delegate) {
        this.delegate = delegate;
    }

    public void setShowFromBottom(boolean value) {
        showFromBottom = value;
        if (popupLayout != null) {
            popupLayout.setShowedFromBottom(showFromBottom);
        }
    }

    public void setSubMenuOpenSide(int side) {
        subMenuOpenSide = side;
    }

    public TextView addSubItem(int id, @NonNull String text) {
        return addSubItem(id, text, null);
    }

    public TextView addSubItem(int id, @StringRes int textId) {
        return addSubItem(id, getContext().getText(textId), null);
    }

    public TextView addSubItem(int id, @StringRes int textId, @DrawableRes int resId) {
        return addSubItem(id, getContext().getText(textId), ContextCompat.getDrawable(getContext(), resId));
    }

    public TextView addSubItem(int id, @StringRes int textId, @DrawableRes Drawable resId) {
        return addSubItem(id, getContext().getText(textId), resId);
    }

    public TextView addSubItem(int id, @NonNull CharSequence text, @DrawableRes Drawable resId) {
        if (popupLayout == null) {
            rect = new Rect();
            location = new int[2];
            popupLayout = new ActionBarPopupMenu.ActionBarPopupMenuLayout(getContext());
            popupLayout.setBackgroundColor(popupMenuBackgroundColor); // Beta.
            popupLayout.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            v.getHitRect(rect);
                            if (!rect.contains((int) event.getX(), (int) event.getY())) {
                                popupWindow.dismiss();
                            }
                        }
                    }
                    return false;
                }
            });
            popupLayout.setDispatchKeyEventListener(new ActionBarPopupMenu.OnDispatchKeyEventListener() {
                @Override
                public void onDispatchKeyEvent(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0 && popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        }

        TextView textView = new TextView(getContext());
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
        textView.setBackgroundResource(Utils.selectableItemBackground(getContext()));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(Utils.dp(getContext(), 16), 0, Utils.dp(getContext(), 16), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setMinWidth(Utils.dp(getContext(), 196));
        textView.setTag(id);
        textView.setText(text);
        if (resId != null) {
            textView.setCompoundDrawablePadding(Utils.dp(getContext(), 16));
            textView.setCompoundDrawablesWithIntrinsicBounds(resId, null, null, null);
        }
        popupLayout.setShowedFromBottom(showFromBottom);
        popupLayout.addView(textView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = Utils.dp(getContext(), 48);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    if (processedPopupClick) {
                        return;
                    }
                    processedPopupClick = true;
                    popupWindow.dismiss(allowCloseAnimation);
                }
                if (parentMenu != null) {
                    parentMenu.onItemClick((Integer) view.getTag());
                } else if (delegate != null) {
                    delegate.onItemClick((Integer) view.getTag());
                }
            }
        });
        menuHeight += layoutParams.height;
        return textView;
    }

    public boolean hasSubMenu() {
        return popupLayout != null;
    }

    public void toggleSubMenu() {
        if (popupLayout == null) {
            return;
        }
        if (showMenuRunnable != null) {
            cancelRunOnUIThread(showMenuRunnable);
            showMenuRunnable = null;
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        if (popupWindow == null) {
            popupWindow = new ActionBarPopupMenu(getContext(), popupLayout, LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= 19) {
                popupWindow.setAnimationStyle(0);
            } else {
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
            }
            popupWindow.setOutsideTouchable(true);
            popupWindow.setClippingEnabled(true);
            popupWindow.setInputMethodMode(ActionBarPopupMenu.INPUT_METHOD_NOT_NEEDED);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
            popupLayout.measure(MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 1000), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(Utils.dp(getContext(), 1000), MeasureSpec.AT_MOST));
            popupWindow.getContentView().setFocusableInTouchMode(true);
            popupWindow.getContentView().setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP && popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
        processedPopupClick = false;
        popupWindow.setFocusable(true);
        if (popupLayout.getMeasuredWidth() == 0) {
            updateOrShowPopup(true, true);
        } else {
            updateOrShowPopup(true, false);
        }
        popupWindow.startAnimation();
    }

    public void openSearch(boolean openKeyboard) {
        if (searchContainer == null || searchContainer.getVisibility() == VISIBLE || parentMenu == null) {
            return;
        }
        parentMenu.actionBar.onSearchFieldVisibilityChanged(toggleSearch(openKeyboard));
    }

    public boolean toggleSearch(boolean openKeyboard) {
        if (searchContainer == null) {
            return false;
        }
        if (searchContainer.getVisibility() == VISIBLE) {
            if (listener == null || listener != null && listener.canCollapseSearch()) {
                searchContainer.setVisibility(GONE);
                mSearchEditText.clearFocus();
                setVisibility(VISIBLE);
                Utils.hideKeyboard(mSearchEditText);
                if (listener != null) {
                    listener.onSearchCollapse();
                }
            }
            return false;
        } else {
            searchContainer.setVisibility(VISIBLE);
            setVisibility(GONE);
            mSearchEditText.setText(null);
            mSearchEditText.requestFocus();
            if (openKeyboard) {
                Utils.showKeyboard();
                //Utils.showKeyboard(mSearchEditText);
            }
            if (listener != null) {
                listener.onSearchExpand();
            }
            return true;
        }
    }

    public void closeSubMenu() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setIcon(int resId) {
        iconView.setImageResource(resId);
    }

    public ImageView getImageView() {
        return iconView;
    }

    public EditText getSearchField() {
        return mSearchEditText;
    }

    public ActionBarMenuItem setOverrideMenuClick(boolean value) {
        overrideMenuClick = value;
        return this;
    }

    public ActionBarMenuItem setIsSearchField(boolean value) {
        if (parentMenu == null) {
            return this;
        }

        if (value && searchContainer == null) {
            searchContainer = new FrameLayout(getContext());
            parentMenu.addView(searchContainer, 0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) searchContainer.getLayoutParams();
            layoutParams.weight = 1;
            layoutParams.width = 0;
            layoutParams.height = LayoutHelper.MATCH_PARENT;
            layoutParams.leftMargin = Utils.dp(getContext(), 6);
            searchContainer.setLayoutParams(layoutParams);
            searchContainer.setVisibility(GONE);

            mSearchEditText = new EditText(getContext());
            mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Utils.isLandscape(getContext()) ? 16 : 18);
            mSearchEditText.setHintTextColor(0x88FFFFFF);
            mSearchEditText.setTextColor(0xFFFFFFFF);
            mSearchEditText.setSingleLine(true);
            mSearchEditText.setBackgroundResource(0);
            mSearchEditText.setPadding(0, 0, 0, 0);
            int inputType = mSearchEditText.getInputType() | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            mSearchEditText.setInputType(inputType);
            mSearchEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {}

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
            mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event != null && (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        Utils.hideKeyboard(mSearchEditText);
                        if (listener != null) {
                            listener.onSearchPressed(mSearchEditText);
                        }
                    }
                    return false;
                }
            });
            mSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (listener != null) {
                        listener.onTextChanged(mSearchEditText);
                    }
                    if (mClearIcon != null) {
                        showClearIcon(!(s == null || s.length() == 0));
                    }
                }
            });

            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mCursorDrawableRes.set(mSearchEditText, R.drawable.search_carret);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            mSearchEditText.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_ACTION_SEARCH);
            mSearchEditText.setTextIsSelectable(false);
            searchContainer.addView(mSearchEditText);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mSearchEditText.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = Utils.dp(getContext(), 36);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.rightMargin = Utils.dp(getContext(), 56);
            mSearchEditText.setLayoutParams(params);

            mClearIcon = new ImageView(getContext());
            mClearIcon.setImageResource(android.R.drawable.ic_delete);
            mClearIcon.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
            mClearIcon.setScaleType(ImageView.ScaleType.CENTER);
            mClearIcon.setPadding(
                    Utils.dp(getContext(), 8),
                    Utils.dp(getContext(), 8),
                    Utils.dp(getContext(), 8),
                    Utils.dp(getContext(), 8)
            );
            mClearIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchEditText.setText(null);
                    mSearchEditText.requestFocus();
                    Utils.showKeyboard();
                    //Utils.showKeyboard(mSearchEditText);
                    showClearIcon(false);
                }
            });

            mClearIcon.setVisibility(INVISIBLE);
            mClearIcon.setRotation(-135F);
            mClearIcon.setScaleY(0.0F);
            mClearIcon.setScaleX(0.0F);

            searchContainer.addView(mClearIcon);
            params = (FrameLayout.LayoutParams) mClearIcon.getLayoutParams();
            params.width = Utils.dp(getContext(), 24);
            params.height = Utils.dp(getContext(), 24);
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
            params.leftMargin = Utils.dp(getContext(), 16);
            params.rightMargin = Utils.dp(getContext(), 16);
            mClearIcon.setLayoutParams(params);
        }
        isSearchField = value;
        return this;
    }

    public ActionBarMenuItem setClearIcon(@DrawableRes int icon) {
        if (mClearIcon != null) {
            mClearIcon.setImageResource(icon);
        }
        return this;
    }

    public ActionBarMenuItem setClearIcon(@DrawableRes Drawable resId) {
        if (mClearIcon != null) {
            mClearIcon.setImageDrawable(resId);
        }
        return this;
    }

    public ActionBarMenuItem setSearchHint(CharSequence text) {
        mSearchEditText.setHint(text);
        return this;
    }

    public ActionBarMenuItem setSearchHint(@StringRes int stringId) {
        setSearchHint(getContext().getText(stringId));
        return this;
    }

    public ActionBarMenuItem setPopupMenuBackgroundColor(@ColorInt int color) {
        popupMenuBackgroundColor = color;
        return this;
    }

    @UiThread
    private void showClearIcon(boolean value) {
        AnimatorSet animatorSet = new AnimatorSet();

        if (value && mClearIcon.getVisibility() != VISIBLE && !animatorSet.isRunning()) {
            mClearIcon.setVisibility(VISIBLE);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(mClearIcon, "scaleY", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(mClearIcon, "scaleX", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(mClearIcon, "rotation", -90F, 0F)
            );
        } else if (!value && mClearIcon.getVisibility() == VISIBLE && !animatorSet.isRunning()) {
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(mClearIcon, "scaleY", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(mClearIcon, "scaleX", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(mClearIcon, "rotation", 0F, -90F)
            );
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mClearIcon.setVisibility(INVISIBLE);
                }
            });
        }

        animatorSet.setDuration(CLOSE_ICON_ANIMATION_DURATION);
        animatorSet.start();
    }

    public boolean isSearchField() {
        return isSearchField;
    }

    public ActionBarMenuItem setActionBarMenuItemSearchListener(ActionBarMenuItemSearchListener listener) {
        this.listener = listener;
        return this;
    }

    public ActionBarMenuItem setAllowCloseAnimation(boolean value) {
        allowCloseAnimation = value;
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (popupWindow != null && popupWindow.isShowing()) {
            updateOrShowPopup(false, true);
        }
    }

    private void updateOrShowPopup(boolean show, boolean update) {
        int offsetY;
        if (showFromBottom) {
            getLocationOnScreen(location);
            int diff = location[1] - Utils.getStatusBarHeight(getContext()) + getMeasuredHeight() - menuHeight;
            offsetY = -menuHeight;
            if (diff < 0) {
                offsetY -= diff;
            }
        } else {
            if (parentMenu != null && subMenuOpenSide == 0) {
                offsetY = -parentMenu.actionBar.getMeasuredHeight() + parentMenu.getTop();
            } else {
                offsetY = -getMeasuredHeight();
            }
        }

        if (show) {
            popupLayout.scrollToTop();
        }

        if (subMenuOpenSide == 0) {
            if (showFromBottom) {
                if (show) {
                    popupWindow.showAsDropDown(this, -popupLayout.getMeasuredWidth() + getMeasuredWidth(), offsetY);
                }
                if (update) {
                    popupWindow.update(this, -popupLayout.getMeasuredWidth() + getMeasuredWidth(), offsetY, -1, -1);
                }
            } else {
                if (parentMenu != null) {
                    View parent = parentMenu.actionBar;
                    if (show) {
                        popupWindow.showAsDropDown(parent, getLeft() + parentMenu.getLeft() + getMeasuredWidth() - popupLayout.getMeasuredWidth(), offsetY);
                    }
                    if (update) {
                        popupWindow.update(parent, getLeft() + parentMenu.getLeft() + getMeasuredWidth() - popupLayout.getMeasuredWidth(), offsetY, -1, -1);
                    }
                } else if (getParent() != null) {
                    View parent = (View) getParent();
                    if (show) {
                        popupWindow.showAsDropDown(parent, parent.getMeasuredWidth() - popupLayout.getMeasuredWidth() - getLeft() - parent.getLeft(), offsetY);
                    }
                    if (update) {
                        popupWindow.update(parent, parent.getMeasuredWidth() - popupLayout.getMeasuredWidth() - getLeft() - parent.getLeft(), offsetY, -1, -1);
                    }
                }
            }
        } else {
            if (show) {
                popupWindow.showAsDropDown(this, -Utils.dp(getContext(), 8), offsetY);
            }
            if (update) {
                popupWindow.update(this, -Utils.dp(getContext(), 8), offsetY, -1, -1);
            }
        }
    }

    public void hideSubItem(int id) {
        View view = popupLayout.findViewWithTag(id);
        if (view != null) {
            view.setVisibility(GONE);
        }
    }

    public void showSubItem(int id) {
        View view = popupLayout.findViewWithTag(id);
        if (view != null) {
            view.setVisibility(VISIBLE);
        }
    }

    private void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, delay);
        }
    }

    private void cancelRunOnUIThread(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }
}