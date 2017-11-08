package com.example.igor.apptcc.controller;

import com.example.igor.apptcc.model.Produto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ControllerProduto {
    private static String TAG = ControllerProduto.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public static void incluir(String id_estabelecimento, Produto produto){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos");

        DatabaseReference push = scoresRef.push();
        produto.id = push.getKey();
        produto.id_estabelecimento = id_estabelecimento;

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", produto.nome);
        postValues.put("preco", produto.preco);
        postValues.put("descricao", produto.descricao);
        postValues.put("avaliacao", produto.avaliacao);

        push.setValue(postValues);
    }


    public static void atualizar(String id_estabelecimento, Produto produto){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos/" + produto.id);


        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", produto.nome);
        postValues.put("preco", produto.preco);
        postValues.put("descricao", produto.descricao);
        postValues.put("avaliacao", produto.avaliacao);

        scoresRef.setValue(postValues);
    }

    public static void excluir(String id_estabelecimento, Produto produto){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + id_estabelecimento + "/produtos/" + produto.id);
        scoresRef.removeValue();
    }
}
