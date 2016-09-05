package org.michaelbel.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.michaelbel.app.fragments.CardFragment;
import org.michaelbel.app.fragments.DialogsFragment;
import org.michaelbel.app.fragments.FabFragment;
import org.michaelbel.material.Utils;
import org.michaelbel.material.widget.ActionBar;
import org.michaelbel.material.widget.ActionBarMenu;
import org.michaelbel.material.widget.ActionBarMenuItem;
import org.michaelbel.material.widget.Browser;
import org.michaelbel.material.widget.FragmentsPagerAdapter;

public class LaunchActivity extends FragmentActivity {

    private static final int github = 1;
    private static final int settings = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
        layout.setBackgroundColor(0xFFF0F0F0);

        ActionBar actionBar = new ActionBar(this)
                .setNavigationIcon(R.drawable.ic_menu)
                .setTitle(R.string.MaterialDemo)
                .setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                    @Override
                    public void onItemClick(int id) {
                        if (id == -1) {
                            Toast.makeText(LaunchActivity.this, "Drawer is not implemented",
                                    Toast.LENGTH_SHORT).show();
                        } else if (id == github) {
                            Browser.openUrl(LaunchActivity.this, R.string.GithubURL);
                        } else if (id == settings) {

                        }
                    }
                });

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(github, R.drawable.ic_github);

        ActionBarMenuItem item = menu.addItem(0, R.drawable.ic_dots_menu);
        item.addSubItem(settings, R.string.Settings, 0);

        layout.addView(actionBar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        FrameLayout.LayoutParams viewPagerParams = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        viewPagerParams.setMargins(0, Utils.isLandscape(this) ?
                Utils.dp(this, 96) : Utils.dp(this, 104), 0, 0);
        viewPager.setLayoutParams(viewPagerParams);

        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new FabFragment(), R.string.Fabs);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(Utils.getAttrColor(R.attr.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*if (tab.getPosition() == 4) {}*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        FrameLayout.LayoutParams tabLayoutParams = (FrameLayout.LayoutParams)
                tabLayout.getLayoutParams();
        tabLayoutParams.setMargins(0, actionBar.getCurrentActionBarHeightDp(), 0, 0);
        tabLayout.setLayoutParams(tabLayoutParams);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}