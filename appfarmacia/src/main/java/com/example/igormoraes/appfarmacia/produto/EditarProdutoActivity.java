package com.example.igormoraes.appfarmacia.produto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.igormoraes.appfarmacia.AppTccAplication;
import com.example.igormoraes.appfarmacia.R;
import com.example.igormoraes.appfarmacia.controller.ControllerProduto;
import com.example.igormoraes.appfarmacia.model.Estabelecimento;
import com.example.igormoraes.appfarmacia.model.Produto;
import com.example.igormoraes.appfarmacia.utils.AndroidUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;

public class EditarProdutoActivity extends AppCompatActivity {
    private static final String TAG = EditarProdutoActivity.class.getName();

    private static final String PACKAGE_NAME = EditarProdutoActivity.class.getName();
    public static final String PARAM_ID = PACKAGE_NAME + ".ID";
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    private String id_produto;
    private String id_estabelecimento;

    private EditText edtNomeProduto;
    private EditText edtDescricaoProduto;
    private EditText edtPrecoProduto;

    private Produto produto;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        id_produto = getIntent().getExtras().getString(PARAM_ID, "");
        id_estabelecimento = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");


        edtNomeProduto = findViewById(R.id.edtNomeProduto);
        edtDescricaoProduto = findViewById(R.id.edtDescricaoProduto);
        edtPrecoProduto = findViewById(R.id.edtPrecoProduto);

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
            edtNomeProduto.setError(null);
            edtDescricaoProduto.setError(null);
            edtPrecoProduto.setError(null);

            if (TextUtils.isEmpty(edtNomeProduto.getText().toString())){
                edtNomeProduto.setError(getString(R.string.informe_nome_produto));
                return;
            }


            if (TextUtils.isEmpty(edtPrecoProduto.getText().toString())){
                edtPrecoProduto.setError(getString(R.string.informe_preco_produto));
                return;
            }


            produto.nome = edtNomeProduto.getText().toString();
            produto.preco = AndroidUtils.convertStringToFloat(edtPrecoProduto.getText().toString());
            produto.descricao= edtDescricaoProduto.getText().toString();



            if (TextUtils.isEmpty(id_produto)){
                ControllerProduto.incluir(estabelecimento, produto);
            }else{
                ControllerProduto.atualizar(estabelecimento, produto);
            }

            finish();
        }
    };


    private void carregarDados(){
        try{
            Dao<Estabelecimento, Integer> estabelecimentoDao = ((AppTccAplication)getApplicationContext()).getHelper().getEstabelecimentoDao();
            estabelecimento = estabelecimentoDao.queryBuilder().where().eq("id", id_estabelecimento).queryForFirst();

            Dao<Produto, Integer> produtoDao = ((AppTccAplication)getApplicationContext()).getHelper().getProdutoDao();
            produto = produtoDao.queryBuilder().where().eq("id", id_produto).and().eq("id_estabelecimento",id_estabelecimento).queryForFirst();
            if (produto==null){
                produto = new Produto();

                produto.nome = "";
                produto.preco = 0f;
                produto.descricao= "";
                produto.avaliacao = 0;
            }

            edtNomeProduto.setText(produto.nome);
            edtPrecoProduto.setText(String.format("%.2f", produto.preco));
            edtDescricaoProduto.setText(produto.descricao);
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }

}
