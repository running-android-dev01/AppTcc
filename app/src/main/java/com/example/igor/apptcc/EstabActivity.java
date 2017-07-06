package com.example.igor.apptcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.igor.apptcc.model.DrinkModel;
import com.example.igor.apptcc.model.EstabModel;
import com.example.igor.apptcc.utils.FetchLocationIntentUtils;
import com.example.igor.apptcc.utils.NewDrink;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EstabActivity extends AppCompatActivity {
    private static final String TAG = EstabActivity.class.getName();
    private static final String PACKAGE_NAME = EstabActivity.class.getName();
    public static final String RECEIVER_KEY = PACKAGE_NAME + ".KEY";
    private final int KEY_ALTER = 1;

    public String KEY = "";


    private TextView title;
    private TextView snippet;

    private LinearLayout lnlDrink;

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
        lnlDrink = (LinearLayout) findViewById(R.id.lnlDrink);


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
    protected void onResume() {
        super.onResume();
        loadDrink();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Add Drink");
        menu.add(1, 2, 2, "Add Avaliable");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent i = new Intent(EstabActivity.this, NewDrink.class);
            i.putExtra(NewDrink.KEY_ESTAB, KEY);
            startActivity(i);
        } else if (item.getItemId() == 2) {

        }
        return true;
    }

    private void loadEstab(){

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

    private void loadDrink(){
        lnlDrink.removeAllViews();
        Query query = FirebaseDatabase.getInstance().getReference().child("date").child("drink").child(KEY).orderByChild("name");
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addDrink(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addDrink(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                removeDrink(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addDrink(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }

    private void addDrink(final DataSnapshot dataSnapshot){
        removeDrink(dataSnapshot);

        final DrinkModel drinkModel = new DrinkModel();
        drinkModel.toDrink(dataSnapshot);

        LayoutInflater factory = LayoutInflater.from(EstabActivity.this);
        View viewDrink =  factory.inflate(R.layout.adapter_estab_drink, null);

        viewDrink.setTag(dataSnapshot.getKey());

        TextView txtDrinkName1 = (TextView)viewDrink.findViewById(R.id.txtDrinkName1);
        TextView txtDrinkPrice1 = (TextView)viewDrink.findViewById(R.id.txtDrinkPrice1);

        txtDrinkName1.setText(drinkModel.name);
        txtDrinkPrice1.setText("R$ " + drinkModel.price);

        viewDrink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EstabActivity.this, NewDrink.class);
                i.putExtra(NewDrink.KEY_ESTAB, KEY);
                i.putExtra(NewDrink.KEY_DRINK, dataSnapshot.getKey());
                i.putExtra(NewDrink.KEY_MODEL, drinkModel);
                startActivity(i);
            }
        });

        lnlDrink.addView(viewDrink);
    }

    private void removeDrink(DataSnapshot dataSnapshot){
        for (int i = 0; i > lnlDrink.getChildCount(); i++){
            View view = lnlDrink.getChildAt(i);
            if (view.getTag().equals(dataSnapshot.getKey())){
                lnlDrink.removeView(view);
            }
        }
    }

}
