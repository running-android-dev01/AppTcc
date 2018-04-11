package com.example.igormoraes.appseguranca;

import android.app.Application;

import com.example.igormoraes.appseguranca.banco.DBHelper;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.android.apptools.OpenHelperManager;


public class AppTccAplication extends Application {
    private DBHelper dbHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public DBHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return dbHelper;
    }
}
