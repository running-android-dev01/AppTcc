package com.example.igormoraes.appbar.controller;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.igormoraes.appbar.AppTccAplication;
import com.example.igormoraes.appbar.model.Estabelecimento;
import com.example.igormoraes.appbar.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class ControllerEstabelecimento {
    private static String TAG = ControllerEstabelecimento.class.getName();
    private static String CAMINHO = "data/estabelecimento";

    public static final String KEY_NEW_ESTAB = TAG + ".ALTER_ESTAB";
    public static final String KEY_DELL_ESTAB = TAG + ".DELL_ESTAB";


    public static void setNewEstab(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_NEW_ESTAB, DateUtils.ConvertToStringFormat(new Date()))
                .apply();
    }

    private static void setDellEstab(Context context, String id_estab) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_DELL_ESTAB, id_estab)
                .apply();
    }

    public static String getDellEstab(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_DELL_ESTAB, "");
    }

    public static void atualizarEstabelecimento(final Context context, final DataSnapshot dataSnapshot){
        final String id = dataSnapshot.getKey();
        final HashMap hashMap = (HashMap)dataSnapshot.getValue();

        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimentoDao.callBatchTasks(() -> {
                try{
                    Estabelecimento estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

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
                return null;
            });

        }catch (Exception ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

    public static void excluirEstabelecimento(final Context context, final DataSnapshot dataSnapshot){
        final String id = dataSnapshot.getKey();
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimentoDao.callBatchTasks(() -> {
                try{
                    Estabelecimento estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (estabelecimento!=null){
                        estabelecimentoDao.delete(estabelecimento);
                    }
                }catch (SQLException ex){
                    Log.e(TAG, "ERRO = ", ex);
                }
                return null;
            });
        }catch (Exception ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        setDellEstab(context.getApplicationContext(), id);
    }


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
}
