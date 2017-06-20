package com.example.igor.apptcc.utils;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import com.example.igor.apptcc.R;

import java.text.DateFormat;
import java.util.Date;

public class LocationUpdatesUtils {
    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }
}
