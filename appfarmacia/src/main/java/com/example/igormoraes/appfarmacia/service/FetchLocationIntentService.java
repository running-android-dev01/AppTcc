package com.example.igormoraes.appfarmacia.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.igormoraes.appfarmacia.R;
import com.example.igormoraes.appfarmacia.utils.FetchLocationIntentUtils;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchLocationIntentService extends IntentService {
    private static final String TAG = FetchAddressIntentService.class.getName();
    private ResultReceiver mReceiver;

    public FetchLocationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(FetchLocationIntentUtils.RECEIVER);

        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        String address =  intent.getStringExtra(FetchLocationIntentUtils.LOCATION_DATA_EXTRA);


        if (address == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(FetchLocationIntentUtils.FAILURE_RESULT, 0.0, 0.0, "", "", "", "", "");
            return;
        }


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(address, 1);

        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "address = " + address , illegalArgumentException);
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(FetchLocationIntentUtils.FAILURE_RESULT, 0.0, 0.0, "", "", "", "", "");
        } else {
            Address location = addresses.get(0);


            double _latitude = location.getLatitude();
            double _longitude = location.getLongitude();
            String _address = location.getAddressLine(0);
            String _locality = location.getLocality();
            String _postalCode = location.getPostalCode();
            String _countryCode = location.getCountryCode();
            String _countryName = location.getCountryName();

            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(FetchLocationIntentUtils.SUCCESS_RESULT, _latitude, _longitude, _address, _locality, _postalCode, _countryCode, _countryName);
        }
    }

    private void deliverResultToReceiver(int resultCode, double latitude, double longitude, String address, String locality, String postalCode, String countryCode, String countryName) {
        Bundle bundle = new Bundle();
        bundle.putDouble(FetchLocationIntentUtils.RESULT_DATA_LATITUDE, latitude);
        bundle.putDouble(FetchLocationIntentUtils.RESULT_DATA_LONGITUDE, longitude);

        bundle.putString(FetchLocationIntentUtils.RESULT_DATA_ADDRESS, address);
        bundle.putString(FetchLocationIntentUtils.RESULT_DATA_LOCALITY, locality);
        bundle.putString(FetchLocationIntentUtils.RESULT_DATA_POSTALCODE, postalCode);
        bundle.putString(FetchLocationIntentUtils.RESULT_DATA_COUNTRYCODE, countryCode);
        bundle.putString(FetchLocationIntentUtils.RESULT_DATA_COUNTRYNAME, countryName);

        mReceiver.send(resultCode, bundle);
    }
}
