package org.michaelbel.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setNavigationIcon(R.drawable.ic_menu);
        actionBar.setTitle(R.string.MaterialDemo);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == ActionBar.MENU_ICON) {
                    Toast.makeText(LaunchActivity.this, "Drawer is not implemented", Toast.LENGTH_SHORT).show();
                } else if (id == github) {
                    Browser.openUrl(LaunchActivity.this, R.string.GithubURL);
                } else if (id == settings) {
                    actionBar.onMenuButtonPressed();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(github, R.drawable.ic_github);

        ActionBarMenuItem item = menu.addItem(0, R.drawable.ic_dots_menu);
        item.addSubItem(settings, getString(R.string.Settings), 0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new FabFragment(), R.string.Fabs);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(Utils.getAttrColor(R.attr.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*if (tab.getPosition() == 0) {}*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}