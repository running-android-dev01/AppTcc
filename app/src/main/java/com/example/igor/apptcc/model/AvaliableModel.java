package com.example.igor.apptcc.model;

import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by igormoraes on 08/07/17.
 */

public class AvaliableModel {
    public String name;
    public String keyUser;
    public float avaliable;
    public String description;

    public AvaliableModel(){

    }

    public AvaliableModel(String name, String keyUser, float avaliable, String description) {
        this.name = name;
        this.keyUser = keyUser;
        this.avaliable = avaliable;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Calendar calendar = Calendar.getInstance();

        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name.toUpperCase());
        result.put("keyUser", this.keyUser);
        result.put("avaliable", this.avaliable);
        result.put("description", this.description);
        result.put("date", calendar.getTimeInMillis());

        return result;
    }

    public void toAvaliable(DataSnapshot dataSnapshot) {
        HashMap<String, Object> map =  (HashMap<String, Object>)dataSnapshot.getValue();

        this.avaliable = 0.0f;
        Object mapAvaliable = map.get("avaliable");
        if (mapAvaliable!=null){
            if (mapAvaliable instanceof Long){
                long numero = (long)mapAvaliable;
                this.avaliable = (float) numero;
            }else if (mapAvaliable instanceof Double){
                double numero = (double)mapAvaliable;
                this.avaliable = (float) numero;
            }

        }
        this.name = map.get("name")==null?"":map.get("name").toString();
        this.keyUser = map.get("keyUser")==null?"":map.get("keyUser").toString();

        this.description = map.get("description")==null?"":map.get("description").toString();
    }
}
