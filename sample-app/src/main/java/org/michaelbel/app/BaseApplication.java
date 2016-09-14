package org.michaelbel.app;

import android.app.Application;
import android.content.Context;

import org.michaelbel.material.util.Utils;

public class BaseApplication extends Application {

    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        Utils.bind(getApplicationContext());
    }
}