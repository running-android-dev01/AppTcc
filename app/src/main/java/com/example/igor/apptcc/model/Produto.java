package com.example.igor.apptcc.model;


import com.j256.ormlite.field.DatabaseField;

public class Produto {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String id_estabelecimento;

    @DatabaseField
    public String nome;

    @DatabaseField
    public double preco;

    @DatabaseField
    public String descricao;

    @DatabaseField
    public int avaliacao;
}
