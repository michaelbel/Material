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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.app.application.ui.DialogsFragment;
import org.app.application.ui.FabFragment;
import org.app.application.ui.ListViewFragment;
import org.app.material.AndroidUtilities;
import org.app.material.widget.Browser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int dotsMenu = 1;
    private int github = 2;

    enum ProgressType {
        INDETERMINATE, PROGRESS_POSITIVE, PROGRESS_NEGATIVE, HIDDEN, PROGRESS_NO_ANIMATION, PROGRESS_NO_BACKGROUND
    }

    int mMaxProgress = 100;
    private Handler mUiHandler = new Handler();
    LinkedList<ProgressType> mProgressTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setTitle(R.string.MaterialDemo);
        } catch (Exception ignored) {
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialogsFragment(), R.string.Dialogs);
        adapter.addFragment(new ListViewFragment(), R.string.ListView);
        //adapter.addFragment(new CardFragment(), R.string.CardView);
        //adapter.addFragment(new RecyclerFragment(), R.string.RecyclerView);
        adapter.addFragment(new FabFragment(), R.string.Fab);

        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, github, Menu.NONE, getString(R.string.OpenGithub)).setIcon(R.drawable.ic_github).setShowAsAction(1);
        menu.add(0, dotsMenu, Menu.NONE, getString(R.string.PopupMenu)).setIcon(R.drawable.ic_dots_menu).setShowAsAction(1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == github) {
            Browser browser = new Browser(this);
            browser.setUrl(getString(R.string.GithubURL))
                   .setToolbarColor(AndroidUtilities.getContextColor(this, R.attr.colorPrimary))
                   .setShareIcon(true)
                   .setShareIconHiddenText(getString(R.string.ShareLink))
                   .show();
        } else if (item.getItemId() == dotsMenu) {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.GithubURL))));
            startActivity(new Intent(MainActivity.this, ViewController.class));
        }

        return super.onOptionsItemSelected(item);
    }

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

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
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