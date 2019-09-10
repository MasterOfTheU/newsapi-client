package com.newsapiclient.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Convenience methods for data parsing.
 */
public abstract class Converters {

    private static final String TAG = "Converters";

    /**
     * @param year  year from date picker dialog
     * @param month month year from date picker dialog
     * @param day   day year from date picker dialog
     * @return String date without time in format "dd-MM-yyyy"
     */
    public static String getDateString(int year, int month, int day) {

        StringBuilder dateSb = new StringBuilder();

        if (day < 10) {
            dateSb.append(0);
        }
        dateSb.append(day).append("-");
        if ((month + 1) < 10) {
            dateSb.append(0);
        }
        dateSb.append(month + 1).append("-");
        dateSb.append(year);
        return dateSb.toString();
    }

    public static Date getDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            Log.w(TAG, "getDateFromString: Couldn't parse the date. " + e.getMessage());
        }
        return date;
    }

    //region [Personal info]

    public static String getFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    //endregion

}
