package com.example.igormoraes.appfarmacia.produto;

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

import com.example.igormoraes.appfarmacia.AppTccAplication;
import com.example.igormoraes.appfarmacia.R;
import com.example.igormoraes.appfarmacia.controller.ControllerProduto;
import com.example.igormoraes.appfarmacia.model.Estabelecimento;
import com.example.igormoraes.appfarmacia.model.Produto;
import com.example.igormoraes.appfarmacia.model.ProdutoAvaliacao;
import com.example.igormoraes.appfarmacia.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class EditarProdutoAvaliacaoActivity extends AppCompatActivity {
    private static final String TAG = EditarProdutoAvaliacaoActivity.class.getName();

    private static final String PACKAGE_NAME = EditarProdutoAvaliacaoActivity.class.getName();
    public static final String PARAM_ID_PRODUTO = PACKAGE_NAME + ".ID_PRODUTO";
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    //private String id_avaliacao;
    private String id_produto;
    private String id_estabelecimento;

    private TextView txtNomeProduto;
    private RatingBar erAvaliacao;
    private EditText edtDescricaoProduto;

    private Estabelecimento estabelecimento;
    private Produto produto;
    private List<ProdutoAvaliacao> lProdutoAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto_avaliacao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //id_avaliacao = getIntent().getExtras().getString(PARAM_ID, "");
        id_produto = getIntent().getExtras().getString(PARAM_ID_PRODUTO, "");
        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");

        txtNomeProduto = findViewById(R.id.txtNomeProduto);
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
            ProdutoAvaliacao produtoAvaliacao = new ProdutoAvaliacao();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            produtoAvaliacao.nome = mAuth.getCurrentUser().getDisplayName();
            produtoAvaliacao.uid = mAuth.getUid();
            produtoAvaliacao.data = DateUtils.ConvertToStringFormat(new java.util.Date());
            produtoAvaliacao.avaliacao = (long) erAvaliacao.getRating();
            produtoAvaliacao.descricao = edtDescricaoProduto.getText().toString();

            ControllerProduto.incluirAvaliacao(id_estabelecimento, id_produto, produtoAvaliacao);


            long total = lProdutoAvaliacao.size() + 1;
            long nota = produtoAvaliacao.avaliacao;

            for (ProdutoAvaliacao pa: lProdutoAvaliacao) {
                nota += pa.avaliacao;
            }

            produto.avaliacao = (nota/total);


            ControllerProduto.atualizar(estabelecimento, produto);
            finish();
        }
    };


    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoAvaliacaoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();

            produto = produtoDao.queryBuilder().where().eq("id", id_produto).and().eq("id_estabelecimento",id_estabelecimento).queryForFirst();
            lProdutoAvaliacao = produtoAvaliacaoDao.queryBuilder().orderBy("data", false).where().eq("id_produto", id_produto).query();

            txtNomeProduto.setText(produto.nome);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

}
