package org.app.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import org.app.material.AndroidUtilities;
import org.app.material.widget.ActionBar;
import org.app.material.widget.ActionBarMenu;

public class SettingsActivity extends FragmentActivity {

    private FrameLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new FrameLayout(this);
        layout.setId(R.id.layout);
        layout.setBackgroundColor(0xFFF0F0F0);

        ActionBar actionBar = new ActionBar(this)
                .setBackGroundColor(AndroidUtilities.getContextColor(R.attr.colorPrimary))
                .setBackButtonImage(R.drawable.ic_arrow_back)
                .setTitle(R.string.Settings)
                .setOccupyStatusBar(false)
                .setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                    @Override
                    public void onItemClick(int id) {
                        if (id == -1) {
                            finish();
                        } else if (id == 2) {

                        }
                    }
                });
        actionBar.setElevation(AndroidUtilities.dp(2.0F));

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(2, R.drawable.ic_github);

        layout.addView(actionBar);

        setContentView(layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void presentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .commit();
    }

    public void presentFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void presentFragment(Fragment fragment, Bundle args, String tag) {
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(layout.getId(), fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void finishFragment() {
        getSupportFragmentManager().popBackStack();
    }
}