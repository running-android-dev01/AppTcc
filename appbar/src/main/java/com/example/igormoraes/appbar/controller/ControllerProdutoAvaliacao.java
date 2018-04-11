package com.example.igormoraes.appbar.controller;

import android.content.Context;
import android.util.Log;

import com.example.igormoraes.appbar.AppTccAplication;
import com.example.igormoraes.appbar.model.ProdutoAvaliacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerProdutoAvaliacao {
    private final static String TAG = ControllerProdutoAvaliacao.class.getName();

    public static void atualizarAvaliacao(final Context context, final DataSnapshot dataSnapshot, final String id_estabelecimento, final String id_produto){
        final String id = dataSnapshot.getKey();
        final HashMap hashMap = (HashMap)dataSnapshot.getValue();
        try{
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoAvaliacaoDao();
            produtoAvaliacaoDao.callBatchTasks(() -> {
                try{

                    ProdutoAvaliacao produtoAvaliacao = produtoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (produtoAvaliacao==null){
                        produtoAvaliacao = new ProdutoAvaliacao();
                        produtoAvaliacao.id = id;
                        produtoAvaliacao.id_produto = id_produto;
                        produtoAvaliacao.id_estabelecimento = id_estabelecimento;
                    }

                    produtoAvaliacao.nome = (String)hashMap.get("nome");
                    produtoAvaliacao.uid = (String)hashMap.get("uid");
                    produtoAvaliacao.data = (String)hashMap.get("data");
                    produtoAvaliacao.avaliacao = (long)hashMap.get("avaliacao");
                    produtoAvaliacao.descricao = (String)hashMap.get("descricao");

                    produtoAvaliacaoDao.createOrUpdate(produtoAvaliacao);
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
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoAvaliacaoDao();
            produtoAvaliacaoDao.callBatchTasks(() -> {
                try{
                    ProdutoAvaliacao produtoAvaliacao = produtoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (produtoAvaliacao!=null){
                        produtoAvaliacaoDao.delete(produtoAvaliacao);
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


    public static void incluir(String id_estabelecimento, String id_produto, ProdutoAvaliacao produtoAvaliacao){
        String CAMINHO_AVALIACAO = String.format("data/produtoAvaliacoes/%s/%s/", id_estabelecimento, id_produto);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO_AVALIACAO);

        DatabaseReference produtoAvaliacaoPush = scoresRef.push();
        produtoAvaliacao.id = produtoAvaliacaoPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();

        postValues.put("nome", produtoAvaliacao.nome);
        postValues.put("uid", produtoAvaliacao.uid);
        postValues.put("data", produtoAvaliacao.data);
        postValues.put("avaliacao", produtoAvaliacao.avaliacao);
        postValues.put("descricao", produtoAvaliacao.descricao);

        produtoAvaliacaoPush.setValue(postValues);
    }
}
