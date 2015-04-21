package com.codepath.shareyourtable.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsamant on 4/1/15.
 */
public class DateHelper {

    public static Date getDateFromString(String eDate) {

        DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("DATEEEEEE " + date);
        return date;
    }

    public static String getDayOfWeek(Date date){
        if(date != null){
            return new SimpleDateFormat("EE").format(date);
        }
        return "";
    }

    public static String getFormattedDate(Date date){
        if(date !=null){
            return new SimpleDateFormat("MMM").format(date) + ", " + new SimpleDateFormat("dd").format(date);
        }

        return "";
    }
}
