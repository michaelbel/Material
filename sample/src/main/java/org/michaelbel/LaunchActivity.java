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
import org.michaelbel.material.widget2.FragmentsPagerAdapter;

public class LaunchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = findViewById(R.id.action_bar);
        actionBar.setNavigationIcon(R.drawable.ic_menu);
        actionBar.setTitle(R.string.MaterialDemo);

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
    }
}