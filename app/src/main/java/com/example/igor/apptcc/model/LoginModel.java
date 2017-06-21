package com.example.igor.apptcc.model;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LoginModel {
    private String name;
    private String email;
    private String password;

    public LoginModel(){

    }

    public LoginModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
