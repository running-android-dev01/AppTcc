package com.example.igor.apptcc.model;

import com.j256.ormlite.field.DatabaseField;

public class Estabelecimento {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String nome;

    @DatabaseField
    public String endereco;

    @DatabaseField
    public String referencia;

    @DatabaseField
    public double latitude;

    @DatabaseField
    public double longitude;

    @DatabaseField
    public long avaliacao;
}
