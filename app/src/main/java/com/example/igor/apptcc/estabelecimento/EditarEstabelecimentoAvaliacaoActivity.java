package com.example.igor.apptcc.estabelecimento;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.R;
import com.example.igor.apptcc.controller.ControllerEstabelecimento;
import com.example.igor.apptcc.controller.ControllerEstabelecimentoAvaliacao;
import com.example.igor.apptcc.model.Estabelecimento;
import com.example.igor.apptcc.model.EstabelecimentoAvaliacao;
import com.example.igor.apptcc.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class EditarEstabelecimentoAvaliacaoActivity extends AppCompatActivity {
    private static final String TAG = EditarEstabelecimentoAvaliacaoActivity.class.getName();

    private static final String PACKAGE_NAME = EditarEstabelecimentoAvaliacaoActivity.class.getName();
    //public static final String PARAM_ID = PACKAGE_NAME + ".ID";
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    private String id_estabelecimento;

    private TextView txtNomeEstabelecimento;
    private RatingBar erAvaliacao;
    private EditText edtDescricaoProduto;

    private Estabelecimento estabelecimento;
    private List<EstabelecimentoAvaliacao> lEstabelecimentoAvaliacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estabelecimento_avaliacao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //id_avaliacao = getIntent().getExtras().getString(PARAM_ID, "");
        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");

        txtNomeEstabelecimento = findViewById(R.id.txtNomeEstabelecimento);
        erAvaliacao = findViewById(R.id.erAvaliacao);
        edtDescricaoProduto = findViewById(R.id.edtDescricaoProduto);

        Button btn_salvar = findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(clickSalvar);

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
            EstabelecimentoAvaliacao estabelecimentoAvaliacao = new EstabelecimentoAvaliacao();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            estabelecimentoAvaliacao.nome = mAuth.getCurrentUser().getDisplayName();
            estabelecimentoAvaliacao.uid = currentUser.getUid();
            estabelecimentoAvaliacao.data = DateUtils.ConvertToStringFormat(new java.util.Date());
            estabelecimentoAvaliacao.avaliacao = (long) erAvaliacao.getRating();
            estabelecimentoAvaliacao.descricao = edtDescricaoProduto.getText().toString();

            ControllerEstabelecimentoAvaliacao.incluir(id_estabelecimento, estabelecimentoAvaliacao);


            long total = lEstabelecimentoAvaliacao.size() + 1;
            long nota = estabelecimentoAvaliacao.avaliacao;

            for (EstabelecimentoAvaliacao ea: lEstabelecimentoAvaliacao) {
                nota += ea.avaliacao;
            }

            estabelecimento.avaliacao = (nota/total);
            ControllerEstabelecimento.atualizar(estabelecimento);
            finish();
        }
    };


    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();
            lEstabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().where().eq("id_estabelecimento", id_estabelecimento).query();

            txtNomeEstabelecimento.setText(estabelecimento.nome);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

}
