package org.app.material.utils;

import java.util.Locale;

public class Size {

    public static StringUtils formatSize(long size) {
        if (size < 1024) {
            return StringUtils.format(Locale.getDefault(), "%d B", size);
        } else if (size < Math.pow(1024, 2)) {
            return StringUtils.format(Locale.getDefault(), "%.1f KB", size / 1024.0F);
        } else if (size < Math.pow(1024, 3)) {
            return StringUtils.format(Locale.getDefault(), "%.1f MB", size / Math.pow(1024.0F, 2));
        } else if (size < Math.pow(1024, 4)) {
            return StringUtils.format(Locale.getDefault(), "%.1f GB", size / Math.pow(1024.0F, 3));
        } else if (size < Math.pow(1024, 5)) {
            return StringUtils.format(Locale.getDefault(), "%.1f TB", size / Math.pow(1024.0F, 4));
        } else if (size < Math.pow(1024, 6)) {
            return StringUtils.format(Locale.getDefault(), "%.1f PB", size / Math.pow(1024.0F, 5));
        } else if (size < Math.pow(1024, 7)) {
            return StringUtils.format(Locale.getDefault(), "%.1f EB", size / Math.pow(1024.0F, 6));
        } else if (size < Math.pow(1024, 8)) {
            return StringUtils.format(Locale.getDefault(), "%.1f ZB", size / Math.pow(1024.0F, 7));
        } else {
            return StringUtils.format(Locale.getDefault(), "%.1f YB", size / Math.pow(1024.0F, 8));
        }
    }
}
