package org.app.material.core;

import android.app.Application;
import android.content.Context;

public class AppLoader extends Application {

    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}