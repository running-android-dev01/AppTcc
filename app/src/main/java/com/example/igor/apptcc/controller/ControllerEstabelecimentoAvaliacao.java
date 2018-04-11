package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.model.EstabelecimentoAvaliacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerEstabelecimentoAvaliacao {
    private static final String TAG = ControllerEstabelecimentoAvaliacao.class.getName();

    public static void atualizarAvaliacao(final Context context, final DataSnapshot dataSnapshot, final String id_estabelecimento){
        final String id = dataSnapshot.getKey();
        final HashMap hashMap = (HashMap)dataSnapshot.getValue();

        try{
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            estabelecimentoAvaliacaoDao.callBatchTasks(() -> {
                try{

                    EstabelecimentoAvaliacao estabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (estabelecimentoAvaliacao==null){
                        estabelecimentoAvaliacao = new EstabelecimentoAvaliacao();
                        estabelecimentoAvaliacao.id = id;
                        estabelecimentoAvaliacao.id_estabelecimento = id_estabelecimento;
                    }


                    estabelecimentoAvaliacao.nome = (String)hashMap.get("nome");
                    estabelecimentoAvaliacao.uid = (String)hashMap.get("uid");
                    estabelecimentoAvaliacao.data = (String)hashMap.get("data");
                    estabelecimentoAvaliacao.avaliacao = (long)hashMap.get("avaliacao");
                    estabelecimentoAvaliacao.descricao = (String)hashMap.get("descricao");

                    estabelecimentoAvaliacaoDao.createOrUpdate(estabelecimentoAvaliacao);
                }catch (SQLException ex){
                    Log.e(TAG, "ERRO = ", ex);
                }
                return null;
            });
        }catch (Exception ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

    public static void excluirAvaliacao(final Context context, final DataSnapshot dataSnapshot){
        final String id = dataSnapshot.getKey();
        try{
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            estabelecimentoAvaliacaoDao.callBatchTasks(() -> {
                try{
                    EstabelecimentoAvaliacao estabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (estabelecimentoAvaliacao!=null){
                        estabelecimentoAvaliacaoDao.delete(estabelecimentoAvaliacao);
                    }
                }catch (SQLException ex){
                    Log.e(TAG, "ERRO = ", ex);
                }
                return null;
            });
        }catch (Exception ex){
            Log.e(TAG, "ERRO = ", ex);
        }



    }


    public static void incluir(String id_estabelecimento, EstabelecimentoAvaliacao estabelecimentoAvaliacao){
        String CAMINHO_AVALIACAO = String.format("data/estabelecimentoConteudo/%s/avaliacoes/", id_estabelecimento);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO_AVALIACAO);

        DatabaseReference produtoAvaliacaoPush = scoresRef.push();
        estabelecimentoAvaliacao.id = produtoAvaliacaoPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();

        postValues.put("nome", estabelecimentoAvaliacao.nome);
        postValues.put("uid", estabelecimentoAvaliacao.uid);
        postValues.put("data", estabelecimentoAvaliacao.data);
        postValues.put("avaliacao", estabelecimentoAvaliacao.avaliacao);
        postValues.put("descricao", estabelecimentoAvaliacao.descricao);

        produtoAvaliacaoPush.setValue(postValues);
    }

}
