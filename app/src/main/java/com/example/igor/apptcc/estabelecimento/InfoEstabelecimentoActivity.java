package com.example.igor.apptcc.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.R;
import com.example.igor.apptcc.model.Estabelecimento;
import com.example.igor.apptcc.model.EstabelecimentoAvaliacao;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class InfoEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = InfoEstabelecimentoActivity.class.getName();

    private static final String PACKAGE_NAME = InfoEstabelecimentoActivity.class.getName();
    public static final String PARAM_ID = PACKAGE_NAME + ".ID";

    private String id_estabelecimento;
    private Estabelecimento estabelecimento;


    //private ImageView imgEstabelecimentoFoto;
    private TextView txtAvaliacaoEstabelecimento;
    private TextView txtNomeEstabelecimento;
    private TextView txtEnderecoEstabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID, "");

        txtAvaliacaoEstabelecimento = findViewById(R.id.txtAvaliacaoEstabelecimento);
        txtNomeEstabelecimento = findViewById(R.id.txtNomeEstabelecimento);
        txtEnderecoEstabelecimento = findViewById(R.id.txtEnderecoEstabelecimento);

        ImageButton imbEditar = findViewById(R.id.imbEditar);
        imbEditar.setOnClickListener(clickEditar);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private View.OnClickListener clickEditar = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(InfoEstabelecimentoActivity.this, EditarEstabelecimentoActivity.class);
            i.putExtra(EditarEstabelecimentoActivity.PARAM_ID, estabelecimento.id);
            i.putExtra(EditarEstabelecimentoActivity.PARAM_LATITUDE, estabelecimento.latitude);
            i.putExtra(EditarEstabelecimentoActivity.PARAM_LONGITUDE, estabelecimento.longitude);

            startActivity(i);
        }
    };

    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();

            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();

            txtNomeEstabelecimento.setText(estabelecimento.nome);
            txtEnderecoEstabelecimento.setText(estabelecimento.endereco);
            txtAvaliacaoEstabelecimento.setText(Long.toString(estabelecimento.avaliacao));

            List<EstabelecimentoAvaliacao> lEstabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().limit((long)5).orderBy("data", false).where().eq("id_estabelecimento", id_estabelecimento).query();
            if (lEstabelecimentoAvaliacao.size()==0){
                txtAvaliacaoEstabelecimento.setText("N/A");
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }
}
