
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.ProfileActivity;
import com.codepath.shareyourtable.activities.TimelineActivity;
import com.codepath.shareyourtable.model.Profile;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LoginFragment extends Fragment {

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;

    public static LoginFragment newInstance() {
        LoginFragment loginfragment = new LoginFragment();
        return loginfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Typeface tfHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helveticaneue-webfont.ttf");
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setTypeface(tfHelvetica);
        etUsername = (EditText) view.findViewById(R.id.etEmail);
        etUsername.setTypeface(tfHelvetica);
        etUsername.setPadding(15,10,10,10);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPassword.setTypeface(tfHelvetica);
        etPassword.setPadding(15,10,10,10);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        login();
    }

    public void login() {

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(
                            R.string.missing_fields), Toast.LENGTH_SHORT).show();
                } else {
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {

                                ParseQuery<Profile> query = ParseQuery
                                        .getQuery(Profile.class);
                                query.whereEqualTo("username", ParseUser.getCurrentUser()
                                        .getUsername());

                                query.findInBackground(new FindCallback<Profile>() {
                                    public void done(List<Profile> profiles, ParseException e) {
                                        if (profiles.size() > 0 && e == null) {
                                            Intent timeLineIntent = new Intent(getActivity(),
                                                    TimelineActivity.class);
                                            startActivity(timeLineIntent);
                                        } else {
                                            Intent profileIntent = new Intent(getActivity(),
                                                    ProfileActivity.class);
                                            startActivity(profileIntent);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(),
                                        getActivity().getResources().getString(R.string.login_failed),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }
}
