package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.model.LoginModel;
import com.example.igor.apptcc.utils.LoginUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private static String TAG = LoginController.class.getName();

    public static void saveUser(DatabaseReference mDatabase, String userId, LoginModel loginModel){
        Map<String, Object> postValues = loginModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userId, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public static void getUser(Context context, DatabaseReference mDatabase){
        String uid = LoginUtils.getLoginUpdatesResult(context);

        Query myTopPostsQuery = mDatabase.child("users").child(uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoginModel loginModel = LoginUtils.getMap(dataSnapshot.getValue());

                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                Log.w(TAG, "loadPost:onDataChange");
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myTopPostsQuery.addValueEventListener(postListener);
    }
}
