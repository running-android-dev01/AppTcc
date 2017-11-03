package com.example.igor.apptcc.estabelecimento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.service.FetchLocationIntentService;
import com.example.igor.apptcc.utils.FetchLocationIntentUtils;


public class EnderecoEstabelecimentoActivity extends AppCompatActivity {
    private static final String TAG = EnderecoEstabelecimentoActivity.class.getSimpleName();

    private static final String PACKAGE_NAME = EnderecoEstabelecimentoActivity.class.getName();
    public static final String PARAM_NOME_ESTABELECIMENTO = PACKAGE_NAME + ".NOME";


    EditText input_nome;
    EditText input_endereco;
    EditText input_numero;
    EditText input_complemento;
    EditText input_bairro;
    EditText input_cidade;
    EditText input_uf;
    EditText input_pais;
    Button btn_avancar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        String nomeEstabelecimento = getIntent().getExtras().getString(PARAM_NOME_ESTABELECIMENTO, "");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        input_nome = findViewById(R.id.input_nome);
        input_endereco = findViewById(R.id.input_endereco);
        input_numero = findViewById(R.id.input_numero);
        input_complemento = findViewById(R.id.input_complemento);
        input_bairro = findViewById(R.id.input_bairro);
        input_cidade = findViewById(R.id.input_cidade);
        input_uf = findViewById(R.id.input_uf);
        input_pais = findViewById(R.id.input_pais);
        btn_avancar = findViewById(R.id.btn_avancar);


        input_nome.setFocusable(false);
        input_nome.setText(nomeEstabelecimento);

        btn_avancar.setOnClickListener(clickAvancar);
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

    private View.OnClickListener clickAvancar = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (validarEndereco()){
                String nome = input_nome.getText().toString().toUpperCase();
                String endereco = input_endereco.getText().toString().toUpperCase();
                String numero = input_numero.getText().toString().toUpperCase();
                String complemento = input_complemento.getText().toString().toUpperCase();
                String bairro = input_bairro.getText().toString().toUpperCase();
                String cidade = input_cidade.getText().toString().toUpperCase();
                String uf = input_uf.getText().toString().toUpperCase();
                String pais = input_pais.getText().toString().toUpperCase();

                String _address = endereco + ", " + numero + ", " + complemento + ", " + bairro + ", " + cidade + ", " + uf + ", " + pais;

                LocationsResultReceiver mResultReceiverLocation = new LocationsResultReceiver(new Handler());

                Intent intent = new Intent(EnderecoEstabelecimentoActivity.this, FetchLocationIntentService.class);
                intent.putExtra(FetchLocationIntentUtils.RECEIVER, mResultReceiverLocation);
                intent.putExtra(FetchLocationIntentUtils.LOCATION_DATA_EXTRA, _address);
                startService(intent);
            }
        }
    };

    private boolean validarEndereco(){
        boolean validacao = true;
        if (TextUtils.isEmpty(input_nome.getText().toString())){
            input_nome.setError("Informe o nome");
            validacao = false;
        }

        if (TextUtils.isEmpty(input_endereco.getText().toString())){
            input_endereco.setError("Informe o endereço");
            validacao = false;
        }

        if (TextUtils.isEmpty(input_cidade.getText().toString())){
            input_cidade.setError("Informe a cidade");
            validacao = false;
        }
        return validacao;
    }


    private class LocationsResultReceiver extends ResultReceiver {
        LocationsResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            double pLatitude = resultData.getDouble(FetchLocationIntentUtils.RESULT_DATA_LATITUDE);
            double pLongitude = resultData.getDouble(FetchLocationIntentUtils.RESULT_DATA_LONGITUDE);

            String pAddress = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_ADDRESS);
            String pLocality = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_LOCALITY);
            String pCountryName = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_COUNTRYNAME);

            String nome = input_nome.getText().toString().toUpperCase();
            String endereco = input_endereco.getText().toString().toUpperCase();
            String complemento = input_complemento.getText().toString().toUpperCase();
            String numero = input_numero.getText().toString().toUpperCase();
            String bairro = input_bairro.getText().toString().toUpperCase();
            String cidade = pLocality.toUpperCase();
            String uf = input_uf.getText().toString().toUpperCase();
            String pais = pCountryName.toUpperCase();
            String enderecoCompleto = pAddress;
            double latitude = pLatitude;
            double longitude = pLongitude;


            Intent i = new Intent(EnderecoEstabelecimentoActivity.this, MapaEstabelecimentoActivity.class);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_NOME, nome);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_ENDERECO, endereco);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_COMPLEMENTO, complemento);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_NUMERO, numero);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_BAIRRO, bairro);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_CIDADE, cidade);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_UF, uf);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_PAIS, pais);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_ENDERECO_COMPLETO, enderecoCompleto);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_LATITUDE, latitude);
            i.putExtra(MapaEstabelecimentoActivity.PARAM_LONGITUDE, longitude);

            startActivity(i);

        }
    }

}
