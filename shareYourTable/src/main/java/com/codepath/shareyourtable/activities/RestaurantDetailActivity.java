
package com.codepath.shareyourtable.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.RestaurantDetailFragment;
import com.codepath.shareyourtable.model.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.location.LocationClient;

public class RestaurantDetailActivity extends ActionBarActivity {

    private RestaurantDetailFragment detailFrag;
    private Restaurant restaurant;
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restuarant_detail);
        restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        initializeDetailFragment();
    }

    public void initializeDetailFragment() {
        detailFrag = RestaurantDetailFragment.newInstance(restaurant);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frDetail, detailFrag);
        ft.commit();
    }
    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

        case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
             */
            switch (resultCode) {
            case Activity.RESULT_OK:
               // mLocationClient.connect();
                break;
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                //ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                //errorFragment.setDialog(errorDialog);
                //errorFragment.show(getSupportFragmentManager(),
                    //    "Location Updates");
            }
            return false;
        }
    }
}
