package com.example.igor.apptcc.controller;


import android.util.Log;

import com.example.igor.apptcc.model.DrinkModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class DrinkController {
    public static void saveDrink(String keyEstab, String name, String price){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("date/drink/" + keyEstab);

        DatabaseReference drinkPush = ref.push();

        DrinkModel drinkModel = new DrinkModel(name, price, keyEstab);

        Map<String, Object> result = drinkModel.toMap();

        drinkPush.setValue(result);
    }

    public static void alterDrink(String keyEstab, String keyDrink, String name, String price){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("date/drink/" + keyEstab + "/" + keyDrink);

        DrinkModel drinkModel = new DrinkModel(name, price, keyEstab);

        Map<String, Object> result = drinkModel.toMap();

        ref.setValue(result);
    }

    public static void removeDrink(String keyEstab, String keyDrink){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("date/drink/" + keyEstab + "/" + keyDrink);

        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(DrinkController.class.getName(), "could not establish onDisconnect event:" + databaseError.getMessage());
                }
            }
        });
    }
}
