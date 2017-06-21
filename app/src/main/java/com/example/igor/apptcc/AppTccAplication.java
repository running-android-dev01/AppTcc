package com.example.igor.apptcc;

import android.app.Application;

import com.google.firebase.FirebaseApp;


public class AppTccAplication extends Application {
    FirebaseApp firebaseApp;
    @Override
    public void onCreate() {
        super.onCreate();
        //firebaseApp = FirebaseApp.initializeApp(this);
    }

    public FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
}
