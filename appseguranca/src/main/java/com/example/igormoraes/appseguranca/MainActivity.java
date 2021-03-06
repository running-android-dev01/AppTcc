package com.example.igormoraes.appseguranca;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.igormoraes.appseguranca.controller.ControllerEstabelecimento;
import com.example.igormoraes.appseguranca.estabelecimento.EditarEstabelecimentoActivity;
import com.example.igormoraes.appseguranca.model.Ocorrencia;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static String TAG = MainActivity.class.getName();
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private CameraPosition mCameraPosition;
    private Location mLastKnownLocation;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_NO_MAP = 2;
    private boolean mLocationPermissionGranted;

    private FirebaseAuth mAuth;

    private HashMap<String, Marker> hasMarker = new HashMap<>();
    //private ControllerMapa controllerMapa;


    private Intent mDownloadsFirebaseServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            Intent intent = new Intent(getApplicationContext(), com.example.igormoraes.appseguranca.usuario.LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);


        //ImageView imgFotoUsuario = headerLayout.findViewById(R.id.imgFotoUsuario);
        TextView txtNomeUsuario = headerLayout.findViewById(R.id.txtNomeUsuario);
        TextView txtEmailUsuario = headerLayout.findViewById(R.id.txtEmailUsuario);
        TextView txtVersaoUsuario = headerLayout.findViewById(R.id.txtVersaoUsuario);

        txtNomeUsuario.setText(currentUser.getDisplayName());
        txtEmailUsuario.setText(currentUser.getEmail());
        txtVersaoUsuario.setText(BuildConfig.VERSION_NAME);

        //imgFotoUsuario.setOnClickListener(clickUsuario);

        //Button buttonProduto = findViewById(R.id.buttonProduto);
        //LinearLayout buttonEstab = findViewById(R.id.buttonEstab);

        //buttonProduto.setOnClickListener(clickProduto);
        //buttonEstab.setOnClickListener(clickEstabelecimento);



        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        //controllerMapa = new ControllerMapa();

        DownloadsFirebaseService mDownloadsFirebaseService = new DownloadsFirebaseService();
        mDownloadsFirebaseServiceIntent = new Intent(this, mDownloadsFirebaseService.getClass());
        if (!isMyServiceRunning(mDownloadsFirebaseService.getClass())) {
            startService(mDownloadsFirebaseServiceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(mDownloadsFirebaseServiceIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        double latitude = mLastKnownLocation.getLatitude();
        double longitude = mLastKnownLocation.getLongitude();

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menu_estabelecimento_novo:

                Intent iEstabNovo = new Intent(MainActivity.this, EditarEstabelecimentoActivity.class);
                iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_ID, "");
                iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_LATITUDE, latitude);
                iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_LONGITUDE, longitude);

                startActivity(iEstabNovo);
                break;
            case R.id.menu_sair:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), com.example.igormoraes.appseguranca.usuario.LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient!=null) mGoogleApiClient.connect();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient!=null) mGoogleApiClient.disconnect();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            mCameraPosition = mMap.getCameraPosition();

            outState.putParcelable(KEY_CAMERA_POSITION, mCameraPosition);
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_sair) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), com.example.igormoraes.appseguranca.usuario.LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //private View.OnClickListener clickUsuario = v -> Toast.makeText(MainActivity.this, "Click usuario", Toast.LENGTH_LONG).show();


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "  + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());


                return infoWindow;
            }
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            String key = (String) marker.getTag();

            double latitude = mLastKnownLocation.getLatitude();
            double longitude = mLastKnownLocation.getLongitude();

            Intent iEstabNovo = new Intent(MainActivity.this, EditarEstabelecimentoActivity.class);
            iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_ID, key);
            iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_LATITUDE, latitude);
            iEstabNovo.putExtra(EditarEstabelecimentoActivity.PARAM_LONGITUDE, longitude);

            startActivity(iEstabNovo);
        });

        updateLocationUI();
        getDeviceLocation();
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
            mLastKnownLocation = null;
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

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        carregarEstab();
        //controllerMapa.atualizarMapa(iControllerMapa, MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            mLocationPermissionGranted = false;
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
                        updateLocationUI();
                        getDeviceLocation();
                    }
                }
            }
            //updateLocationUI();
        }
    }

    private void carregarEstab(){
        try{
            if (mMap!=null){
                Dao<Ocorrencia, Integer> estabelecimentoDao = ((AppTccAplication)this.getApplicationContext()).getHelper().getEstabelecimentoDao();
                List<Ocorrencia> lEstabelecimento = estabelecimentoDao.queryForAll();
                for (Ocorrencia estabelecimento: lEstabelecimento) {
                    Marker m = hasMarker.get(estabelecimento.id);
                    if (m!=null){
                        m.remove();
                        hasMarker.remove(estabelecimento.id);
                    }

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(estabelecimento.latitude, estabelecimento.longitude))
                            .title(estabelecimento.ocorrencia)
                            .snippet(estabelecimento.endereco));

                    marker.setTag(estabelecimento.id);
                    hasMarker.put(estabelecimento.id, marker);
                }
            }
        }catch (SQLException ex){
            Log.e(TAG, "ERRO = ", ex);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(ControllerEstabelecimento.KEY_NEW_ESTAB)) {
            carregarEstab();
        }else if (key.equals(ControllerEstabelecimento.KEY_DELL_ESTAB)) {
            String id = ControllerEstabelecimento.getDellEstab(getApplicationContext());

            Marker marker = hasMarker.get(id);
            if (marker!=null){
                marker.remove();
                hasMarker.remove(id);
            }
        }
    }
}
