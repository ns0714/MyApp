package com.codepath.shareyourtable.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.helpers.AddressHelper;
import com.codepath.shareyourtable.model.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private SupportMapFragment mapFragment;
    private TextView tvList;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Restaurant> restaurants;
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // getActionBar().setDisplayHomeAsUpEnabled(true);

        restaurants = getIntent().getParcelableArrayListExtra("restaurants");

        buildGoogleApiClient();
        mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.frMap);
        if (mapFragment != null) {
            map = mapFragment.getMap();
            if (map != null) {
                Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT)
                        .show();
                map.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        if (isGooglePlayServicesAvailable()) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
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
                 * If the result code is Activity.RESULT_OK, try to connect
                 * again
                 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        for (int i = 0; i < restaurants.size(); i++) {
            final int position = i;
            LatLng latLng = AddressHelper.getAddress(this, restaurants.get(i).getLocation());
            if (latLng != null) {
                // Toast.makeText(this, "Location was found!",
                // Toast.LENGTH_SHORT)
                // .show();
               /* Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
                Bitmap bmp = Bitmap.createBitmap(200, 150, conf); 
                Canvas canvas = new Canvas(bmp);
                Paint paint = new Paint();
                paint.setColor(getResources().getColor(R.color.yelp_red));
                canvas.drawText(String.valueOf(i), 10, 100, paint); // paint defines the text color, stroke width, size
               */
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        latLng, 10);
                // map = detailFrag.getMap();
                map.animateCamera(cameraUpdate);
                map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconr1))
                        //icon(BitmapDescriptorFactory.fromBitmap(bmp)) .anchor(0.5f, 1)
                );
                map.setOnMarkerClickListener(new OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent detail = new Intent(getBaseContext(), RestaurantDetailActivity.class);
                        detail.putExtra("restaurant", restaurants.get(position));
                        startActivity(detail);
                        return false;
                    }
                });
            } else {
               /* Toast.makeText(this, "Location was not found!", Toast.LENGTH_SHORT)
                        .show();*/
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}