
package com.codepath.shareyourtable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.HomeFragment;
import com.codepath.shareyourtable.fragments.LoginFragment;
import com.codepath.shareyourtable.fragments.SignUpFragment;
import com.codepath.shareyourtable.helpers.NetworkHelper;
import com.parse.ParseUser;

public class HomeActivity extends ActionBarActivity implements
        HomeFragment.OnItemSelectedListener {

    private HomeFragment homeFragment;
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    private ParseUser currentUser;

    private final String HOME_FRAGMENT_TAG = "homeFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currentUser = ParseUser.getCurrentUser();

        /*
        Check if internet is available
         */
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.no_network),
                    Toast.LENGTH_SHORT).show();
        }

        if (currentUser == null) {
            if (savedInstanceState != null) {
                homeFragment = (HomeFragment)
                        getSupportFragmentManager().findFragmentByTag(HOME_FRAGMENT_TAG);
            } else if (homeFragment == null) {
                initializeFragment();
            }
        } else {
            Intent timelineIntent = new Intent(this, TimelineActivity.class);
            startActivity(timelineIntent);
        }
    }

    private void initializeFragment() {
        homeFragment = HomeFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.frHome, new HomeFragment(), HOME_FRAGMENT_TAG);
        ft.addToBackStack(homeFragment.getTag());
        // Execute the changes specified
        ft.commit();
    }

    @Override
    public void onButtonItemSelected(Button button) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        if (button.getId() == R.id.btnLogin) {
            loginFragment = new LoginFragment();
            ft.replace(R.id.frHome, loginFragment);
            ft.addToBackStack(loginFragment.getTag());
        } else if (button.getId() == R.id.btnCreateAcc) {
            signUpFragment = new SignUpFragment();
            ft.replace(R.id.frHome, signUpFragment);
            ft.addToBackStack(signUpFragment.getTag());
        }
        ft.commit();
    }
}
