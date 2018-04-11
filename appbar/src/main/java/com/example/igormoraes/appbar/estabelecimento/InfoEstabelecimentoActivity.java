package com.example.igormoraes.appbar.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.TextView;

import com.example.igormoraes.appbar.AppTccAplication;
import com.example.igormoraes.appbar.R;
import com.example.igormoraes.appbar.controller.ControllerEstabelecimento;
import com.example.igormoraes.appbar.model.Estabelecimento;
import com.example.igormoraes.appbar.model.EstabelecimentoAvaliacao;
import com.example.igormoraes.appbar.model.Produto;
import com.example.igormoraes.appbar.produto.AdapterProduto;
import com.example.igormoraes.appbar.produto.EditarProdutoActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class InfoEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = InfoEstabelecimentoActivity.class.getName();

    private static final String PACKAGE_NAME = InfoEstabelecimentoActivity.class.getName();
    public static final String PARAM_ID = PACKAGE_NAME + ".ID";

    private String id_estabelecimento;
    private Estabelecimento estabelecimento;

    private TextView txtAvaliacaoEstabelecimento;
    private TextView txtNomeEstabelecimento;
    private TextView txtEnderecoEstabelecimento;

    private TextView txtProdutoAvaliacao;

    private TextView txtEmptyAvaliacao;
    private TextView txtEmptyProdutos;
    private RecyclerView rcwProdutos;

    private Boolean telaProduto = true;


    private AdapterProduto adapterProduto;
    private AdapterEstabelecimentoAvaliacao adapterEstabelecimentoAvaliacao;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_produto:
                    telaProduto = true;
                    try{
                        rcwProdutos.setAdapter(adapterProduto);
                        txtProdutoAvaliacao.setText(getString(R.string.title_produtos));

                        Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();
                        List<Produto> lProduto = produtoDao.queryBuilder().orderBy("avaliacao", false).where().eq("id_estabelecimento", id_estabelecimento).query();
                        adapterProduto.atualizarLista(lProduto);

                        rcwProdutos.setVisibility(lProduto.size()>0?View.VISIBLE:View.GONE);
                        txtEmptyProdutos.setVisibility(lProduto.size()==0?View.VISIBLE:View.GONE);

                        //rcwAvaliacao.setVisibility(View.GONE);
                        txtEmptyAvaliacao.setVisibility(View.GONE);
                    }catch (SQLException ex){
                        Log.e(TAG, "ERRO = ", ex);
                    }
                    return true;
                case R.id.navigation_avaliacao:
                    telaProduto = false;
                    try{
                        rcwProdutos.setAdapter(adapterEstabelecimentoAvaliacao);
                        txtProdutoAvaliacao.setText(getString(R.string.title_avaliacao));

                        Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
                        List<EstabelecimentoAvaliacao> lEstabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().orderBy("data", false).where().eq("id_estabelecimento", id_estabelecimento).query();
                        adapterEstabelecimentoAvaliacao.atualizarLista(lEstabelecimentoAvaliacao);

                        rcwProdutos.setVisibility(lEstabelecimentoAvaliacao.size()>0?View.VISIBLE:View.GONE);
                        txtEmptyAvaliacao.setVisibility(lEstabelecimentoAvaliacao.size()==0?View.VISIBLE:View.GONE);

                        //rcwProdutos.setVisibility(View.GONE);
                        txtEmptyProdutos.setVisibility(View.GONE);
                    }catch (SQLException ex){
                        Log.e(TAG, "ERRO = ", ex);
                    }
                    return true;
            }
            return false;
        }
    };


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
        rcwProdutos = findViewById(R.id.rcwProdutos);

        txtProdutoAvaliacao = findViewById(R.id.txtProdutoAvaliacao);
        txtProdutoAvaliacao.setText(getString(R.string.title_produtos));

        txtEmptyProdutos = findViewById(R.id.txtEmptyProdutos);

        txtEmptyProdutos.setVisibility(View.GONE);

        txtEmptyAvaliacao = findViewById(R.id.txtEmptyAvaliacao);

        txtEmptyAvaliacao.setVisibility(View.GONE);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(clickFab);


        ImageButton imbEditar = findViewById(R.id.imbEditar);
        imbEditar.setOnClickListener(clickEditar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ControllerEstabelecimento controllerEstabelecimento = new ControllerEstabelecimento();
        controllerEstabelecimento.atualizarInfomacoes(this, id_estabelecimento);

        setupRecycler();
    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcwProdutos.setLayoutManager(layoutManager);

        adapterProduto = new AdapterProduto(this);
        rcwProdutos.setAdapter(adapterProduto);

        rcwProdutos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapterEstabelecimentoAvaliacao = new AdapterEstabelecimentoAvaliacao(this);
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

    private View.OnClickListener clickFab = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (telaProduto){
                Intent i = new Intent(InfoEstabelecimentoActivity.this, EditarProdutoActivity.class);
                i.putExtra(EditarProdutoActivity.PARAM_ID, "");
                i.putExtra(EditarProdutoActivity.PARAM_ID_ESTABELECIMENTO, id_estabelecimento);

                startActivity(i);
            }else{
                Intent i = new Intent(InfoEstabelecimentoActivity.this, EditarEstabelecimentoAvaliacaoActivity.class);
                i.putExtra(EditarEstabelecimentoAvaliacaoActivity.PARAM_ID_ESTABELECIMENTO, estabelecimento.id);

                startActivity(i);
            }

        }
    };



    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            Dao<EstabelecimentoAvaliacao, Integer> estabelecimentoAvaliacaoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoAvaliacaoDao();
            Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();

            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();

            txtNomeEstabelecimento.setText(estabelecimento.nome);
            txtEnderecoEstabelecimento.setText(estabelecimento.endereco);
            txtAvaliacaoEstabelecimento.setText(Long.toString(estabelecimento.avaliacao));



            if (telaProduto){
                List<Produto> lProduto = produtoDao.queryBuilder().orderBy("avaliacao", false).where().eq("id_estabelecimento", id_estabelecimento).query();
                adapterProduto.atualizarLista(lProduto);

                List<EstabelecimentoAvaliacao> lEstabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().limit((long)5).orderBy("data", false).where().eq("id_estabelecimento", id_estabelecimento).query();
                if (lEstabelecimentoAvaliacao.size()==0){
                    txtAvaliacaoEstabelecimento.setText("N/A");
                }

                rcwProdutos.setVisibility(lProduto.size()>0?View.VISIBLE:View.GONE);
                txtEmptyProdutos.setVisibility(lProduto.size()==0?View.VISIBLE:View.GONE);

                txtEmptyAvaliacao.setVisibility(View.GONE);
            }else{
                List<EstabelecimentoAvaliacao> lEstabelecimentoAvaliacao = estabelecimentoAvaliacaoDao.queryBuilder().orderBy("data", false).where().eq("id_estabelecimento", id_estabelecimento).query();
                if (lEstabelecimentoAvaliacao.size()==0){
                    txtAvaliacaoEstabelecimento.setText("N/A");
                }

                rcwProdutos.setVisibility(lEstabelecimentoAvaliacao.size()>0?View.VISIBLE:View.GONE);
                txtEmptyProdutos.setVisibility(View.GONE);

                txtEmptyAvaliacao.setVisibility(lEstabelecimentoAvaliacao.size()==0?View.VISIBLE:View.GONE);
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }
}
