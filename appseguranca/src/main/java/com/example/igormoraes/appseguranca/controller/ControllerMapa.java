package com.example.igormoraes.appseguranca.controller;


import android.content.Context;
import android.util.Log;

import com.example.igormoraes.appseguranca.AppTccAplication;
import com.example.igormoraes.appseguranca.model.Estabelecimento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerMapa {
    private static String TAG = ControllerMapa.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public void atualizarMapa(final IControllerMapa iControllerMapa, final Context context){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO);
        scoresRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(context, dataSnapshot, iControllerMapa);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(context, dataSnapshot, iControllerMapa);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                excluir(context, dataSnapshot, iControllerMapa);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(context, dataSnapshot, iControllerMapa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }

    private void atualizar(final Context context, DataSnapshot dataSnapshot, IControllerMapa iControllerMapa){
        String id = dataSnapshot.getKey();
        HashMap hashMap = (HashMap)dataSnapshot.getValue();
        Estabelecimento estabelecimento = new Estabelecimento();
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (estabelecimento==null){
                estabelecimento = new Estabelecimento();
                estabelecimento.id = id;
            }

            estabelecimento.nome = (String)hashMap.get("nome");
            estabelecimento.endereco = (String)hashMap.get("endereco");
            estabelecimento.referencia = (String)hashMap.get("referencia");
            estabelecimento.latitude = (Double)hashMap.get("latitude");
            estabelecimento.longitude = (Double)hashMap.get("longitude");
            estabelecimento.avaliacao = (long)hashMap.get("avaliacao");

            estabelecimentoDao.createOrUpdate(estabelecimento);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        iControllerMapa.atualizarMapa(estabelecimento);

    }

    private void excluir(final Context context, DataSnapshot dataSnapshot, IControllerMapa iControllerMapa){
        String id = dataSnapshot.getKey();
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            Estabelecimento estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (estabelecimento!=null){
                estabelecimentoDao.delete(estabelecimento);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        iControllerMapa.excluirMapa(id);
    }
}
