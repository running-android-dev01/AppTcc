package com.example.igor.apptcc.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.igor.apptcc.Constants;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DBHelper.class.getName();

    private static final String DATABASE_NAME = Constants.NOME_BANCO;
    private static final int DATABASE_VERSION = 1;
    private Dao<Estabelecimento, Integer> estabelecimentoDao = null;

    public DBHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Estabelecimento.class);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Cant create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int i = oldVersion + 1;
        while (i <= newVersion) {
            switch (i) {
                case 1:
                    try {
                        TableUtils.createTable(connectionSource, Estabelecimento.class);
                    } catch (Exception e) {
                        Log.e(TAG, "ERRO", e);
                    }
                    break;
            }
            i++;
        }


    }

    /**
     * Returns the Database Access Object (DAO) for our Representante class. It will create it or just give the cached
     * value.
     */
    public Dao<Estabelecimento, Integer> getEstabelecimentoDao() throws SQLException {
        if (estabelecimentoDao == null) {
            estabelecimentoDao = getDao(Estabelecimento.class);
        }
        return estabelecimentoDao;
    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();

        estabelecimentoDao = null;
    }

}
