package org.app.material.utils;

import android.content.Context;
import android.util.Log;

import java.util.Hashtable;

public class Typeface {

    private static final String TAG = Typeface.class.getSimpleName();
    private static final Hashtable<String, android.graphics.Typeface> typefaceCache = new Hashtable<>();

    public static android.graphics.Typeface getTypeface(Context context, String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    android.graphics.Typeface t = android.graphics.Typeface.createFromAsset(context.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
            }

            return typefaceCache.get(assetPath);
        }
    }
}