package org.michaelbel;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import org.michaelbel.app.R;
import org.michaelbel.fragment.CardFragment;
import org.michaelbel.fragment.DialogsFragment;
import org.michaelbel.fragment.FabFragment;
import org.michaelbel.material.util2.Utils;
import org.michaelbel.material.widget.ParallaxViewPager;
import org.michaelbel.material.widget2.ActionBar;
import org.michaelbel.material.widget2.ActionBarMenu;
import org.michaelbel.material.widget2.ActionBarMenuItem;
import org.michaelbel.material.widget2.FragmentsPagerAdapter;

public class LaunchActivity extends FragmentActivity {

    private static final int github = 1;
    private static final int settings = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = findViewById(R.id.action_bar);
        actionBar.setNavigationIcon(R.drawable.ic_menu);
        actionBar.setTitle(R.string.MaterialDemo);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == ActionBar.MENU_ICON) {
                    //startActivity(new Intent(LaunchActivity.this, ActionmodActivity.class));
                } else if (id == github) {
                    //Browser.openUrl(LaunchActivity.this, R.string.GithubURL);
                    //startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                } else if (id == settings) {
                    //startActivity(new Intent(LaunchActivity.this, SearchableActivity.class));
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(github, R.drawable.ic_github);

        ActionBarMenuItem item = menu.addItem(0, R.drawable.ic_dots_menu);
        //item.setPopupMenuBackgroundColor(0xFF303030);

        item.addSubItem(settings, R.string.Settings);

        ParallaxViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setMode(ParallaxViewPager.Mode.RIGHT_OVERLAY);

        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new FabFragment(), R.string.Fabs);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(Utils.getAttrColor(this, R.attr.colorPrimary));
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
}