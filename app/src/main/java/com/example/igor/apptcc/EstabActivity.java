package com.example.igor.apptcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.igor.apptcc.model.EstabModel;
import com.example.igor.apptcc.utils.FetchLocationIntentUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EstabActivity extends AppCompatActivity {
    private static final String PACKAGE_NAME = EstabActivity.class.getName();
    public static final String RECEIVER_KEY = PACKAGE_NAME + ".KEY";

    public String KEY = "";


    private TextView title;
    private TextView snippet;

    private TextView txtDrinkName1;
    private TextView txtDrinkPrice1;
    private View viewDrink1;

    private TextView txtDrinkName2;
    private TextView txtDrinkPrice2;
    private View viewDrink2;

    private TextView txtDrinkName3;
    private TextView txtDrinkPrice3;
    private View viewDrink3;

    private TextView txtAvaliableName1;
    private TextView txtAvaliableNote1;
    private TextView txtAvaliable1;
    private View viewAvaliable1;

    private TextView txtAvaliableName2;
    private TextView txtAvaliableNote2;
    private TextView txtAvaliable2;
    private View viewAvaliable2;

    private TextView txtAvaliableName3;
    private TextView txtAvaliableNote3;
    private TextView txtAvaliable3;
    private View viewAvaliable3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        KEY = getIntent().getExtras().getString(RECEIVER_KEY, "");

        title = (TextView) findViewById(R.id.title);
        snippet = (TextView) findViewById(R.id.snippet);

        txtDrinkName1 = (TextView) findViewById(R.id.txtDrinkName1);
        txtDrinkPrice1 = (TextView) findViewById(R.id.txtDrinkPrice1);
        viewDrink1 = (View) findViewById(R.id.viewDrink1);

        txtDrinkName2 = (TextView) findViewById(R.id.txtDrinkName2);
        txtDrinkPrice2 = (TextView) findViewById(R.id.txtDrinkPrice2);
        viewDrink2 = (View) findViewById(R.id.viewDrink2);

        txtDrinkName3 = (TextView) findViewById(R.id.txtDrinkName3);
        txtDrinkPrice3 = (TextView) findViewById(R.id.txtDrinkPrice3);
        viewDrink3 = (View) findViewById(R.id.viewDrink3);

        txtAvaliableName1 = (TextView) findViewById(R.id.txtAvaliableName1);
        txtAvaliableNote1 = (TextView) findViewById(R.id.txtAvaliableNote1);
        txtAvaliable1 = (TextView) findViewById(R.id.txtAvaliable1);
        viewAvaliable1 = (View) findViewById(R.id.viewAvaliable1);

        txtAvaliableName2 = (TextView) findViewById(R.id.txtAvaliableName2);
        txtAvaliableNote2 = (TextView) findViewById(R.id.txtAvaliableNote2);
        txtAvaliable2 = (TextView) findViewById(R.id.txtAvaliable2);
        viewAvaliable2 = (View) findViewById(R.id.viewAvaliable2);

        txtAvaliableName3 = (TextView) findViewById(R.id.txtAvaliableName3);
        txtAvaliableNote3 = (TextView) findViewById(R.id.txtAvaliableNote3);
        txtAvaliable3 = (TextView) findViewById(R.id.txtAvaliable3);
        viewAvaliable3 = (View) findViewById(R.id.viewAvaliable3);

        loadEstab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Drink");
        menu.add(1, 2, 2, "Avaliable");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {

        } else if (item.getItemId() == 2) {

        }
        return true;
    }

    private void loadEstab(){

        txtDrinkName1.setVisibility(View.GONE);
        txtDrinkPrice1.setVisibility(View.GONE);
        viewDrink1.setVisibility(View.GONE);

        txtDrinkName2.setVisibility(View.GONE);
        txtDrinkPrice2.setVisibility(View.GONE);
        viewDrink2.setVisibility(View.GONE);

        txtDrinkName3.setVisibility(View.GONE);
        txtDrinkPrice3.setVisibility(View.GONE);
        viewDrink3.setVisibility(View.GONE);

        txtAvaliableName1.setVisibility(View.GONE);
        txtAvaliableNote1.setVisibility(View.GONE);
        txtAvaliable1.setVisibility(View.GONE);
        viewAvaliable1.setVisibility(View.GONE);

        txtAvaliableName2.setVisibility(View.GONE);
        txtAvaliableNote2.setVisibility(View.GONE);
        txtAvaliable2.setVisibility(View.GONE);
        viewAvaliable2.setVisibility(View.GONE);

        txtAvaliableName3.setVisibility(View.GONE);
        txtAvaliableNote3.setVisibility(View.GONE);
        txtAvaliable3.setVisibility(View.GONE);
        viewAvaliable3.setVisibility(View.GONE);

        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference("date/establishment/" + KEY);
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                EstabModel estabModel = snapshot.getValue(EstabModel.class);

                title.setText(estabModel.name);
                snippet.setText(estabModel.address + " - " + estabModel.locality + " - " + estabModel.countryName);

                txtDrinkName1.setVisibility(View.VISIBLE);
                txtDrinkPrice1.setVisibility(View.VISIBLE);
                viewDrink1.setVisibility(View.VISIBLE);

                txtDrinkName2.setVisibility(View.VISIBLE);
                txtDrinkPrice2.setVisibility(View.VISIBLE);
                viewDrink2.setVisibility(View.VISIBLE);

                txtDrinkName3.setVisibility(View.VISIBLE);
                txtDrinkPrice3.setVisibility(View.VISIBLE);
                viewDrink3.setVisibility(View.VISIBLE);

                txtAvaliableName1.setVisibility(View.VISIBLE);
                txtAvaliableNote1.setVisibility(View.VISIBLE);
                txtAvaliable1.setVisibility(View.VISIBLE);
                viewAvaliable1.setVisibility(View.VISIBLE);

                txtAvaliableName2.setVisibility(View.VISIBLE);
                txtAvaliableNote2.setVisibility(View.VISIBLE);
                txtAvaliable2.setVisibility(View.VISIBLE);
                viewAvaliable2.setVisibility(View.VISIBLE);

                txtAvaliableName3.setVisibility(View.VISIBLE);
                txtAvaliableNote3.setVisibility(View.VISIBLE);
                txtAvaliable3.setVisibility(View.VISIBLE);
                viewAvaliable3.setVisibility(View.VISIBLE);



                txtDrinkName1.setText("BEBIDA 1");
                txtDrinkPrice1.setText("R$ 4,50");

                txtDrinkName2.setText("BEBIDA 2");
                txtDrinkPrice2.setText("R$ 9,50");

                txtDrinkName3.setText("BEBIDA 3");
                txtDrinkPrice3.setText("R$ 15,50");

                txtAvaliableName1.setText("AVALIADOR 1");
                txtAvaliableNote1.setText("4");
                txtAvaliable1.setText("AVALIACAO BOA DE MAIS. AVALIACAO BOA DE MAIS. AVALIACAO BOA DE MAIS. AVALIACAO BOA DE MAIS. AVALIACAO BOA DE MAIS.");

                txtAvaliableName2.setText("AVALIADOR 2");
                txtAvaliableNote2.setText("2");
                txtAvaliable2.setText("AVALIACAO PIQUENA.");

                txtAvaliableName3.setText("AVALIADOR 3");
                txtAvaliableNote3.setText("1");
                txtAvaliable3.setText("AVALIACAO MEDIA. AVALIACAO MEDIA. AVALIACAO MEDIA. AVALIACAO MEDIA.");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

    }

}
