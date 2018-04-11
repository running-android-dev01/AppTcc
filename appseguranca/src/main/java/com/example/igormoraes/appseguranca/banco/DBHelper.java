package com.example.igormoraes.appseguranca.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.igormoraes.appseguranca.Constants;
import com.example.igormoraes.appseguranca.model.Ocorrencia;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DBHelper.class.getName();

    private static final String DATABASE_NAME = Constants.NOME_BANCO;
    private static final int DATABASE_VERSION = 1;

    private Dao<Ocorrencia, Integer> estabelecimentoDao = null;

    public DBHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Ocorrencia.class);
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
                        TableUtils.createTable(connectionSource, Ocorrencia.class);
                    } catch (Exception e) {
                        Log.e(TAG, "ERRO", e);
                    }
                    break;
            }
            i++;
        }


    }



    public Dao<Ocorrencia, Integer> getEstabelecimentoDao() throws SQLException {
        if (estabelecimentoDao == null) {
            estabelecimentoDao = getDao(Ocorrencia.class);
        }
        return estabelecimentoDao;
    }

    @Override
    public void close() {
        super.close();

        estabelecimentoDao = null;
    }

}
