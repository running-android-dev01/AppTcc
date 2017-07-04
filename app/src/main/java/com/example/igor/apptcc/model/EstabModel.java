package com.example.igor.apptcc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by igormoraes on 03/07/17.
 */

public class EstabModel {
    public String name;
    public double latitude;
    public double longitude;
    public String address;
    public String locality;
    public String postalCode;
    public String countryCode;
    public String countryName;


    public EstabModel(){

    }

    public EstabModel(String name, double latitude, double longitude, String address, String locality, String postalCode, String countryCode, String countryName) {
        this.name = name.toUpperCase();
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.locality = locality;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name);
        result.put("latitude", this.latitude);
        result.put("longitude", this.longitude);
        result.put("address", this.address);
        result.put("locality", this.locality);
        result.put("postalCode", this.postalCode);
        result.put("countryCode", this.countryCode);
        result.put("countryName", this.countryName);

        return result;
    }
}
