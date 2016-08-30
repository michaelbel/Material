package org.michaelbel.material.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "MMMM d";

    public static String getYesterdayDate() {
        return getYesterdayDate(DEFAULT_DATE_FORMAT);
    }

    public static String getYesterdayDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return formatDate(format, calendar.getTime());
    }

    public static String getTodayDate() {
        return getTodayDate(DEFAULT_DATE_FORMAT);
    }

    public static String getTodayDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        return formatDate(format, calendar.getTime());
    }

    public static String getTomorrowDate() {
        return getTomorrowDate(DEFAULT_DATE_FORMAT);
    }

    public static String getTomorrowDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return formatDate(format, calendar.getTime());
    }

    private static String formatDate(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date != null ? date : new Date());
    }
}