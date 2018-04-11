package com.example.igormoraes.appseguranca.controller;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.igormoraes.appseguranca.AppTccAplication;
import com.example.igormoraes.appseguranca.model.Ocorrencia;
import com.example.igormoraes.appseguranca.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class ControllerEstabelecimento {
    private static String TAG = ControllerEstabelecimento.class.getName();
    private static String CAMINHO = "data/ocorrencia";

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
            Dao<Ocorrencia, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimentoDao.callBatchTasks(() -> {
                try{
                    Ocorrencia estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (estabelecimento==null){
                        estabelecimento = new Ocorrencia();
                        estabelecimento.id = id;
                    }

                    estabelecimento.ocorrencia = (String)hashMap.get("ocorrencia");
                    estabelecimento.endereco = (String)hashMap.get("endereco");
                    estabelecimento.latitude = (Double)hashMap.get("latitude");
                    estabelecimento.longitude = (Double)hashMap.get("longitude");

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
            Dao<Ocorrencia, Integer> estabelecimentoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimentoDao.callBatchTasks(() -> {
                try{
                    Ocorrencia estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id).queryForFirst();

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


    public static void incluir(Ocorrencia estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO);

        DatabaseReference estabPush = scoresRef.push();
        estabelecimento.id = estabPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("ocorrencia", estabelecimento.ocorrencia);
        postValues.put("endereco", estabelecimento.endereco);
        postValues.put("latitude", estabelecimento.latitude);
        postValues.put("longitude", estabelecimento.longitude);

        estabPush.setValue(postValues);
    }


    public static void atualizar(Ocorrencia estabelecimento){
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + estabelecimento.id);


        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("ocorrencia", estabelecimento.ocorrencia);
        postValues.put("endereco", estabelecimento.endereco);
        postValues.put("latitude", estabelecimento.latitude);
        postValues.put("longitude", estabelecimento.longitude);

        scoresRef.setValue(postValues);
    }
}
