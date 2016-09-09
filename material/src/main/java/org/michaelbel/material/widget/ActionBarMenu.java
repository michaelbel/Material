package org.michaelbel.material.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.michaelbel.material.util.Utils;

@SuppressWarnings("unused")
public class ActionBarMenu extends LinearLayout {

    protected ActionBar actionBar;

    public ActionBarMenu(@NonNull Context context) {
        super(context);
    }

    public ActionBarMenu(@NonNull Context context, ActionBar actionBar) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.actionBar = actionBar;
    }

    public View addItemResource(int id, int resId) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resId, null);
        view.setTag(id);
        addView(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));
        view.setLayoutParams(params);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick((Integer) view.getTag());
            }
        });
        return view;
    }

    public ActionBarMenuItem addItem(int id, @DrawableRes Drawable drawable) {
        return addItem(id, 0, actionBar.itemsBackgroundColor, drawable, Utils.dp(getContext(), 48));
    }

    public ActionBarMenuItem addItem(int id, @DrawableRes int icon) {
        return addItem(id, icon, actionBar.itemsBackgroundColor);
    }

    public ActionBarMenuItem addItem(int id, @DrawableRes int icon, int backgroundColor) {
        return addItem(id, icon, backgroundColor, null, Utils.dp(getContext(), 48));
    }

    public ActionBarMenuItem addItemWithWidth(int id, @DrawableRes int icon, int width) {
        return addItem(id, icon, actionBar.itemsBackgroundColor, null, width);
    }

    public ActionBarMenuItem addItem(int id, @DrawableRes int icon, int backgroundColor, Drawable drawable, int width) {
        ActionBarMenuItem menuItem = new ActionBarMenuItem(getContext(), this, backgroundColor);
        menuItem.setTag(id);
        if (drawable != null) {
            menuItem.iconView.setImageDrawable(drawable);
        } else {
            menuItem.iconView.setImageResource(icon);
        }
        addView(menuItem);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) menuItem.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = width;
        menuItem.setLayoutParams(params);
        menuItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.hasSubMenu()) {
                    if (actionBar.actionBarMenuOnItemClick.canOpenMenu()) {
                        item.toggleSubMenu();
                    }
                } else if (item.isSearchField()) {
                    actionBar.onSearchFieldVisibilityChanged(item.toggleSearch(true));
                } else {
                    onItemClick((Integer) view.getTag());
                }
            }
        });
        return menuItem;
    }

    public void hideAllPopupMenus() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).closeSubMenu();
            }
        }
    }

    public void onItemClick(int id) {
        if (actionBar.actionBarMenuOnItemClick != null) {
            actionBar.actionBarMenuOnItemClick.onItemClick(id);
        }
    }

    public void clearItems() {
        removeAllViews();
    }

    public void onMenuButtonPressed() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.getVisibility() != VISIBLE) {
                    continue;
                }
                if (item.hasSubMenu()) {
                    item.toggleSubMenu();
                    break;
                } else if (item.overrideMenuClick) {
                    onItemClick((Integer) item.getTag());
                    break;
                }
            }
        }
    }

    public void closeSearchField() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    actionBar.onSearchFieldVisibilityChanged(item.toggleSearch(false));
                    break;
                }
            }
        }
    }

    public void openSearchField(boolean toggle, String text) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    if (toggle) {
                        actionBar.onSearchFieldVisibilityChanged(item.toggleSearch(true));
                    }
                    item.getSearchField().setText(text);
                    item.getSearchField().setSelection(text.length());
                    break;
                }
            }
        }
    }

    public ActionBarMenuItem getItem(int id) {
        View v = findViewWithTag(id);
        if (v instanceof ActionBarMenuItem) {
            return (ActionBarMenuItem) v;
        }
        return null;
    }
}