package com.example.igormoraes.appseguranca.model;

import com.j256.ormlite.field.DatabaseField;

public class ProdutoAvaliacao {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String id_produto;

    @DatabaseField
    public String id_estabelecimento;

    @DatabaseField
    public String nome;

    @DatabaseField
    public String uid;

    @DatabaseField
    public String data;

    @DatabaseField
    public long avaliacao;

    @DatabaseField
    public String descricao;
}
