package org.michaelbel;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.michaelbel.app.R;

public class LaunchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}