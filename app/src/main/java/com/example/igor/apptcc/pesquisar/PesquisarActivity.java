package com.example.igor.apptcc.pesquisar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.igor.apptcc.R;

import java.sql.SQLException;

public class PesquisarActivity extends AppCompatActivity {

    private boolean estabelecimento = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_estabelecimento:
                    estabelecimento = true;
                    edtPesquisar.setText("");
                    btn_pesquisar.setOnClickListener(clickEstabelecimento);
                    return true;
                case R.id.navigation_produto:
                    estabelecimento = false;
                    edtPesquisar.setText("");
                    btn_pesquisar.setOnClickListener(clickProduto);
                    return true;
            }
            return false;
        }
    };


    private EditText edtPesquisar;
    private Button btn_pesquisar;

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

        edtPesquisar = findViewById(R.id.edtPesquisar);
        btn_pesquisar = findViewById(R.id.btn_pesquisar);

        btn_pesquisar.setOnClickListener(clickEstabelecimento);
    }

    private View.OnClickListener clickProduto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener clickEstabelecimento = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
