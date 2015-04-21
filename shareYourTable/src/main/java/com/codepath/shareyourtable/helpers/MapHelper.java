package com.codepath.shareyourtable.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Produce;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nsamant on 4/1/15.
 */
public class MapHelper implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String city;
    private Context actContext;
    public MapHelper(){

    }

    public MapHelper(String currCity){
        this.city = currCity;
    }
    public synchronized GoogleApiClient buildGoogleApiClient(Context context) {
        actContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        return mGoogleApiClient;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(actContext, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                String country = addresses.get(0).getAddressLine(2);

                System.out.print("CITYYYY" + city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(city != null) {
            BusProvider.getInstance().register(this);
            BusProvider.getInstance().post(city);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Produce
    public String produceEvent() {
        if (city != null) {
            return city;
        }
        return "";
    }
}
