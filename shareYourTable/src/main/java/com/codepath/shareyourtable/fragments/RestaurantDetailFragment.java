
package com.codepath.shareyourtable.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.CreateEventActivity;
import com.codepath.shareyourtable.helpers.AddressHelper;
import com.codepath.shareyourtable.helpers.MapHelper;
import com.codepath.shareyourtable.helpers.RequestCodes;
import com.codepath.shareyourtable.model.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class RestaurantDetailFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView tvRestaurantName;
    private ImageView ivRatings;
    private TextView tvReviews;
    private TextView tvPhoneNumber;
    private Button btnSelect;
    private ImageView ivBackgroundImg;

    private static Restaurant restaurantDetails;
    private MapHelper mapHelper;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LatLng srcLatLng;
    private String srcLatitude;
    private String srcLongitude;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static RestaurantDetailFragment newInstance(Restaurant restaurant) {
        RestaurantDetailFragment restFrag = new RestaurantDetailFragment();
        restaurantDetails = restaurant;
        return restFrag;
    }

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (errorCode != ConnectionResult.SUCCESS)
        {
            if (DEBUG) {
                Log.d(TAG, "errorCode = " + errorCode);}
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this,
                    1, new DialogCancelListener());
            errorDialog.show();
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
        ivRatings = (ImageView) view.findViewById(R.id.ivRatings);
        tvReviews = (TextView) view.findViewById(R.id.tvRatings);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);
        ivBackgroundImg = (ImageView) view.findViewById(R.id.ivBackgroundImg);
        loadUI();
        btnSelect.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                        intent.putExtra("restaurantName", restaurantDetails.getName());
                        intent.putExtra("restaurantAddress", restaurantDetails.getLocation());
                        startActivityForResult(intent, RequestCodes.EVENT_REQUEST);
                    }
                });
        return view;
    }

    private void loadUI() {
        tvRestaurantName.setText(restaurantDetails.getName());
        tvReviews.setText(Integer.toString(restaurantDetails.getRating()) + " " +
                getResources().getString(R.string.reviews));
        Picasso.with(getActivity()).load(restaurantDetails.getRatingImage())
                .into(ivRatings);

        Picasso.with(getActivity()).load(restaurantDetails.getImage()).into(ivBackgroundImg);
        ivBackgroundImg.setAlpha(30);
        tvPhoneNumber.setText(restaurantDetails.getPhoneNumber());

        tvPhoneNumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + restaurantDetails.getPhoneNumber()));
                    startActivity(callIntent);
            }
        });
        loadMap();
    }

    public void loadMap() {
        mLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3, 1, mLocationListener);
        // mLocationClient = new LocationClient(getActivity(), this, this);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            map = mapFragment.getMap();
            if (map != null) {
                // Toast.makeText(this, "Map Fragment was loaded properly!",
                // Toast.LENGTH_SHORT).show();
                map.setMyLocationEnabled(true);
            } else {
                Toast.makeText(getActivity(), "Error - Map was null!!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

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
                connectionResult.startResolutionForResult(getActivity(),
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
            Toast.makeText(getActivity(),
                    "Sorry. Location services not available to you",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /*
  * Called by Location Services when the request to connect the client
  * finishes successfully. At this point, you can request the current
  * location or start periodic updates
  */
    @Override
    public void onConnected(Bundle connectionHint) {
        LatLng latLng = AddressHelper.getAddress(getActivity(), restaurantDetails.getLocation());
        if (latLng != null) {

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15);
            //map = detailFrag.getMap();
            map.animateCamera(cameraUpdate);
            map.addMarker(new MarkerOptions().position(latLng).title(
                    "Hello world"));
        } else {
            Toast.makeText(getActivity(), "Location was not found!", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location srcLocation) {
            srcLatLng = new LatLng(srcLocation.getLatitude(),
                    srcLocation.getLongitude());
            srcLatitude = Double.toString(srcLatLng.latitude);
            srcLongitude = Double.toString(srcLatLng.longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public GoogleMap getMap() {
        return map;
    }
}
