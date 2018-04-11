package com.example.igormoraes.appbar.controller;

import android.content.Context;
import android.util.Log;

import com.example.igormoraes.appbar.AppTccAplication;
import com.example.igormoraes.appbar.model.Estabelecimento;
import com.example.igormoraes.appbar.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

public class ControllerProduto {
    public static void atualizarProduto(final Context context, final DataSnapshot dataSnapshot, final String id_estabelecimento){
        final String id = dataSnapshot.getKey();
        final HashMap hashMap = (HashMap)dataSnapshot.getValue();
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoDao();
            produtoDao.callBatchTasks(() -> {
                try{
                    Produto produto = produtoDao.queryBuilder().where().eq("id", id).queryForFirst();

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
                return null;
            });
        }catch (Exception ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

    public static void excluirProduto(final Context context, final DataSnapshot dataSnapshot){
        final String id = dataSnapshot.getKey();
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)context.getApplicationContext()).getHelper().getProdutoDao();
            produtoDao.callBatchTasks(() -> {
                try{
                    Produto produto = produtoDao.queryBuilder().where().eq("id", id).queryForFirst();

                    if (produto!=null){
                        produtoDao.delete(produto);
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
}
