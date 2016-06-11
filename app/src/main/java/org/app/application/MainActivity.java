/*
 * Copyright 2016 Michael Bel
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
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.ui.BottomsFragment;
import org.app.application.ui.CardFragment;
import org.app.application.ui.DialogsFragment;
import org.app.application.ui.FabFragment;
import org.app.application.ui.ListViewFragment;
import org.app.application.ui.Recycler;
import org.app.application.ui.RecyclerFragment;
import org.app.material.OnClickListener;
import org.app.material.widget.Browser;
import org.app.material.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Toolbar toolbar;
    private FrameLayout layout;

    private int dotsMenu = 1;
    private int github = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        layout = (FrameLayout) findViewById(R.id.frameLayout);


        toolbar = new Toolbar(this)
                .setTitle(R.string.MaterialDemo)
                .setNavIcon(R.drawable.ic_menu)
                .setNavIconClickListener(new OnClickListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "OnClick", Toast.LENGTH_SHORT).show();
                    }
                })
                .addIcons(
                        new org.app.material.widget.Toolbar.ToolbarIcon(this).setToolbarIcon(R.drawable.ic_dots_menu)
                                .setOnClick(new OnClickListener() {
                            @Override
                            public void onClick() {
                                Browser.openUrl(MainActivity.this, getString(R.string.GithubURL));
                            }
                        })
                );
        layout.addView(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new ListViewFragment(), R.string.ListView);
        adapter.addFragment(new CardFragment(), R.string.CardView);
        adapter.addFragment(new RecyclerFragment(), R.string.RecyclerView);
        adapter.addFragment(new Recycler(), R.string.RecyclerView);
        adapter.addFragment(new FabFragment(), R.string.Fab);
        adapter.addFragment(new BottomsFragment(), R.string.Bottoms);

        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    menu.add(0, github, Menu.NONE, getString(R.string.OpenGithub)).setIcon(R.drawable.ic_github).setShowAsAction(1);
    //    menu.add(0, dotsMenu, Menu.NONE, getString(R.string.PopupMenu)).setIcon(R.drawable.ic_dots_menu).setShowAsAction(1);
    //    return super.onCreateOptionsMenu(menu);
    //}

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    if (item.getItemId() == github) {
    //
    //    } else if (item.getItemId() == dotsMenu) {
    //        startActivity(new Intent(MainActivity.this, Test.class));
    //    }
    //    return super.onOptionsItemSelected(item);
    //}

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<CharSequence> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, @StringRes int resId) {
            fragmentList.add(fragment);
            fragmentTitleList.add(getResources().getText(resId));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}