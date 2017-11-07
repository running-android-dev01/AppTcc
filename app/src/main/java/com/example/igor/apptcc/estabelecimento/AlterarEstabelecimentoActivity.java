package com.example.igor.apptcc.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.controller.EstabelecimentoController;
import com.example.igor.apptcc.modelDb.Estabelecimento;

public class AlterarEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = EstabelecimentoActivity.class.getSimpleName();
    private static final int CT_ALTERAR_ENDERECO = 1;

    private static final String PACKAGE_NAME = EstabelecimentoActivity.class.getName();
    public static final String PARAM_ID_ESTABELECIMENTO = PACKAGE_NAME + ".ID_ESTABELECIMENTO";

    private String ID_ESTABELECIMENTO;

    private EditText input_nome;
    private LinearLayout lnlEnderecoAlterEstabelecimento;
    private TextView txtEnderecoAlterEstabelecimento;
    private Button btn_salvar;

    private EstabelecimentoController estabelecimentoController;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ID_ESTABELECIMENTO = getIntent().getExtras().getString(PARAM_ID_ESTABELECIMENTO, "");

        input_nome = findViewById(R.id.input_nome);
        lnlEnderecoAlterEstabelecimento = findViewById(R.id.lnlEnderecoAlterEstabelecimento);
        txtEnderecoAlterEstabelecimento = findViewById(R.id.txtEnderecoAlterEstabelecimento);
        btn_salvar = findViewById(R.id.btn_salvar);


        estabelecimentoController = new EstabelecimentoController();
        estabelecimento = estabelecimentoController.carregarEstabelecimento(ID_ESTABELECIMENTO, this);

        input_nome.setText(estabelecimento.nome);
        txtEnderecoAlterEstabelecimento.setText(estabelecimento.enderecoCompleto);

        lnlEnderecoAlterEstabelecimento.setOnClickListener(clickEndereco);
        btn_salvar.setOnClickListener(clickAtualizar);
    }

    private View.OnClickListener clickAtualizar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            estabelecimentoController.atualizarEstab(estabelecimento);
            finish();
        }
    };

    private View.OnClickListener clickEndereco = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(AlterarEstabelecimentoActivity.this, MapaAlterarEstabelecimentoActivity.class);
            i.putExtra(MapaAlterarEstabelecimentoActivity.PARAM_NOME, estabelecimento.nome);
            i.putExtra(MapaAlterarEstabelecimentoActivity.PARAM_ENDERECO_COMPLETO, estabelecimento.enderecoCompleto);
            i.putExtra(MapaAlterarEstabelecimentoActivity.PARAM_LATITUDE, estabelecimento.latitude);
            i.putExtra(MapaAlterarEstabelecimentoActivity.PARAM_LONGITUDE, estabelecimento.longitude);

            startActivityForResult(i, CT_ALTERAR_ENDERECO);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CT_ALTERAR_ENDERECO && resultCode==RESULT_OK){
            estabelecimento.endereco = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_ENDERECO, "");
            estabelecimento.numero = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_NUMERO, "");
            estabelecimento.bairro = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_BAIRRO, "");
            estabelecimento.cidade = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_CIDADE, "");
            estabelecimento.uf = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_UF, "");
            estabelecimento.pais = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_PAIS, "");
            estabelecimento.enderecoCompleto = data.getExtras().getString(MapaAlterarEstabelecimentoActivity.PARAM_ENDERECO_COMPLETO, "");
            estabelecimento.latitude = data.getExtras().getDouble(MapaAlterarEstabelecimentoActivity.PARAM_LATITUDE, 0);
            estabelecimento.longitude = data.getExtras().getDouble(MapaAlterarEstabelecimentoActivity.PARAM_LONGITUDE, 0);

            txtEnderecoAlterEstabelecimento.setText(estabelecimento.enderecoCompleto);
        }
    }
}

