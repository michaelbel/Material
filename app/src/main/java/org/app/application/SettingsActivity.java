package org.app.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.material.AndroidUtilities;
import org.app.material.widget.ActionBar;

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout layout = new FrameLayout(this);
        layout.setBackgroundColor(0xFFF0F0F0);

        ActionBar actionBar = new ActionBar(this)
                .setBackGroundColor(AndroidUtilities.getContextColor(R.attr.colorPrimary))
                .setBackButtonImage(R.drawable.ic_arrow_back)
                .setTitle(R.string.Settings)
                .setOccupyStatusBar(false)
                .setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                    @Override
                    public void onItemClick(int id) {
                        if (id == -1) {
                            Toast.makeText(SettingsActivity.this, "Return to prev activity", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        layout.addView(actionBar);

        setContentView(layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}