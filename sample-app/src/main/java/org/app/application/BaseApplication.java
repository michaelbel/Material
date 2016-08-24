package org.app.application;

import android.app.Application;
import android.content.Context;

import org.app.material.logger.Logger;
import org.app.material.utils.AndroidUtilities;

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