package com.example.igormoraes.appseguranca.produto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igormoraes.appseguranca.AppTccAplication;
import com.example.igormoraes.appseguranca.R;
import com.example.igormoraes.appseguranca.controller.ControllerProduto;
import com.example.igormoraes.appseguranca.estabelecimento.InfoEstabelecimentoActivity;
import com.example.igormoraes.appseguranca.model.Produto;
import com.example.igormoraes.appseguranca.model.ProdutoAvaliacao;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class InfoProdutoActivity extends AppCompatActivity {
    private static final String TAG = InfoEstabelecimentoActivity.class.getName();

    private static final String PACKAGE_NAME = InfoProdutoActivity.class.getName();
    public static final String PARAM_ID = PACKAGE_NAME + ".ID";
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    private String id_produto;
    private String id_estabelecimento;
    private Produto produto;

    private ImageView imgPodutoFoto;
    private TextView txtNomeProduto;
    private TextView txtPrecoProduto;
    private TextView txtDescricaoProduto;
    private TextView txtAvaliacaoProduto;
    private ImageButton imbEditar;

    private RecyclerView rcwAvaliacoes;
    private TextView txtEmptyAvaliacoes;

    private AdapterProdutoAvaliacao adapterProdutoAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_produto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id_produto = getIntent().getExtras().getString(PARAM_ID, "");
        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");


        imgPodutoFoto = findViewById(R.id.imgPodutoFoto);
        txtAvaliacaoProduto = findViewById(R.id.txtAvaliacaoProduto);
        txtNomeProduto = findViewById(R.id.txtNomeProduto);
        txtPrecoProduto = findViewById(R.id.txtPrecoProduto);
        txtDescricaoProduto = findViewById(R.id.txtDescricaoProduto);
        imbEditar = findViewById(R.id.imbEditar);

        rcwAvaliacoes = findViewById(R.id.rcwAvaliacoes);
        txtEmptyAvaliacoes = findViewById(R.id.txtEmptyAvaliacoes);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(clickAvaliacao);

        imbEditar.setOnClickListener(clickEditar);

        ControllerProduto controllerProduto = new ControllerProduto();
        controllerProduto.atualizarAvaliacoes(this, id_estabelecimento, id_produto);

        setupRecycler();
    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcwAvaliacoes.setLayoutManager(layoutManager);

        adapterProdutoAvaliacao = new AdapterProdutoAvaliacao(this);
        rcwAvaliacoes.setAdapter(adapterProdutoAvaliacao);

        rcwAvaliacoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
            Intent i = new Intent(InfoProdutoActivity.this, EditarProdutoActivity.class);
            i.putExtra(EditarProdutoActivity.PARAM_ID, id_produto);
            i.putExtra(EditarProdutoActivity.PARAM_ID_ESTABELECIMENTO, id_estabelecimento);

            startActivity(i);
        }
    };

    private View.OnClickListener clickAvaliacao = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(InfoProdutoActivity.this, EditarProdutoAvaliacaoActivity.class);
            i.putExtra(EditarProdutoAvaliacaoActivity.PARAM_ID_PRODUTO, id_produto);
            i.putExtra(EditarProdutoAvaliacaoActivity.PARAM_ID_ESTABELECIMENTO, id_estabelecimento);

            startActivity(i);
        }
    };


    private void carregarDados(){
        try{
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();
            Dao<ProdutoAvaliacao, Integer> produtoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoAvaliacaoDao();

            produto = produtoDao.queryBuilder().where().eq("id", id_produto).and().eq("id_estabelecimento", id_estabelecimento).queryForFirst();

            txtNomeProduto.setText(produto.nome.toUpperCase());
            txtDescricaoProduto.setText(produto.descricao);
            txtAvaliacaoProduto.setText(Long.toString(produto.avaliacao));
            txtPrecoProduto.setText(String.format("R$ %s", Float.toString(produto.preco)));

            List<ProdutoAvaliacao> lProdutoAvaliacao = produtoAvaliacaoDao.queryBuilder().orderBy("data", false).where().eq("id_produto", id_produto).query();
            if (lProdutoAvaliacao.size()==0){
                txtAvaliacaoProduto.setText("N/A");
            }

            adapterProdutoAvaliacao.atualizarLista(lProdutoAvaliacao);
            rcwAvaliacoes.setVisibility(lProdutoAvaliacao.size()>0?View.VISIBLE:View.GONE);
            txtEmptyAvaliacoes.setVisibility(lProdutoAvaliacao.size()==0?View.VISIBLE:View.GONE);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }
}
