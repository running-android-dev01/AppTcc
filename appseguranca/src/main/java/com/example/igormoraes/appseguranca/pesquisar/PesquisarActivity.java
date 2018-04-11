package com.example.igormoraes.appseguranca.pesquisar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.igormoraes.appseguranca.AppTccAplication;
import com.example.igormoraes.appseguranca.R;
import com.example.igormoraes.appseguranca.model.Estabelecimento;
import com.example.igormoraes.appseguranca.model.PesquisaModel;
import com.example.igormoraes.appseguranca.model.Produto;
import com.example.igormoraes.appseguranca.utils.AndroidUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PesquisarActivity extends AppCompatActivity {
    private static final String TAG = PesquisarActivity.class.getName();

    private boolean estabelecimento = true;

    private double latitude;
    private double longitude;

    private static final String PACKAGE_NAME = PesquisarActivity.class.getName();
    public static final String PARAM_LATITUDE = PACKAGE_NAME + ".LATITUDE";
    public static final String PARAM_LONGITUDE = PACKAGE_NAME + ".LONGITUDE";

    private EditText edtPesquisar;
    private Button btn_pesquisar;
    private RecyclerView rcwPesquisa;
    private TextView txtEmpty;


    private AdapterPesquisa adapterPesquisa;

    private List<PesquisaModel> lPesquisaModel = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_estabelecimento:
                    txtEmpty.setVisibility(View.GONE);
                    estabelecimento = true;
                    edtPesquisar.setText("");
                    btn_pesquisar.setOnClickListener(clickEstabelecimento);
                    lPesquisaModel.clear();
                    adapterPesquisa.atualizarLista(lPesquisaModel);
                    return true;
                case R.id.navigation_produto:
                    txtEmpty.setVisibility(View.GONE);
                    estabelecimento = false;
                    edtPesquisar.setText("");
                    btn_pesquisar.setOnClickListener(clickProduto);
                    lPesquisaModel.clear();
                    adapterPesquisa.atualizarLista(lPesquisaModel);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        latitude = getIntent().getExtras().getDouble(PARAM_LATITUDE, 0.0);
        longitude = getIntent().getExtras().getDouble(PARAM_LONGITUDE, 0.0);

        edtPesquisar = findViewById(R.id.edtPesquisar);
        btn_pesquisar = findViewById(R.id.btn_pesquisar);
        rcwPesquisa = findViewById(R.id.rcwPesquisa);
        txtEmpty = findViewById(R.id.txtEmpty);

        txtEmpty.setVisibility(View.GONE);

        btn_pesquisar.setOnClickListener(clickEstabelecimento);

        setupRecycler();
    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcwPesquisa.setLayoutManager(layoutManager);

        adapterPesquisa = new AdapterPesquisa(this);
        rcwPesquisa.setAdapter(adapterPesquisa);

        rcwPesquisa.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private View.OnClickListener clickProduto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                String produtoLike = edtPesquisar.getText().toString().trim();
                Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();

                List<Produto> produto = produtoDao.queryBuilder().where().like("nome", "%" + produtoLike + "%").query();


                lPesquisaModel.clear();
                for (Produto p:produto) {
                    PesquisaModel pesquisa = new PesquisaModel();

                    pesquisa.produto_key = p.id;
                    pesquisa.produto_nome = p.nome;
                    pesquisa.estabelecimento_key = p.id_estabelecimento;
                    pesquisa.estabelecimento_nome = p.estabelecimento_nome;
                    pesquisa.estabelecimento_endereco = p.estabelecimento_endereco;
                    pesquisa.estabelecimento_referencia = p.estabelecimento_referencia;

                    pesquisa.distancia = AndroidUtils.calculaDistancia(latitude, longitude, p.estabelecimento_latitude, p.estabelecimento_longitude);


                    lPesquisaModel.add(pesquisa);
                }

                Collections.sort(lPesquisaModel, new Comparator<PesquisaModel>() {
                    @Override
                    public int compare(PesquisaModel o1, PesquisaModel o2) {
                        Double d1 = o1.distancia;
                        Double d2 = o2.distancia;
                        return d1.compareTo(d2);
                    }
                });

                txtEmpty.setVisibility(lPesquisaModel.size()==0?View.VISIBLE:View.GONE);
                adapterPesquisa.atualizarLista(lPesquisaModel);
            }catch (SQLException ex){
                Log.e(TAG, "ERRO = ", ex);
            }
        }
    };

    private View.OnClickListener clickEstabelecimento = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                String estabelecimentoLike = edtPesquisar.getText().toString().trim();
                Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();

                List<Estabelecimento> estabelecimentos = estabelecimentoDao.queryBuilder().where().like("nome", "%" + estabelecimentoLike + "%").query();


                lPesquisaModel.clear();
                for (Estabelecimento e:estabelecimentos) {
                    PesquisaModel pesquisa = new PesquisaModel();

                    pesquisa.produto_key = "";
                    pesquisa.produto_nome = e.nome;
                    pesquisa.estabelecimento_key = e.id;
                    pesquisa.estabelecimento_nome = "";
                    pesquisa.estabelecimento_endereco = e.endereco;
                    pesquisa.estabelecimento_referencia = e.referencia;

                    pesquisa.distancia = AndroidUtils.calculaDistancia(latitude, longitude, e.latitude, e.longitude);


                    lPesquisaModel.add(pesquisa);
                }

                Collections.sort(lPesquisaModel, new Comparator<PesquisaModel>() {
                    @Override
                    public int compare(PesquisaModel o1, PesquisaModel o2) {
                        Double d1 = o1.distancia;
                        Double d2 = o2.distancia;
                        return d1.compareTo(d2);
                    }
                });

                txtEmpty.setVisibility(lPesquisaModel.size()==0?View.VISIBLE:View.GONE);
                adapterPesquisa.atualizarLista(lPesquisaModel);
            }catch (SQLException ex){
                Log.e(TAG, "ERRO = ", ex);
            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
