package com.example.igormoraes.appseguranca.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.igormoraes.appseguranca.Constants;
import com.example.igormoraes.appseguranca.model.Estabelecimento;
import com.example.igormoraes.appseguranca.model.EstabelecimentoAvaliacao;
import com.example.igormoraes.appseguranca.model.Produto;
import com.example.igormoraes.appseguranca.model.ProdutoAvaliacao;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DBHelper.class.getName();

    private static final String DATABASE_NAME = Constants.NOME_BANCO;
    private static final int DATABASE_VERSION = 1;

    private Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = null;
    private Dao<Estabelecimento, Integer> estabelecimentoDao = null;
    private Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = null;
    private Dao<Produto, Integer> produtoDao = null;

    public DBHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, ProdutoAvaliacao.class);
            TableUtils.createTable(connectionSource, Estabelecimento.class);
            TableUtils.createTable(connectionSource, EstabelecimentoAvaliacao.class);
            TableUtils.createTable(connectionSource, Produto.class);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Cant create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int i = oldVersion + 1;
        while (i <= newVersion) {
            switch (i) {
                case 1:
                    try {
                        TableUtils.createTable(connectionSource, ProdutoAvaliacao.class);
                        TableUtils.createTable(connectionSource, Estabelecimento.class);
                        TableUtils.createTable(connectionSource, EstabelecimentoAvaliacao.class);
                        TableUtils.createTable(connectionSource, Produto.class);
                    } catch (Exception e) {
                        Log.e(TAG, "ERRO", e);
                    }
                    break;
            }
            i++;
        }


    }


    public Dao<ProdutoAvaliacao, Integer> getProdutoAvaliacaoDao() throws SQLException {
        if (produtoAvaliacaoDao == null) {
            produtoAvaliacaoDao = getDao(ProdutoAvaliacao.class);
        }
        return produtoAvaliacaoDao;
    }

    public Dao<EstabelecimentoAvaliacao, Integer> getEstabelecimentoAvaliacaoDao() throws SQLException {
        if (estabelecimentoAvaliacaoDao == null) {
            estabelecimentoAvaliacaoDao = getDao(EstabelecimentoAvaliacao.class);
        }
        return estabelecimentoAvaliacaoDao;
    }

    public Dao<Estabelecimento, Integer> getEstabelecimentoDao() throws SQLException {
        if (estabelecimentoDao == null) {
            estabelecimentoDao = getDao(Estabelecimento.class);
        }
        return estabelecimentoDao;
    }

    public Dao<Produto, Integer> getProdutoDao() throws SQLException {
        if (produtoDao == null) {
            produtoDao = getDao(Produto.class);
        }
        return produtoDao;
    }

    @Override
    public void close() {
        super.close();

        produtoAvaliacaoDao = null;
        estabelecimentoDao = null;
        estabelecimentoAvaliacaoDao = null;
        produtoDao = null;
    }

}
