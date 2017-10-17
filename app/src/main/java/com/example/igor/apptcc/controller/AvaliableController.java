package com.example.igor.apptcc.controller;

import com.example.igor.apptcc.model.AvaliableModel;
import com.example.igor.apptcc.model.DrinkModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AvaliableController {
    private FirebaseAuth mAuth;

    public static void saveAvaliable(String keyEstab, float avaliable, String description){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String name = currentUser.getDisplayName();
        String keyUser = currentUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("date/establishment/avaliable/" + keyEstab);

        DatabaseReference avaliablePush = ref.push();

        AvaliableModel avaliableModel = new AvaliableModel(name, keyUser, avaliable, description);
        Map<String, Object> result = avaliableModel.toMap();

        avaliablePush.setValue(result);
    }
}
