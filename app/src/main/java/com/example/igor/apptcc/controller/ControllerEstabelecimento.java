package com.example.igor.apptcc.controller;

import android.content.Context;
import android.util.Log;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.model.Estabelecimento;
import com.example.igor.apptcc.model.EstabelecimentoAvaliacao;
import com.example.igor.apptcc.model.Produto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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


    public void atualizarInfomacoes(final Context context, String id_estabelecimento){
        String CAMINHO_PRODUTO = String.format("data/estabelecimentoConteudo/%s/", id_estabelecimento);

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO_PRODUTO);
        scoresRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarInformacao(context, dataSnapshot, id_estabelecimento);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarInformacao(context, dataSnapshot, id_estabelecimento);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                excluirInformacao(context, dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizarInformacao(context, dataSnapshot, id_estabelecimento);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }


    private void atualizarInformacao(final Context context, DataSnapshot dataSnapshot, String id_estabelecimento){
        String id = dataSnapshot.getKey();
        if (id.equals("avaliacoes")){
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                atualizarAvaliacao(context, postSnapshot, id_estabelecimento);
            }
        }else{
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                atualizarProduto(context, postSnapshot, id_estabelecimento);
            }
        }
    }

    private void excluirInformacao(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        if (id.equals("avaliacoes")){
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                excluirAvaliacao(context, postSnapshot);
            }
        }else{
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                excluirProduto(context, postSnapshot);
            }
        }

    }

    private void atualizarProduto(final Context context, DataSnapshot dataSnapshot, String id_estabelecimento){
        String id = dataSnapshot.getKey();
        HashMap hashMap = (HashMap)dataSnapshot.getValue();
        Produto produto;
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoDao();
            produto = produtoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (produto==null){
                produto = new Produto();
                produto.id = id;
                produto.id_estabelecimento = id_estabelecimento;
            }

            float preco = 0.0f;
            Object oPreco = hashMap.get("preco");
            if (oPreco!=null){
                if (oPreco instanceof Long){
                    long lPreco = (long)oPreco;
                    preco = (float) lPreco;
                }else if (oPreco instanceof Double){
                    double dPreco = (double)oPreco;
                    preco = (float) dPreco;
                }else if (oPreco instanceof String){
                    String sPreco = (String)oPreco;
                    preco = Float.parseFloat(sPreco);
                }

            }
            produto.nome = (String)hashMap.get("nome");
            produto.preco = preco;
            produto.descricao = (String)hashMap.get("descricao");
            produto.avaliacao = (long)hashMap.get("avaliacao");

            produto.estabelecimento_nome = (String)hashMap.get("estabelecimento_nome");
            produto.estabelecimento_endereco = (String)hashMap.get("estabelecimento_endereco");
            produto.estabelecimento_referencia = (String)hashMap.get("estabelecimento_referencia");
            produto.estabelecimento_latitude = (Double)hashMap.get("estabelecimento_latitude");
            produto.estabelecimento_longitude = (Double)hashMap.get("estabelecimento_longitude");

            produtoDao.createOrUpdate(produto);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

    private void excluirProduto(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoDao();
            Produto produto = produtoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (produto!=null){
                produtoDao.delete(produto);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }


    public static void incluirAvaliacao(String id_estabelecimento, EstabelecimentoAvaliacao estabelecimentoAvaliacao){
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

    private void atualizarAvaliacao(final Context context, DataSnapshot dataSnapshot, String id_estabelecimento){
        String id = dataSnapshot.getKey();
        HashMap hashMap = (HashMap)dataSnapshot.getValue();
        EstabelecimentoAvaliacao estabelecimentoAvaliacao;
        try{
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            estabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

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
    }

    private void excluirAvaliacao(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        try{
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            EstabelecimentoAvaliacao estabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().where().eq("id", id).queryForFirst();

            if (estabelecimentoAvaliacao!=null){
                estabelecimentoAvaliacaoDao.delete(estabelecimentoAvaliacao);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

}
