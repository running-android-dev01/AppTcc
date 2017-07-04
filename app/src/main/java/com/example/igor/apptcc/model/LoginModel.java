package com.example.igor.apptcc.model;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class LoginModel {
    public String name;
    public String email;
    public String password;

    public LoginModel(){

    }

    public LoginModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name);
        result.put("email", this.email);
        result.put("password", password);

        return result;
    }
}
