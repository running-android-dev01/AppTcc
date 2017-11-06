package com.example.igor.apptcc.estabelecimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.controller.EstabelecimentoController;
import com.example.igor.apptcc.listagem.ListagemActivity;
import com.example.igor.apptcc.modelDb.Estabelecimento;
import com.example.igor.apptcc.service.FetchAddressIntentService;
import com.example.igor.apptcc.utils.FetchAddressIntentUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapaAlterarEstabelecimentoActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapaEstabelecimentoActivity.class.getSimpleName();

    private static final String PACKAGE_NAME = MapaEstabelecimentoActivity.class.getName();

    public static final String PARAM_NOME = PACKAGE_NAME + ".NOME";
    public static final String PARAM_ENDERECO = PACKAGE_NAME + ".ENDERECO";
    public static final String PARAM_COMPLEMENTO = PACKAGE_NAME + ".COMPLEMENTO";
    public static final String PARAM_NUMERO = PACKAGE_NAME + ".NUMERO";
    public static final String PARAM_BAIRRO = PACKAGE_NAME + ".BAIRRO";
    public static final String PARAM_CIDADE = PACKAGE_NAME + ".CIDADE";
    public static final String PARAM_UF = PACKAGE_NAME + ".UF";
    public static final String PARAM_PAIS = PACKAGE_NAME + ".PAIS";
    public static final String PARAM_ENDERECO_COMPLETO = PACKAGE_NAME + ".ENDERECO_COMPLETO";
    public static final String PARAM_LATITUDE = PACKAGE_NAME + ".LATITUDE";
    public static final String PARAM_LONGITUDE = PACKAGE_NAME + ".LONGITUDE";

    private String nome = "";
    private String enderecoCompleto = "";
    private double latitude = 0.0;
    private double longitude = 0.0;

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private Button btn_salvar;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_alterar_estabelecimento);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        nome =  getIntent().getExtras().getString(PARAM_NOME, "");
        enderecoCompleto = getIntent().getExtras().getString(PARAM_ENDERECO_COMPLETO, "");
        latitude = getIntent().getExtras().getDouble(PARAM_LATITUDE, 0.0);
        longitude = getIntent().getExtras().getDouble(PARAM_LONGITUDE, 0.0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_salvar = findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(clickSalvar);
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
                .title(nome)
                .snippet(enderecoCompleto)
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
            new AlertDialog.Builder(MapaAlterarEstabelecimentoActivity.this)
                    .setMessage("Confirma que a posição esta correta?")
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LocationsResultReceiver mResultReceiverLocation = new LocationsResultReceiver(new Handler());

                            Location location = new Location("");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);

                            Intent intent = new Intent(MapaAlterarEstabelecimentoActivity.this, FetchAddressIntentService.class);
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
            String pThoroughfare = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_THOROUGHFARE);
            String pSubThoroughfare = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_SUBTHOROUGHFARE);

            String pState = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_STATE);
            String pSubLocality = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_SUB_LOCALITY);
            String pLocality = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_LOCALITY);
            String pCountryName = resultData.getString(FetchAddressIntentUtils.RESULT_DATA_COUNTRYNAME);

            Estabelecimento estabelecimento = new Estabelecimento();
            estabelecimento.nome = nome.toUpperCase().trim();
            estabelecimento.endereco = pThoroughfare.toUpperCase().trim();
            estabelecimento.numero = pSubThoroughfare.toUpperCase().trim();
            estabelecimento.bairro = pSubLocality.toUpperCase().trim();
            estabelecimento.cidade = pLocality.toUpperCase().trim();
            estabelecimento.uf = pState.toUpperCase().trim();
            estabelecimento.pais = pCountryName.toUpperCase().trim();
            estabelecimento.enderecoCompleto = pAddress.toUpperCase().trim();
            estabelecimento.latitude = latitude;
            estabelecimento.longitude = longitude;

            //EstabelecimentoController controller = new EstabelecimentoController();
            //controller.novoEstab(estabelecimento);

            //Intent intent = new Intent(getApplicationContext(), ListagemActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
        }
    }
}
