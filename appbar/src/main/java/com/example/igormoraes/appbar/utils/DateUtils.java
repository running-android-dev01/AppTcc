package com.example.igormoraes.appbar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by igormoraes on 17/03/18.
 */

public class DateUtils {
    private static Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
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


    public static String ConvertToString(String date){
        return android.text.format.DateFormat.format("dd/MM/yyyy", ConvertToDate(date)).toString();
    }

}
