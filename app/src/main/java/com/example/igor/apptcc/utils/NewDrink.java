package com.example.igor.apptcc.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.igor.apptcc.EstabActivity;
import com.example.igor.apptcc.NewEstabActivity;
import com.example.igor.apptcc.R;
import com.example.igor.apptcc.controller.DrinkController;
import com.example.igor.apptcc.controller.NewEstabController;
import com.example.igor.apptcc.model.DrinkModel;

public class NewDrink extends AppCompatActivity {

    private static final String PACKAGE_NAME = NewDrink.class.getName();
    public static final String KEY_ESTAB = PACKAGE_NAME + ".ESTAB";
    public static final String KEY_DRINK = PACKAGE_NAME + ".DRINK";
    public static final String KEY_MODEL = PACKAGE_NAME + ".MODEL";

    private String keyEstab = "";
    private String keyDrink = "";
    private DrinkModel drinkModel;


    private EditText input_name;
    private EditText input_price;
    private Button btn_create_drink;
    private TextView link_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        keyEstab = getIntent().getExtras().getString(KEY_ESTAB, "");
        keyDrink = getIntent().getExtras().getString(KEY_DRINK, "");
        drinkModel = (DrinkModel) getIntent().getExtras().getSerializable(KEY_MODEL);

        input_name = (EditText) findViewById(R.id.input_name);
        input_price = (EditText) findViewById(R.id.input_price);
        btn_create_drink = (Button) findViewById(R.id.btn_create_drink);
        link_cancel = (TextView) findViewById(R.id.link_cancel);

        if (drinkModel!=null){
            input_name.setText(drinkModel.name);
            input_price.setText(drinkModel.price);
        }

        btn_create_drink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(keyDrink)){
                    addDrink();
                }else{
                    alterDrink();
                }
            }
        });

        link_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (drinkModel!=null){
            menu.add(1, 1, 1, "Remove");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            DrinkController.removeDrink(keyEstab, keyDrink);
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    private void addDrink(){
        DrinkController.saveDrink(keyEstab, input_name.getText().toString(), input_price.getText().toString());

        AlertDialog.Builder alert = new AlertDialog.Builder(NewDrink.this);
        alert.setMessage("Saved successfully");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    private void alterDrink(){
        DrinkController.alterDrink(keyEstab, keyDrink, input_name.getText().toString(), input_price.getText().toString());

        AlertDialog.Builder alert = new AlertDialog.Builder(NewDrink.this);
        alert.setMessage("Saved successfully");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                finish();
            }
        });
        alert.show();
    }

}
