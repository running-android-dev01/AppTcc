package com.example.igor.apptcc.controller;

import com.example.igor.apptcc.model.EstabelecimentoAvaliacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ControllerEstabelecimentoAvaliacao {
    private static String TAG = ControllerEstabelecimentoAvaliacao.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public static void incluir(String id_estabelecimento, EstabelecimentoAvaliacao estabelecimentoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/avaliacoes");

        DatabaseReference push = scoresRef.push();
        estabelecimentoAvaliacao.id = push.getKey();
        estabelecimentoAvaliacao.id_estabelecimento = id_estabelecimento;

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", estabelecimentoAvaliacao.nome);
        postValues.put("uid", estabelecimentoAvaliacao.uid);
        postValues.put("data", estabelecimentoAvaliacao.data);
        postValues.put("avaliacao", estabelecimentoAvaliacao.avaliacao);
        postValues.put("descricao", estabelecimentoAvaliacao.descricao);

        push.setValue(postValues);
    }


    public static void atualizar(String id_estabelecimento, EstabelecimentoAvaliacao estabelecimentoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/avaliacoes/" + estabelecimentoAvaliacao.id);


        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", estabelecimentoAvaliacao.nome);
        postValues.put("uid", estabelecimentoAvaliacao.uid);
        postValues.put("data", estabelecimentoAvaliacao.data);
        postValues.put("avaliacao", estabelecimentoAvaliacao.avaliacao);
        postValues.put("descricao", estabelecimentoAvaliacao.descricao);

        scoresRef.setValue(postValues);
    }

    public static void excluir(String id_estabelecimento, EstabelecimentoAvaliacao estabelecimentoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/avaliacoes/" + estabelecimentoAvaliacao.id);
        scoresRef.removeValue();
    }


}
