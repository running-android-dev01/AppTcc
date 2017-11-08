package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.model.Estabelecimento;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerEstabelecimento {
    private static String TAG = ControllerEstabelecimento.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public static void incluir(Estabelecimento estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO);

        DatabaseReference estabPush = scoresRef.push();
        estabelecimento.id = estabPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", estabelecimento.nome);
        postValues.put("endereco", estabelecimento.endereco);
        postValues.put("referencia", estabelecimento.referencia);
        postValues.put("latitude", estabelecimento.latitude);
        postValues.put("longitude", estabelecimento.longitude);
        postValues.put("avaliacao", estabelecimento.avaliacao);

        estabPush.setValue(postValues);
    }


    public static void atualizar(Estabelecimento estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + estabelecimento.id);


        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", estabelecimento.nome);
        postValues.put("endereco", estabelecimento.endereco);
        postValues.put("referencia", estabelecimento.referencia);
        postValues.put("latitude", estabelecimento.latitude);
        postValues.put("longitude", estabelecimento.longitude);
        postValues.put("avaliacao", estabelecimento.avaliacao);

        scoresRef.setValue(postValues);
    }

    public static void excluir(Estabelecimento estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + estabelecimento.id);
        scoresRef.removeValue();
    }

    public Estabelecimento getEstabelecimento(Context context, String key){
        Estabelecimento estabelecimento = null;
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", key).queryForFirst();
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        return estabelecimento;
    }
}
