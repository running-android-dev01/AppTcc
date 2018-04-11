package com.example.igormoraes.appfarmacia.model;

import com.j256.ormlite.field.DatabaseField;

public class EstabelecimentoAvaliacao {
    @DatabaseField(id = true)
    public String id;

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
