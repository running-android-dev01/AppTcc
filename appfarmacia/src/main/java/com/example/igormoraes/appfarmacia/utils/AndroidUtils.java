package com.example.igormoraes.appfarmacia.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

public class AndroidUtils {

    public static double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
    }

    public static String formatDistanciaFormat(double distance) {
        String unit = "m";
        if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }


    public static float convertStringToFloat(String str){
        Float valor = Float.valueOf(0);
        try
        {
            valor = Float.valueOf(str);
        }
        catch(NumberFormatException ex)
        {
            DecimalFormat df = new DecimalFormat();
            Number n = null;
            try
            {
                n = df.parse(str);
            }
            catch(ParseException ex2){ }
            if(n != null)
                valor = n.floatValue();
        }
        return valor;
    }

}
