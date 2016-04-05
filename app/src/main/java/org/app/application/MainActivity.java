package org.app.application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.app.material.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RelativeLayout layout = new RelativeLayout(this);
        //layout.setBackgroundColor(0xffffffff);
//
        //button1 = new Button(this);
        //button1.setText(getResources().getString(R.string.NumberPicker));
        //button1.setOnClickListener(this);
        //layout.addView(button1, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
        //button2 = new Button(this);
        //button2.setText("Button");
        //button2.setOnClickListener(this);
        //layout.addView(button2, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
        //CheckBox checkBox = new CheckBox(this);
        //checkBox.setChecked(true);
        //layout.addView(checkBox, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
        //SwitchButton android = new SwitchButton(this);
        //android.setChecked(true);
//
        //layout.addView(android, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 100, 100, 0, 0));
//
        ////layout.addView(view, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 100, 200, 0, 0));
//
        //setContentView(layout);

        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    //private void setupViewPager(ViewPager viewPager) {
    //    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    //    adapter.addFragment(new OneFragment(), "ONE");
    //    adapter.addFragment(new TwoFragment(), "TWO");
    //    adapter.addFragment(new ThreeFragment(), "THREE");
    //    viewPager.setAdapter(adapter);
    //}

    //class ViewPagerAdapter extends FragmentPagerAdapter {
    //    private final List<Fragment> mFragmentList = new ArrayList<>();
    //    private final List<String> mFragmentTitleList = new ArrayList<>();
//
    //    public ViewPagerAdapter(FragmentManager manager) {
    //        super(manager);
    //    }
//
    //    @Override
    //    public Fragment getItem(int position) {
    //        return mFragmentList.get(position);
    //    }
//
    //    @Override
    //    public int getCount() {
    //        return mFragmentList.size();
    //    }
//
    //    public void addFragment(Fragment fragment, String title) {
    //        mFragmentList.add(fragment);
    //        mFragmentTitleList.add(title);
    //    }
//
    //    @Override
    //    public CharSequence getPageTitle(int position) {
    //        return mFragmentTitleList.get(position);
    //    }
    //}

    @Override
    public void onClick(View view) {
        if (view == button1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fast NumberPicker");

            final NumberPicker numberPicker = new NumberPicker(this);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(10);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setSelectionDividerColor(0xff4285f4);

            builder.setView(numberPicker);
            builder.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Value = " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xff4285f4);
        } else if (view == button2) {}
    }
}