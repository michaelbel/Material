package org.app.material;

import org.app.material.core.AppLoader;

public class DimenUtil {

    public static float density = 1;

    static {
        density = AppLoader.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }

        return (int) Math.ceil(density * value);
    }
}