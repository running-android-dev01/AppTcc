package com.example.igor.apptcc.modelDb;


import com.j256.ormlite.field.DatabaseField;

public class AvaliacaoEstabelecimento {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String nome;

    @DatabaseField
    public String Uid;

    @DatabaseField
    public String data;

    @DatabaseField
    public String avaliacao;

    @DatabaseField
    public int nota;
}
