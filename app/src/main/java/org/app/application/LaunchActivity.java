/*
 * Copyright 2015 Michael Bel
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

package org.app.application;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.fragments.CardFragment;
import org.app.application.fragments.DialogsFragment;
import org.app.application.fragments.FabFragment;
import org.app.application.fragments.ListViewFragment;
import org.app.application.fragments.NumberViewFragment;
import org.app.application.fragments.PatternViewFragment;
import org.app.application.fragments.RecyclerFragment;
import org.app.application.fragments.SwitchesFragment;
import org.app.material.AndroidUtilities;
import org.app.material.widget.ActionBar;
import org.app.material.widget.ActionBarMenu;
import org.app.material.widget.ActionBarMenuItem;
import org.app.material.widget.Browser;
import org.app.material.widget.FragmentsPagerAdapter;

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
                .setToolbarColor(AndroidUtilities.getThemeColor(R.attr.colorPrimary))
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
        viewPagerParams.setMargins(0, AndroidUtilities.isLandscape() ?
                AndroidUtilities.dp(96) : AndroidUtilities.dp(104), 0, 0);
        viewPager.setLayoutParams(viewPagerParams);

        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new ListViewFragment(), R.string.ListViewAdapter);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new RecyclerFragment(), R.string.RecyclerView);
        adapter.addFragment(new NumberViewFragment(), R.string.NumberView);
        adapter.addFragment(new FabFragment(), R.string.Fabs);
        adapter.addFragment(new PatternViewFragment(), R.string.Pattern);
        adapter.addFragment(new SwitchesFragment(), R.string.Switch);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(AndroidUtilities.getThemeColor(R.attr.colorPrimary));
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
        tabLayoutParams.setMargins(0, ActionBar.getCurrentActionBarHeight(), 0, 0);
        tabLayout.setLayoutParams(tabLayoutParams);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}