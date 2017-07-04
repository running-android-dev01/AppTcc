package com.example.igor.apptcc.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.igor.apptcc.model.LoginModel;

import java.util.HashMap;

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

    public static LoginModel getMap(Object object) {
        HashMap<String, Object> result = (HashMap<String, Object>)object;

        LoginModel loginModel = new LoginModel();

        loginModel.name = (String)result.get("name");;
        loginModel.email = (String)result.get("email");
        loginModel.password = (String)result.get("password");

        return loginModel;
    }

}
