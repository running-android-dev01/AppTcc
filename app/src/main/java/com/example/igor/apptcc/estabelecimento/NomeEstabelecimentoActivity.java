package com.example.igor.apptcc.estabelecimento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.R;
import com.example.igor.apptcc.banco.DBHelper;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class NomeEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = NomeEstabelecimentoActivity.class.getSimpleName();


    EditText input_nome;
    Button btn_avancar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nome_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        input_nome = findViewById(R.id.input_nome);
        btn_avancar = findViewById(R.id.btn_avancar);

        btn_avancar.setOnClickListener(clickAvancar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener clickAvancar = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (validarEstabelecimento()){
                String nomeEstabelecimento = getNomeEstabelecimento();
                Intent i = new Intent(NomeEstabelecimentoActivity.this, EnderecoEstabelecimentoActivity.class);
                i.putExtra(EnderecoEstabelecimentoActivity.PARAM_NOME_ESTABELECIMENTO, nomeEstabelecimento);
                startActivity(i);
            }
        }
    };


    private String getNomeEstabelecimento(){
        return input_nome.getText().toString().toUpperCase();
    }

    private boolean validarEstabelecimento(){
        /*try{

            if (TextUtils.isEmpty(input_nome.getText().toString())){
                input_nome.setError("Informe o nome");
                return false;
            }

            String nomeEstabelecimento = getNomeEstabelecimento();
            Dao<Estabelecimento, Integer> estabelecimentoDao = getHelper().getEstabelecimentoDao();
            List<Estabelecimento> lEstabelecimentos = estabelecimentoDao.queryBuilder().where().eq("nome", nomeEstabelecimento).query();
            if (lEstabelecimentos.size()>0){
                input_nome.setError("Nome já cadastrado! Por favor usar um ponto de referencia ou nome do bairro no nome!");
                return false;
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO =", ex);
            return false;
        }*/
        return true;
    }

    public DBHelper getHelper() {
        return ((AppTccAplication)getApplicationContext()).getHelper();
    }
}
