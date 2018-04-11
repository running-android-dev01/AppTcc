package com.example.igormoraes.appseguranca.model;

import com.j256.ormlite.field.DatabaseField;

public class Ocorrencia {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String ocorrencia;

    @DatabaseField
    public String endereco;

    @DatabaseField
    public double latitude;

    @DatabaseField
    public double longitude;
}
