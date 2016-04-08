package org.app.application;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.app.application.Fragments.ListViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int dotsMenu = 1;
    private static final int github = 2;

    //private boolean menuOpen = false;
    //private PopupMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialogFragment(), getResources().getString(R.string.Dialogs));
        adapter.addFragment(new ListViewFragment(), getResources().getString(R.string.ListView));
        adapter.addFragment(new RecyclerFragment(), getResources().getString(R.string.RecyclerView));
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        //menu = new PopupMenu(this);
        //menu.setElevation(10);
        //menu.setVisibility(View.INVISIBLE);
        //menu.addItem(0, "Item 1");
        //menu.addItem(1, "Item 2");
        //menu.addItem(2, "Item 3");

        //CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinator);
        //if (layout != null) {
        //    layout.addView(menu, LayoutHelper.makeCoordinator(180, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.END, 0, 6, 6, 0));
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, github, Menu.NONE, "Open Github.com").setIcon(R.drawable.github_circle).setShowAsAction(1);
        menu.add(0, dotsMenu, Menu.NONE, "Popup Menu").setIcon(R.drawable.dots_vertical).setShowAsAction(1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //public void revealMenu() {
    //    menuOpen = true;
    //    int cx = menu.getRight();
    //    int cy = menu.getTop();
    //    int finalRadius = Math.max(menu.getWidth(), menu.getHeight());
    //    Animator anim = ViewAnimationUtils.createCircularReveal(menu, cx, cy, 0, finalRadius);
    //    anim.setDuration(500);
    //    menu.setVisibility(View.VISIBLE);
    //    anim.start();
    //}

    //public void hideMenu() {
    //    menuOpen = false;
    //    int cx = menu.getRight();
    //    int cy = menu.getTop();
    //    int initialRadius = menu.getWidth();
    //    Animator anim = ViewAnimationUtils.createCircularReveal(menu, cx, cy, initialRadius, 0);
    //    anim.addListener(new AnimatorListenerAdapter() {
    //        @Override
    //        public void onAnimationEnd(Animator animation) {
    //            super.onAnimationEnd(animation);
    //            menu.setVisibility(View.INVISIBLE);
    //        }
    //    });
    //    anim.start();
    //}

    //@Override
    //public void onBackPressed() {
    //    if (menuOpen) {
    //        hideMenu();
    //    } else {
    //        super.onBackPressed();
    //    }
    //}

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