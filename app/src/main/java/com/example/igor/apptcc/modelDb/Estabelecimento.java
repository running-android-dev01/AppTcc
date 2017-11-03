package com.example.igor.apptcc.modelDb;

import com.j256.ormlite.field.DatabaseField;

public class Estabelecimento {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String nome;

    @DatabaseField
    public String endereco;

    @DatabaseField
    public String complemento;

    @DatabaseField
    public String numero;

    @DatabaseField
    public String bairro;

    @DatabaseField
    public String cidade;

    @DatabaseField
    public String uf;

    @DatabaseField
    public String pais;

    @DatabaseField
    public String enderecoCompleto;

    @DatabaseField
    public double latitude;

    @DatabaseField
    public double longitude;
}
