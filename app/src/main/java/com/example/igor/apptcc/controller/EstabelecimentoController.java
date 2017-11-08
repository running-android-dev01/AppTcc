package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class EstabelecimentoController {
    private static String TAG = EstabelecimentoController.class.getName();

    public static void novoEstab(Estabelecimento estabelecimento){
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
    }


    public static void atualizarEstab(Estabelecimento estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("data/estabelecimento/" + estabelecimento.id);


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

        scoresRef.setValue(postValues);
    }

    Dao<Estabelecimento, Integer> getEstabelecimentoDao(Context ctx) throws SQLException{
        Dao<Estabelecimento, Integer> estabelecimentoDao = null;
        /*try{
            estabelecimentoDao = ((AppTccAplication)ctx.getApplicationContext()).getHelper().getEstabelecimentoDao();
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }*/
        return estabelecimentoDao;
    }


    public Estabelecimento carregarEstabelecimento(String id, Context ctx){
        Estabelecimento estabelecimento = null;
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = getEstabelecimentoDao(ctx);
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        return estabelecimento;
    }

    public void atualizarEstab(final IEstabelecimentoController iEstabelecimentoController, final Context ctx){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("data/estabelecimento");
        scoresRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarEstabelecimento(dataSnapshot, iEstabelecimentoController, ctx);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarEstabelecimento(dataSnapshot, iEstabelecimentoController, ctx);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                excluirEstabelecimento(dataSnapshot, iEstabelecimentoController, ctx);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarEstabelecimento(dataSnapshot, iEstabelecimentoController, ctx);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }

    private void atualizarEstabelecimento(DataSnapshot dataSnapshot, IEstabelecimentoController iEstabelecimentoController, final Context ctx){
        String id = dataSnapshot.getKey();
        Estabelecimento estabelecimento = null;
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = getEstabelecimentoDao(ctx);
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();
            if (estabelecimento==null){
                estabelecimento = new Estabelecimento();
                estabelecimento.id = id;
            }

            HashMap hashMap = (HashMap)dataSnapshot.getValue();

            estabelecimento.nome = (String)hashMap.get("nome");
            estabelecimento.endereco = (String)hashMap.get("endereco");
            estabelecimento.complemento = (String)hashMap.get("complemento");
            estabelecimento.numero = (String)hashMap.get("numero");
            estabelecimento.bairro = (String)hashMap.get("bairro");
            estabelecimento.cidade = (String)hashMap.get("cidade");
            estabelecimento.uf = (String)hashMap.get("uf");
            estabelecimento.pais = (String)hashMap.get("pais");
            estabelecimento.enderecoCompleto = (String)hashMap.get("enderecoCompleto");
            estabelecimento.latitude = (Double) hashMap.get("latitude");
            estabelecimento.longitude = (Double) hashMap.get("longitude");

            estabelecimentoDao.createOrUpdate(estabelecimento);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        iEstabelecimentoController.atualizarEstabelecimento(estabelecimento);
    }
    private void excluirEstabelecimento(DataSnapshot dataSnapshot, IEstabelecimentoController iEstabelecimentoController, final Context ctx){
        String id = dataSnapshot.getKey();
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = getEstabelecimentoDao(ctx);
            Estabelecimento estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();
            if (estabelecimento!=null){
                estabelecimentoDao.delete(estabelecimento);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        iEstabelecimentoController.excluirEstabelecimento(id);
    }
}
