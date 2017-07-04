package com.example.igor.apptcc.controller;


import android.content.Context;

import com.example.igor.apptcc.model.EstabModel;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewEstabController {

    public static void saveEstab(DatabaseReference ref, Context context, String name, double latitude, double longitude, String address, String locality, String postalCode, String countryCode, String countryName){
        GeoFire geoFire = new GeoFire(ref);

        String guid = UUID.randomUUID().toString();

        EstabModel estabModel = new EstabModel(name, latitude, longitude, address, locality, postalCode, countryCode, countryName);

        Map<String, Object> postValues = estabModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/date/establishment/"+ guid, postValues);

        ref.updateChildren(childUpdates);

        geoFire.setLocation("GeoFire/"+ guid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
    }
}
