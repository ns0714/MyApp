package com.codepath.shareyourtable.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.YelpFragment;
import com.codepath.shareyourtable.helpers.BusProvider;
import com.codepath.shareyourtable.helpers.KeyboardHelper;
import com.codepath.shareyourtable.helpers.MapHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Subscribe;


public class YelpActivity extends FragmentActivity {
    
    private YelpFragment yelpFrag;
    private EditText etFind;
    private EditText etNear;
    private Button btnSearch;
    private static String searchTerm;
    private static String searchLocation;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String city;
    private MapHelper mapHelper;
    private final String YELP_FRAGMENT_TAG = "yelpFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_yelp);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            searchTerm = savedInstanceState.getString("find");
            yelpFrag = (YelpFragment)
                    getSupportFragmentManager().findFragmentByTag(YELP_FRAGMENT_TAG);
        } else if (yelpFrag == null) {
            // only create fragment if they haven't been instantiated already
            setUpView();
        }


        String rest = getIntent().getStringExtra("restaurantName");
    }

    @Subscribe
    public void getCurrentLocation(String currCity) {
        if(currCity !=null) {
            city = currCity;
            etNear.setText(currCity);
            etNear.setTextColor(getResources().getColor(R.color.btn_color_blue));
            //Toast.makeText(this, currCity, Toast.LENGTH_LONG).show();
        }else{
            etNear.setText("");
        }
    }

    public void setUpView() {
        etFind = (EditText)findViewById(R.id.etFind);
        etNear = (EditText)findViewById(R.id.etNear);
        btnSearch = (Button)findViewById(R.id.btnYelp);

        btnSearch.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                searchTerm = etFind.getText().toString();
                searchLocation = etNear.getText().toString();
                KeyboardHelper.hideSoftKeyboard(btnSearch, getBaseContext());
                initializeYelpFragment();
            }
        });
    }

    public void initializeYelpFragment() {
        yelpFrag = YelpFragment.newInstance(searchTerm, searchLocation);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frYelpListings, yelpFrag, YELP_FRAGMENT_TAG);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if(city == null) {
            mapHelper = new MapHelper();
            mGoogleApiClient = mapHelper.buildGoogleApiClient(this);
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}

