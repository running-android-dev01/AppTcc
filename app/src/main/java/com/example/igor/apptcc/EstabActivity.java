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

import com.example.igor.apptcc.model.AvaliableModel;
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

import java.util.Locale;

public class EstabActivity extends AppCompatActivity {
    private static final String TAG = EstabActivity.class.getName();
    private static final String PACKAGE_NAME = EstabActivity.class.getName();
    public static final String RECEIVER_KEY = PACKAGE_NAME + ".KEY";
    private final int KEY_ALTER = 1;

    public String KEY = "";


    private TextView title;
    private TextView snippet;

    private LinearLayout lnlDrink;
    private LinearLayout lnlAvaliable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        KEY = getIntent().getExtras().getString(RECEIVER_KEY, "");

        title = (TextView) findViewById(R.id.title);
        snippet = (TextView) findViewById(R.id.snippet);
        lnlDrink = (LinearLayout) findViewById(R.id.lnlDrink);
        lnlAvaliable = (LinearLayout) findViewById(R.id.lnlAvaliable);

        loadEstab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDrink();
        loadAvaliable();
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
            Intent i = new Intent(EstabActivity.this, NewAvaliableActivity.class);
            i.putExtra(NewAvaliableActivity.KEY_ESTAB, KEY);
            startActivity(i);
        }
        return true;
    }

    private void loadEstab(){

        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference("date/establishment/" + KEY);
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                EstabModel estabModel = snapshot.getValue(EstabModel.class);

                title.setText(estabModel.name);
                snippet.setText(estabModel.address + " - " + estabModel.locality + " - " + estabModel.countryName);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    private void loadDrink(){
        lnlDrink.removeAllViews();
        Query query = FirebaseDatabase.getInstance().getReference().child("date").child("drink").orderByChild("keyEstab").equalTo(KEY);

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

        TextView txtDrinkName1 = viewDrink.findViewById(R.id.txtDrinkName1);
        TextView txtDrinkPrice1 = viewDrink.findViewById(R.id.txtDrinkPrice1);


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


    private void loadAvaliable(){
        lnlAvaliable.removeAllViews();


        Query query = FirebaseDatabase.getInstance().getReference().child("date").child("establishment").child("avaliable").child(KEY).orderByChild("date");
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addAvaliable(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addAvaliable(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                removeAvaliable(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                addAvaliable(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }

    private void addAvaliable(final DataSnapshot dataSnapshot){
        removeAvaliable(dataSnapshot);

        final AvaliableModel avaliableModel = new AvaliableModel();
        avaliableModel.toAvaliable(dataSnapshot);

        LayoutInflater factory = LayoutInflater.from(EstabActivity.this);
        View viewAvaliable =  factory.inflate(R.layout.adapter_estab_avaliable, null);

        viewAvaliable.setTag(dataSnapshot.getKey());

        TextView txtAvaliableName = (TextView)viewAvaliable.findViewById(R.id.txtAvaliableName);
        TextView txtAvaliableNote = (TextView)viewAvaliable.findViewById(R.id.txtAvaliableNote);
        TextView txtAvaliable = (TextView)viewAvaliable.findViewById(R.id.txtAvaliable);

        txtAvaliableName.setText(avaliableModel.name);
        txtAvaliableNote.setText(String.format(Locale.getDefault(), "%.1f", avaliableModel.avaliable));
        txtAvaliable.setText(avaliableModel.description);


        lnlAvaliable.addView(viewAvaliable);
    }


    private void removeAvaliable(DataSnapshot dataSnapshot){
        for (int i = 0; i > lnlAvaliable.getChildCount(); i++){
            View view = lnlAvaliable.getChildAt(i);
            if (view.getTag().equals(dataSnapshot.getKey())){
                lnlAvaliable.removeView(view);
            }
        }
    }

}
