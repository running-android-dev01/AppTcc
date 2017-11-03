package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;


public class EstabelecimentoController {

    public static void saveEstab(DatabaseReference ref, Context context, Estabelecimento estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("data/estabelecimento");

        DatabaseReference estabPush = scoresRef.push();
        estabelecimento.id = estabPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", estabelecimento.nome);
        postValues.put("endereco", estabelecimento.endereco);
        postValues.put("complemento", estabelecimento.complemento);
        postValues.put("numero", estabelecimento.numero);
        postValues.put("bairro", estabelecimento.bairro);
        postValues.put("cidade", estabelecimento.cidade);
        postValues.put("uf", estabelecimento.uf);
        postValues.put("pais", estabelecimento.pais);
        postValues.put("enderecoCompleto", estabelecimento.enderecoCompleto);
        postValues.put("latitude", estabelecimento.latitude);
        postValues.put("longitude", estabelecimento.longitude);

        estabPush.setValue(postValues);

        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimentoDao.createOrUpdate(estabelecimento);
        }catch (SQLException ex){
            Log.e(EstabelecimentoController.class.getSimpleName(), "ERRO = ", ex);
        }



    }
}
