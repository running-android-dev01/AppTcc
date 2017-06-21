package com.example.igor.apptcc.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class LoginUtils {
    public static final String KEY_LOGIN_RESULT = "login-result";

    public static void setLoginUpdatesResult(Context context, String userKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOGIN_RESULT, userKey)
                .apply();
    }

    public static String getLoginUpdatesResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LOGIN_RESULT, "");
    }


}
