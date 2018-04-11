package com.example.igormoraes.appbar.controller;

import android.content.Context;
import android.util.Log;

import com.example.igormoraes.appbar.AppTccAplication;
import com.example.igormoraes.appbar.model.Estabelecimento;
import com.example.igormoraes.appbar.model.Produto;
import com.example.igormoraes.appbar.model.ProdutoAvaliacao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerProduto {
    private static String TAG = ControllerEstabelecimento.class.getName();
    private static String CAMINHO_ESTAB = "data/estabelecimentoConteudo/%s/produtos/";
    private static String CAMINHO = "";

    public static void incluir(Estabelecimento estabelecimento, Produto produto){
        CAMINHO = String.format(CAMINHO_ESTAB, estabelecimento.id);

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO);

        DatabaseReference produtoPush = scoresRef.push();
        produto.id = produtoPush.getKey();

        HashMap<String, Object> postValues = new HashMap<>();

        postValues.put("nome", produto.nome);
        postValues.put("preco", produto.preco);
        postValues.put("descricao", produto.descricao);
        postValues.put("avaliacao", produto.avaliacao);
        postValues.put("estabelecimento_nome", estabelecimento.nome);
        postValues.put("estabelecimento_endereco", estabelecimento.endereco);
        postValues.put("estabelecimento_referencia", estabelecimento.referencia);
        postValues.put("estabelecimento_latitude", estabelecimento.latitude);
        postValues.put("estabelecimento_longitude", estabelecimento.longitude);

        produtoPush.setValue(postValues);
    }


    public static void atualizar(Estabelecimento estabelecimento, Produto produto){
        CAMINHO = String.format(CAMINHO_ESTAB, estabelecimento.id);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + produto.id);


        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("nome", produto.nome);
        postValues.put("preco", produto.preco);
        postValues.put("descricao", produto.descricao);
        postValues.put("avaliacao", produto.avaliacao);
        postValues.put("estabelecimento_nome", estabelecimento.nome);
        postValues.put("estabelecimento_endereco", estabelecimento.endereco);
        postValues.put("estabelecimento_referencia", estabelecimento.referencia);
        postValues.put("estabelecimento_latitude", estabelecimento.latitude);
        postValues.put("estabelecimento_longitude", estabelecimento.longitude);


        scoresRef.setValue(postValues);
    }

    public static void excluir(String id_estabelecimento, Produto produto){
        CAMINHO = String.format(CAMINHO_ESTAB, id_estabelecimento);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO +"/" + produto.id);
        scoresRef.removeValue();
    }

    public Produto getProduto(Context context, String key){
        Produto produto = null;
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoDao();
            produto = produtoDao.queryBuilder().where().eq("id", key).queryForFirst();
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
        return produto;
    }


    public void atualizarAvaliacoes(final Context context, String id_estabelecimento, String id_produto){
        String CAMINHO_AVALIACAO = String.format("data/produtoAvaliacoes/%s/%s/", id_estabelecimento, id_produto);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO_AVALIACAO);
        scoresRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarAvaliacao(context, dataSnapshot, id_estabelecimento, id_produto);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarAvaliacao(context, dataSnapshot, id_estabelecimento, id_produto);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                excluirAvaliacao(context, dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarAvaliacao(context, dataSnapshot, id_estabelecimento, id_produto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }


    private void atualizarAvaliacao(final Context context, DataSnapshot dataSnapshot, String id_estabelecimento, String id_produto){
        String id = dataSnapshot.getKey();
        HashMap hashMap = (HashMap)dataSnapshot.getValue();
        ProdutoAvaliacao produtoAvaliacao;
        try{
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoAvaliacaoDao();
            produtoAvaliacao = produtoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

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
    }

    private void excluirAvaliacao(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        try{
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoAvaliacaoDao();
            ProdutoAvaliacao produtoAvaliacao = produtoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (produtoAvaliacao!=null){
                produtoAvaliacaoDao.delete(produtoAvaliacao);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }


    public static void incluirAvaliacao(String id_estabelecimento, String id_produto, ProdutoAvaliacao produtoAvaliacao){
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
