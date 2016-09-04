package org.michaelbel.material.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.michaelbel.material.Utils;

@SuppressWarnings("unused")
public class ActionBarMenu extends LinearLayout {

    protected ActionBar parentActionBar;

    public ActionBarMenu(Context context, ActionBar layer) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        parentActionBar = layer;

        Utils.bind(context);
    }

    public ActionBarMenu(Context context) {
        super(context);

        Utils.bind(context);
    }

    public View addItemResource(int id, int resourceId) {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(resourceId, null);
        view.setTag(id);
        addView(view);

        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.height = LayoutHelper.MATCH_PARENT;

        view.setBackgroundResource(Utils.selectableItemBackgroundBorderless(getContext()));

        view.setLayoutParams(layoutParams);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick((Integer) view.getTag());
            }
        });
        return view;
    }

    public ActionBarMenuItem addItem(int id, Drawable drawable) {
        return addItem(id, 0, drawable, Utils.dp(getContext(), 48));
    }

    public ActionBarMenuItem addItem(int id, int icon) {
        return addItem(id, icon, null, Utils.dp(getContext(), 48));
    }

    public ActionBarMenuItem addItemWithWidth(int id, int icon, int width) {
        return addItem(id, icon, null, width);
    }

    public ActionBarMenuItem addItem(int id, int icon, Drawable drawable, int width) {
        ActionBarMenuItem menuItem = new ActionBarMenuItem(getContext(), this);
        menuItem.setTag(id);

        if (drawable != null) {
            menuItem.iconView.setImageDrawable(drawable);
        } else {
            menuItem.iconView.setImageResource(icon);
        }

        addView(menuItem);
        LayoutParams layoutParams = (LayoutParams) menuItem.getLayoutParams();
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        layoutParams.width = width;
        menuItem.setLayoutParams(layoutParams);
        menuItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;

                if (item.hasSubMenu()) {
                    if (parentActionBar.mActionBarMenuOnItemClick.canOpenMenu()) {
                        item.toggleSubMenu();
                    }
                } else if (item.isSearchField()) {
                    parentActionBar.onSearchFieldVisibilityChanged(item.toggleSearch(true));
                } else {
                    onItemClick((Integer) view.getTag());
                }
            }
        });

        return menuItem;
    }

    public void hideAllPopupMenus() {
        for (int a = 0; a < getChildCount(); a++) {
            View view = getChildAt(a);

            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).closeSubMenu();
            }
        }
    }

    public void onItemClick(int id) {
        if (parentActionBar.mActionBarMenuOnItemClick != null) {
            parentActionBar.mActionBarMenuOnItemClick.onItemClick(id);
        }
    }

    public void clearItems() {
        removeAllViews();
    }

    public void onMenuButtonPressed() {
        for (int a = 0; a < getChildCount(); a++) {
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
        for (int a = 0; a < getChildCount(); a++) {
            View view = getChildAt(a);

            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;

                if (item.isSearchField()) {
                    parentActionBar.onSearchFieldVisibilityChanged(item.toggleSearch(false));
                    break;
                }
            }
        }
    }

    public void openSearchField(boolean toggle, String text) {
        for (int a = 0; a < getChildCount(); a++) {
            View view = getChildAt(a);

            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;

                if (item.isSearchField()) {
                    if (toggle) {
                        parentActionBar.onSearchFieldVisibilityChanged(item.toggleSearch(true));
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