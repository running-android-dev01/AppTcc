package com.example.igor.apptcc.model;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class DrinkModel implements Serializable {

    public String name;
    public String price;
    private String keyEstab;

    public DrinkModel() {
    }

    public DrinkModel(String name, String price, String keyEstab) {
        this.name = name.toUpperCase();
        this.price = price;
        this.keyEstab = keyEstab;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name.toUpperCase());
        result.put("price", this.price);
        result.put("keyEstab", this.keyEstab);

        return result;
    }

    public void toDrink(DataSnapshot dataSnapshot) {
        HashMap<String, Object> map =  (HashMap<String, Object>)dataSnapshot.getValue();

        this.name = map.get("name")==null?"":map.get("name").toString();
        this.price = map.get("price")==null?"0.0":map.get("price").toString();
        this.keyEstab = map.get("keyEstab")==null?"":map.get("keyEstab").toString();
    }
}
