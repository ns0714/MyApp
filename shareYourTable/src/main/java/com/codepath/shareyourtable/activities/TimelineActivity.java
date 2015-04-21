
package com.codepath.shareyourtable.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.FragmentNavigationDrawer;
import com.codepath.shareyourtable.fragments.TimelineFragment;
import com.codepath.shareyourtable.helpers.NavigationDrawerHelper;

public class TimelineActivity extends ActionBarActivity {
    
    private TimelineFragment timelineFrag;
    private FragmentNavigationDrawer dlDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Share Your Table");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), toolbar,
                R.layout.drawer_nav_item, R.id.frTimeline);
        // Add nav items
        NavigationDrawerHelper.getNavigationDrawer(dlDrawer);

        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }
        //Initialize Timeline Fragment
        initializeTimelineFragment();
    }

    public void initializeTimelineFragment() {
        timelineFrag = TimelineFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frTimeline, timelineFrag);
        ft.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        inflater.inflate(R.menu.event, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch(item.getItemId()){
           case R.id.profile:
               openProfilePage();
               return true;
           case R.id.event:
               openEventPage();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openEventPage() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }
}
