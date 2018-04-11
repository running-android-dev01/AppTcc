package com.example.igor.apptcc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadsFirebaseBreadCast extends BroadcastReceiver {
    public static final String RESTART = "com.example.igor.apptcc.DownloadsFirebase";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, DownloadsFirebaseService.class));

    }
}
