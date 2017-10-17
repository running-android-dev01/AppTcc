package com.example.igor.apptcc;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.igor.apptcc.controller.AvaliableController;
import com.example.igor.apptcc.utils.NewDrink;

public class NewAvaliableActivity extends AppCompatActivity {
    private static final String PACKAGE_NAME = NewAvaliableActivity.class.getName();
    public static final String KEY_ESTAB = PACKAGE_NAME + ".ESTAB";

    private String keyEstab = "";

    RatingBar ratingBar;
    EditText input_avaliable;
    Button btn_create_avalible;
    TextView link_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avaliable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        keyEstab = getIntent().getExtras().getString(KEY_ESTAB, "");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        input_avaliable = (EditText) findViewById(R.id.input_avaliable);
        btn_create_avalible = (Button) findViewById(R.id.btn_create_avalible);
        link_cancel = (TextView) findViewById(R.id.link_cancel);

        btn_create_avalible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAvaliable();
            }
        });

        link_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createAvaliable(){
        float avaliable = ratingBar.getRating();
        String description = input_avaliable.getText().toString();

        if (avaliable==0.0){
            AlertDialog.Builder alert = new AlertDialog.Builder(NewAvaliableActivity.this);
            alert.setMessage("Report note");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

        AvaliableController.saveAvaliable(keyEstab, avaliable, description);

        AlertDialog.Builder alert = new AlertDialog.Builder(NewAvaliableActivity.this);
        alert.setMessage("Saved successfully");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

}
