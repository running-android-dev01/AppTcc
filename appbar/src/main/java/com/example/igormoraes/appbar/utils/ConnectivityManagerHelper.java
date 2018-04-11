package com.example.igormoraes.appbar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ConnectivityManagerHelper {
    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static NetworkInfo getActivieNetworkInfo(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager == null)
            return null;

        return manager.getActiveNetworkInfo();
    }

    /*-
     * Only use this method to retrieve error reason
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isConnectedOrConnecting(Context context) {
        NetworkInfo networkInfo = getActivieNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /*public static boolean isActiveNetworkMetered(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        return manager != null && ConnectivityManagerCompat.isActiveNetworkMetered(manager);
    }*/

    /*public static String getActiveNetworkName(Context context) {
        try {
            NetworkInfo info = getActivieNetworkInfo(context);
            if (info != null) {
                String typeName = info.getTypeName().toLowerCase();
                if (typeName.equals("wifi")) {
                    return typeName;
                } else {
                    String extraInfoName = info.getExtraInfo().toLowerCase();
                    if (!TextUtils.isEmpty(extraInfoName))
                        return extraInfoName;
                }
                return typeName;
            }
        } catch (Exception e) {
            LogUtils.error(Constantes.TAG_LOG_ERRO, e);
            return null;
        }
        return null;
    }*/
}
