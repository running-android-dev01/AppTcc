package com.example.igor.apptcc.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igor.apptcc.AppTccAplication;
import com.example.igor.apptcc.R;
import com.example.igor.apptcc.avaliacaoEstabelecimento.AvaliacacaoEstabelecimentoAdapter;
import com.example.igor.apptcc.controller.EstabelecimentoController;
import com.example.igor.apptcc.listagem.ListagemActivity;
import com.example.igor.apptcc.modelDb.AvaliacaoEstabelecimento;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = EstabelecimentoActivity.class.getSimpleName();

    private static final String PACKAGE_NAME = EstabelecimentoActivity.class.getName();
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    private String ID_ESTABELECIMENTO;
    private Estabelecimento estabelecimento;

    private TextView txtNomeEstabelecimento;
    private TextView txtAvaliacaoEstabelecimento;
    private TextView txtEnderecoEstabelecimento;
    private EstabelecimentoController estabelecimentoController;

    private RecyclerView rwAvaliacao;
    private LinearLayout lnlTodosComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ID_ESTABELECIMENTO = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");

        txtNomeEstabelecimento = findViewById(R.id.txtNomeEstabelecimento);
        txtAvaliacaoEstabelecimento = findViewById(R.id.txtAvaliacaoEstabelecimento);
        txtEnderecoEstabelecimento = findViewById(R.id.txtEnderecoEstabelecimento);

        rwAvaliacao = findViewById(R.id.rwAvaliacao);
        lnlTodosComentarios = findViewById(R.id.lnlTodosComentarios);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EstabelecimentoActivity.this, AlterarEstabelecimentoActivity.class);
                i.putExtra(AlterarEstabelecimentoActivity.PARAM_ID_ESTABELECIMENTO, ID_ESTABELECIMENTO);
                startActivity(i);
            }
        });


        List<AvaliacaoEstabelecimento> lAvaliacaoEstabelecimento = new ArrayList<>();
        for (int i = 0; i<=5; i++){
            AvaliacaoEstabelecimento avaliacaoEstabelecimento = new AvaliacaoEstabelecimento();
            avaliacaoEstabelecimento.nome = "NOME " + i;
            avaliacaoEstabelecimento.nota = i/5;
            avaliacaoEstabelecimento.data = "07/11/2017";
            avaliacaoEstabelecimento.avaliacao = "NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i + " NOME " + i;

            lAvaliacaoEstabelecimento.add(avaliacaoEstabelecimento);
        }

        // final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager _layoutManager = new LinearLayoutManager(this);
        rwAvaliacao.setLayoutManager(_layoutManager);
        rwAvaliacao.setAdapter(new AvaliacacaoEstabelecimentoAdapter(this, lAvaliacaoEstabelecimento));
    }

    @Override
    protected void onResume() {
        super.onResume();
        estabelecimentoController = new EstabelecimentoController();
        estabelecimento = estabelecimentoController.carregarEstabelecimento(ID_ESTABELECIMENTO, this);

        txtNomeEstabelecimento.setText(estabelecimento.nome);
        txtAvaliacaoEstabelecimento.setText("5*");
        txtEnderecoEstabelecimento.setText(estabelecimento.enderecoCompleto);
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

    private View.OnClickListener clickEstab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(EstabelecimentoActivity.this, "AQUI", Toast.LENGTH_SHORT).show();
        }
    };
}
