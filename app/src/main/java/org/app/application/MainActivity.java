package org.app.application;

import android.os.Bundle;
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

import org.app.application.ui.CardFragment;
import org.app.application.ui.DialogFragment;
import org.app.application.ui.FabFragment;
import org.app.application.ui.ListViewFragment;
import org.app.application.ui.RecyclerFragment;
import org.app.material.widget.Browser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int dotsMenu = 1;
    private int github = 2;

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
        } catch (Exception ignored) {}

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialogFragment(), getResources().getString(R.string.Dialogs));
        adapter.addFragment(new CardFragment(), getResources().getString(R.string.CardView));
        adapter.addFragment(new ListViewFragment(), getResources().getString(R.string.ListView));
        adapter.addFragment(new RecyclerFragment(), getResources().getString(R.string.RecyclerView));
        adapter.addFragment(new FabFragment(), getResources().getString(R.string.Fab));

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
        menu.add(0, github, Menu.NONE, getResources().getString(R.string.OpenGithub)).setIcon(R.drawable.ic_github).setShowAsAction(1);
        menu.add(0, dotsMenu, Menu.NONE, getResources().getString(R.string.PopupMenu)).setIcon(R.drawable.ic_dots_menu).setShowAsAction(1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == github) {
            Browser browser = new Browser();
            browser.setToolbarColor(0xFF4285f4);
            browser.setShareIcon(R.drawable.abc_ic_menu_share_mtrl_alpha);
            browser.setShareIconHiddenText("Share link");
            browser.openUrl(this, getResources().getString(R.string.GithubURL));
        } else if (item.getItemId() == dotsMenu) {}

        return super.onOptionsItemSelected(item);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

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

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}