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
import android.view.View;
import android.widget.FrameLayout;

import org.app.application.cells.DrawerHeaderCell;
import org.app.application.fragments.BottomsFragment;
import org.app.application.fragments.CardFragment;
import org.app.application.fragments.DialogsFragment;
import org.app.application.fragments.FabFragment;
import org.app.application.fragments.ListViewFragment;
import org.app.application.fragments.RecyclerFragment;
import org.app.material.AndroidUtilities;
import org.app.material.Drawer.Drawer;
import org.app.material.Drawer.DrawerBuilder;
import org.app.material.Drawer.model.PrimaryDrawerItem;
import org.app.material.Drawer.model.interfaces.IDrawerItem;
import org.app.material.widget.Browser;
import org.app.material.widget.FragmentsPagerAdapter;
import org.app.material.widget.OnClickListener;
import org.app.material.widget.Toolbar;

public class LaunchActivity extends FragmentActivity {

    private Drawer drawer;
    private TabLayout tabLayout;
    private TabLayout.Tab tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FrameLayout toolbarLayout = (FrameLayout) findViewById(R.id.frameLayout);

        Toolbar toolbar = new Toolbar(this)
                .setTitle(R.string.MaterialDemo)
                .setNavIcon(R.drawable.ic_menu)
                .setNavIconClickListener(new OnClickListener() {
                    @Override
                    public void onClick() {
                        drawer.openDrawer();
                    }
                })
                .addIcons(
                        new Toolbar.ToolbarIcon(this).setToolbarIcon(R.drawable.ic_dots_menu)
                                .setOnClick(new OnClickListener() {
                                    @Override
                                    public void onClick() {
                                        Browser.openUrl(LaunchActivity.this, getString(R.string.GithubURL));
                                    }
                                })
                );
        toolbarLayout.addView(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new BottomsFragment(), R.string.Bottoms);
        adapter.addFragment(new ListViewFragment(), R.string.ListView);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new FabFragment(), R.string.Fabs);
        adapter.addFragment(new RecyclerFragment(), R.string.RecyclerView);

        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(AndroidUtilities.getContextColor(this, R.attr.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);

        drawer = new DrawerBuilder(this)
                .setHasStableIds(true)
                .setSavedInstance(savedInstanceState)
                .setHeader(new DrawerHeaderCell(this))
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .setName(R.string.Dialogs)
                                .setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_edit, 0xFF616161))
                                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        tab = tabLayout.getTabAt(0);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                        return false;
                                    }
                                }),
                        new PrimaryDrawerItem()
                                .setName(R.string.Bottoms)
                                .setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_edit, 0xFF616161))
                                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        tab = tabLayout.getTabAt(1);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                        return false;
                                    }
                                }),
                        new PrimaryDrawerItem()
                                .setName(R.string.ListView)
                                .setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_edit, 0xFF616161))
                                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        tab = tabLayout.getTabAt(2);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                        return false;
                                    }
                                }),
                        new PrimaryDrawerItem()
                                .setName(R.string.CardView)
                                .setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_edit, 0xFF616161))
                                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        tab = tabLayout.getTabAt(3);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                        return false;
                                    }
                                }),
                        new PrimaryDrawerItem()
                                .setName(R.string.Fabs)
                                .setIcon(AndroidUtilities.getIcon(this, R.drawable.ic_edit, 0xFF616161))
                                .setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        tab = tabLayout.getTabAt(4);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                        return false;
                                    }
                                })
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }
                })
                .build();
    }
}