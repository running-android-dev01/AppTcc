package com.example.igormoraes.appseguranca.model;


import com.j256.ormlite.field.DatabaseField;

public class Produto {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String id_estabelecimento;

    @DatabaseField
    public String nome;

    @DatabaseField
    public float preco;

    @DatabaseField
    public String descricao;

    @DatabaseField
    public long avaliacao;


    @DatabaseField
    public String estabelecimento_nome;

    @DatabaseField
    public String estabelecimento_endereco;

    @DatabaseField
    public String estabelecimento_referencia;

    @DatabaseField
    public double estabelecimento_latitude;

    @DatabaseField
    public double estabelecimento_longitude;
}
