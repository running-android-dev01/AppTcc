package com.example.igor.apptcc.model;

import com.j256.ormlite.field.DatabaseField;

public class EstabelecimentoAvaliacao {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String id_estabelecimento;

    @DatabaseField
    public String nome;

    @DatabaseField
    public double uid;

    @DatabaseField
    public String data;

    @DatabaseField
    public int avaliacao;

    @DatabaseField
    public String descricao;
}
