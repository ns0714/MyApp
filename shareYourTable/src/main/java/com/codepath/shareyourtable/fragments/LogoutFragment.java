package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.HomeActivity;
import com.parse.ParseUser;

public class LogoutFragment extends android.support.v4.app.Fragment {

        public LogoutFragment() {
            ParseUser.logOut();
            //Intent i = new Intent(getActivity(), HomeActivity.class);
            //getActivity().startActivity(i);
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent i = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(i);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

}
