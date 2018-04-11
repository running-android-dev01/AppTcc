package com.example.igormoraes.appbar.estabelecimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.igormoraes.appbar.R;
import com.example.igormoraes.appbar.service.FetchAddressIntentService;
import com.example.igormoraes.appbar.utils.FetchAddressIntentUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaEditarEstabelecimentoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapaEditarEstabelecimentoActivity.class.getName();

    private static final String PACKAGE_NAME = MapaEditarEstabelecimentoActivity.class.getName();
    public static final String PARAM_LATITUDE = PACKAGE_NAME + ".LATITUDE";
    public static final String PARAM_LONGITUDE = PACKAGE_NAME + ".LONGITUDE";
    public static final String PARAM_ENDERECO = PACKAGE_NAME + ".ENDERECO";

    private double latitude;
    private double longitude;

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final String KEY_CAMERA_POSITION = "camera_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_editar_estabelecimento);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        latitude = getIntent().getExtras().getDouble(PARAM_LATITUDE, 0.0);
        longitude = getIntent().getExtras().getDouble(PARAM_LONGITUDE, 0.0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(clickSalvar);


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            mCameraPosition = mMap.getCameraPosition();

            outState.putParcelable(KEY_CAMERA_POSITION, mCameraPosition);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                LatLng latLng = arg0.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                Log.i("System out", "onMarkerDrag...");
            }
        });

        updateLocationUI();
        getDeviceLocation();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .draggable(true)
        );
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else  {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude,
                            longitude), DEFAULT_ZOOM));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private View.OnClickListener clickSalvar = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MapaEditarEstabelecimentoActivity.this)
                    .setMessage("Confirma que a posição esta correta?")
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LocationsResultReceiver mResultReceiverLocation = new LocationsResultReceiver(new Handler());

                            Location location = new Location("");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);

                            Intent intent = new Intent(MapaEditarEstabelecimentoActivity.this, FetchAddressIntentService.class);
                            intent.putExtra(FetchAddressIntentUtils.RECEIVER, mResultReceiverLocation);
                            intent.putExtra(FetchAddressIntentUtils.LOCATION_DATA_EXTRA, location);
                            startService(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    };

    private class LocationsResultReceiver extends ResultReceiver {
        LocationsResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String pAddress = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_ADDRESS);

            Intent i = getIntent();
            i.putExtra(PARAM_ENDERECO, pAddress.toUpperCase().trim());
            i.putExtra(PARAM_LATITUDE, latitude);
            i.putExtra(PARAM_LONGITUDE, longitude);

            setResult(RESULT_OK, i);
            finish();
        }
    }
}
