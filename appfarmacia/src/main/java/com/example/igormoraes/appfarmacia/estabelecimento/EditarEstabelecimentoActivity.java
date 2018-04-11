package com.example.igormoraes.appfarmacia.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.igormoraes.appfarmacia.AppTccAplication;
import com.example.igormoraes.appfarmacia.R;
import com.example.igormoraes.appfarmacia.controller.ControllerEstabelecimento;
import com.example.igormoraes.appfarmacia.model.Estabelecimento;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class EditarEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = EditarEstabelecimentoActivity.class.getName();
    private static final int CT_ENDERECO = 1;

    private static final String PACKAGE_NAME = EditarEstabelecimentoActivity.class.getName();
    public static final String PARAM_ID = PACKAGE_NAME + ".ID";
    public static final String PARAM_LATITUDE = PACKAGE_NAME + ".LATITUDE";
    public static final String PARAM_LONGITUDE = PACKAGE_NAME + ".LONGITUDE";

    private String id_estabelecimento;
    private double latitude;
    private double longitude;

    private EditText edtNome;
    private TextView txtEnderecoAlterEstabelecimento;

    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID, "");
        latitude = getIntent().getExtras().getDouble(PARAM_LATITUDE, 0.0);
        longitude = getIntent().getExtras().getDouble(PARAM_LONGITUDE, 0.0);

        edtNome = findViewById(R.id.edtNome);
        LinearLayout lnlEnderecoAlterEstabelecimento = findViewById(R.id.lnlEnderecoAlterEstabelecimento);
        txtEnderecoAlterEstabelecimento = findViewById(R.id.txtEnderecoAlterEstabelecimento);
        Button btn_salvar = findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(clickSalvar);
        lnlEnderecoAlterEstabelecimento.setOnClickListener(clickEndereco);

        carregarDados();
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

    private View.OnClickListener clickSalvar = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            edtNome.setError(null);

            if (TextUtils.isEmpty(edtNome.getText().toString())){
                edtNome.setError(getString(R.string.informe_nome_estabelecimento));
                return;
            }


            if (TextUtils.isEmpty(txtEnderecoAlterEstabelecimento.getText().toString())){
                clickEndereco.onClick(null);
                return;
            }

            estabelecimento.nome = edtNome.getText().toString().toUpperCase();
            estabelecimento.endereco = txtEnderecoAlterEstabelecimento.getText().toString().toUpperCase();
            estabelecimento.referencia = "";
            estabelecimento.longitude = longitude;
            estabelecimento.latitude = latitude;

            if (TextUtils.isEmpty(id_estabelecimento)){
                ControllerEstabelecimento.incluir(estabelecimento);
            }else{
                ControllerEstabelecimento.atualizar(estabelecimento);
            }
            finish();
        }
    };


    private View.OnClickListener clickEndereco = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(EditarEstabelecimentoActivity.this, MapaEditarEstabelecimentoActivity.class);
            i.putExtra(MapaEditarEstabelecimentoActivity.PARAM_LATITUDE, latitude);
            i.putExtra(MapaEditarEstabelecimentoActivity.PARAM_LONGITUDE, longitude);
            startActivityForResult(i, CT_ENDERECO);
        }
    };


    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();
            if (estabelecimento==null){
                estabelecimento = new Estabelecimento();
                estabelecimento.nome = "";
                estabelecimento.endereco = "";
                estabelecimento.avaliacao = 0;
            }
            edtNome.setText(estabelecimento.nome);
            txtEnderecoAlterEstabelecimento.setText(estabelecimento.endereco);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CT_ENDERECO && resultCode==RESULT_OK){
            latitude = data.getExtras().getDouble(MapaEditarEstabelecimentoActivity.PARAM_LATITUDE, 0);
            longitude = data.getExtras().getDouble(MapaEditarEstabelecimentoActivity.PARAM_LONGITUDE, 0);

            estabelecimento.latitude = latitude;
            estabelecimento.longitude = longitude;
            estabelecimento.endereco = data.getExtras().getString(MapaEditarEstabelecimentoActivity.PARAM_ENDERECO, "");

            txtEnderecoAlterEstabelecimento.setText(estabelecimento.endereco);
        }
    }
}
