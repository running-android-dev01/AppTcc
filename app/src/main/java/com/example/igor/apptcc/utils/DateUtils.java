package com.example.igor.apptcc.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by igormoraes on 17/03/18.
 */

public class DateUtils {
    private static Date ConvertToDate(String dateString, Context context){
        Locale current = context.getApplicationContext().getResources().getConfiguration().locale;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", current);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        return convertedDate;
    }

    public static String ConvertToStringFormat(Date date){
        return android.text.format.DateFormat.format("yyyyMMddhhmmss", date).toString();
    }


    public static String ConvertToString(String date, Context context){
        return android.text.format.DateFormat.format("dd/MM/yyyy", ConvertToDate(date, context)).toString();
    }

}
