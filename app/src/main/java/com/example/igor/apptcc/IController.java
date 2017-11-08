package com.example.igor.apptcc;


import com.google.firebase.database.DataSnapshot;

import java.util.Map;

public interface IController {
    Map<String, Object> getMap();
    void setMap(DataSnapshot dataSnapshot);
}
