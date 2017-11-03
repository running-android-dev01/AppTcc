package com.example.igor.apptcc.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.utils.FetchAddressIntentUtils;
import com.example.igor.apptcc.utils.FetchLocationIntentUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = FetchAddressIntentService.class.getName();
    private ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(FetchAddressIntentUtils.RECEIVER);

        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Location location = intent.getParcelableExtra(FetchAddressIntentUtils.LOCATION_DATA_EXTRA);

        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(FetchAddressIntentUtils.FAILURE_RESULT, "","","","","","", "", "", "", "");
            return;
        }


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(FetchAddressIntentUtils.FAILURE_RESULT, "","","", "","","","", "", "", "");
        } else {
            Address address = addresses.get(0);


            String _address = address.getAddressLine(0);
            String _locality = address.getLocality();
            String _state = address.getAdminArea();
            String _thoroughfare = address.getThoroughfare();
            String _subThoroughfare = address.getSubThoroughfare();
            String _subLocality = address.getSubLocality();
            String _featureName = address.getFeatureName();
            String _postalCode = address.getPostalCode();
            String _countryCode = address.getCountryCode();
            String _countryName = address.getCountryName();

            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(FetchAddressIntentUtils.SUCCESS_RESULT, _address, _thoroughfare, _subThoroughfare, _featureName,_subLocality, _locality, _state, _postalCode, _countryCode, _countryName);
        }
    }

    private void deliverResultToReceiver(int resultCode, String address, String thoroughfare, String subThoroughfare, String featureName, String subLocality, String locality, String _state, String postalCode, String countryCode, String countryName) {
        Bundle bundle = new Bundle();
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_ADDRESS, address);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_THOROUGHFARE, thoroughfare);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_SUBTHOROUGHFARE, subThoroughfare);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_FEATURE_NAME, featureName);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_SUB_LOCALITY, subLocality);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_LOCALITY, locality);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_STATE, _state);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_POSTALCODE, postalCode);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_COUNTRYCODE, countryCode);
        bundle.putString(FetchAddressIntentUtils.RESULT_DATA_COUNTRYNAME, countryName);
        mReceiver.send(resultCode, bundle);
    }
}
