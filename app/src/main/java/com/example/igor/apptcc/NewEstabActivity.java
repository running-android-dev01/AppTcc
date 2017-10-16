package com.example.igor.apptcc;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.igor.apptcc.controller.NewEstabController;
import com.example.igor.apptcc.service.FetchAddressIntentService;
import com.example.igor.apptcc.service.FetchLocationIntentService;
import com.example.igor.apptcc.utils.FetchAddressIntentUtils;
import com.example.igor.apptcc.utils.FetchLocationIntentUtils;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewEstabActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private static final String TAG = NewEstabActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    private Location mLastLocation;
    //private boolean mAddressRequested;
    private String address;
    private String locality;
    private String postalCode;
    private String countryCode;
    private String countryName;

    private double latitude;
    private double longitude;

    private AddressResultReceiver mResultReceiver;
    private LocationsResultReceiver mResultReceiverLocation;

    private FusedLocationProviderClient mFusedLocationClient;

    EditText input_name;
    EditText input_address;
    EditText input_locality;
    EditText input_postalCode;
    EditText input_countryName;
    AppCompatButton btn_create_estabelishment;
    TextView link_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);

        input_name = findViewById(R.id.input_name);
        input_address = findViewById(R.id.input_address);
        input_locality = findViewById(R.id.input_locality);
        input_postalCode = findViewById(R.id.input_postalCode);
        input_countryName = findViewById(R.id.input_countryName);
        btn_create_estabelishment = findViewById(R.id.btn_create_estabelishment);
        link_cancel = findViewById(R.id.link_cancel);

        btn_create_estabelishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEstabelishment();
            }
        });

        link_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadAdrres();

        address = "";
        locality = "";
        postalCode = "";
        countryCode = "";
        countryName = "";
    }


    private void loadAdrres(){
        mResultReceiver = new AddressResultReceiver(new Handler());

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;
                                startIntentService();
                                return;
                            }
                        }
                    });
        }
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentUtils.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentUtils.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //mAddressOutput = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_KEY);

            address = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_ADDRESS);
            locality = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_LOCALITY);
            postalCode = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_POSTALCODE);
            countryCode = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_COUNTRYCODE);
            countryName = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_COUNTRYNAME);

            loadDate();
        }
    }

    private void loadDate(){
        input_address.setText(address);
        input_locality.setText(locality);
        input_postalCode.setText(postalCode);
        input_countryName.setText(countryName);
    }

    private void createEstabelishment(){
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startIntentServiceLocation();
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(NewEstabActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(NewEstabActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAdrres();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }



    private void startIntentServiceLocation() {
        mResultReceiverLocation = new LocationsResultReceiver(new Handler());
        String _address = input_address.getText().toString();

        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(FetchLocationIntentUtils.RECEIVER, mResultReceiverLocation);
        intent.putExtra(FetchLocationIntentUtils.LOCATION_DATA_EXTRA, _address);
        startService(intent);
    }

    private class LocationsResultReceiver extends ResultReceiver {
        LocationsResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String name = input_name.getText().toString();

            latitude = resultData.getDouble(FetchLocationIntentUtils.RESULT_DATA_LATITUDE);
            longitude = resultData.getDouble(FetchLocationIntentUtils.RESULT_DATA_LONGITUDE);

            address = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_ADDRESS);
            locality = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_LOCALITY);
            postalCode = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_POSTALCODE);
            countryCode = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_COUNTRYCODE);
            countryName = resultData.getString(FetchLocationIntentUtils.RESULT_DATA_COUNTRYNAME);

            loadDate();

            NewEstabController.saveEstab(mDatabase, NewEstabActivity.this, name, latitude, longitude, address, locality, postalCode, countryCode, countryName);

            AlertDialog.Builder alert = new AlertDialog.Builder(NewEstabActivity.this);
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

}

