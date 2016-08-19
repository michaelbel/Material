package org.app.application;

import android.app.Application;
import android.content.Context;

import org.app.material.AndroidUtilities;
import org.app.material.logger.Logger;

public class BaseApplication extends Application {

    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        AndroidUtilities.bind(getApplicationContext());
        Logger.bind(getApplicationContext());
    }
}