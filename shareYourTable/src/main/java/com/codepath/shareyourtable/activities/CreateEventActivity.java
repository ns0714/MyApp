
package com.codepath.shareyourtable.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.CreateEventFragment;

public class CreateEventActivity extends ActionBarActivity {

    private CreateEventFragment eventFrag;
    private String restaurant;
    private String restaurantAddress;
    private final String EVENT_FRAGMENT_TAG = "eventFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        restaurant = getIntent().getStringExtra("restaurantName");
        restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            eventFrag = (CreateEventFragment)
                    getSupportFragmentManager().findFragmentByTag(EVENT_FRAGMENT_TAG);
        } else if (eventFrag == null) {
            // only create fragment if they haven't been instantiated already
            initializeEventFragment();
        }
    }

    public void initializeEventFragment() {

        eventFrag = CreateEventFragment.newInstance(restaurant, restaurantAddress);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frEvent, eventFrag, EVENT_FRAGMENT_TAG);
        ft.commit();
    }
}
