package com.example.igor.apptcc.controller;

import com.example.igor.apptcc.model.ProdutoAvaliacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ControllerProdutoAvaliacao {
    private static String TAG = ControllerProdutoAvaliacao.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public static void incluir(String id_estabelecimento, String id_produto, ProdutoAvaliacao produtoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos/" + id_produto + "/avaliacoes");

        DatabaseReference push = scoresRef.push();
        produtoAvaliacao.id = push.getKey();
        produtoAvaliacao.id_produto = id_produto;

        HashMap<String, Object> postValues = new HashMap<>();

        postValues.put("nome", produtoAvaliacao.nome);
        postValues.put("uid", produtoAvaliacao.uid);
        postValues.put("data", produtoAvaliacao.data);
        postValues.put("avaliacao", produtoAvaliacao.avaliacao);
        postValues.put("descricao", produtoAvaliacao.descricao);

        push.setValue(postValues);
    }


    public static void atualizar(String id_estabelecimento, String id_produto, ProdutoAvaliacao produtoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos/" + id_produto + "/avaliacoes/" + produtoAvaliacao.id);

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", produtoAvaliacao.nome);
        postValues.put("uid", produtoAvaliacao.uid);
        postValues.put("data", produtoAvaliacao.data);
        postValues.put("avaliacao", produtoAvaliacao.avaliacao);
        postValues.put("descricao", produtoAvaliacao.descricao);

        scoresRef.setValue(postValues);
    }

    public static void excluir(String id_estabelecimento, String id_produto, ProdutoAvaliacao produtoAvaliacao){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos/" + id_produto + "/avaliacoes/" + produtoAvaliacao.id);
        scoresRef.removeValue();
    }
}
