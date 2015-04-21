
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.ProfileActivity;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;


public class SignUpFragment extends Fragment {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etFirstName;
    private EditText etLastName;
    private Button btnSignUp;
    private LoginButton btnFacebook;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public static SignUpFragment newInstance() {
        SignUpFragment signUpFragment = new SignUpFragment();
        return signUpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        Typeface tfHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helveticaneue-webfont.ttf");
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.setTypeface(tfHelvetica);
        etEmail.setPadding(15,10,10,10);

        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etPassword.setTypeface(tfHelvetica);
        etPassword.setPadding(15,10,10,10);

        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etFirstName.setTypeface(tfHelvetica);
        etFirstName.setPadding(15,10,10,10);

        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etLastName.setTypeface(tfHelvetica);
        etLastName.setPadding(15,10,10,10);

        btnSignUp = (Button) view.findViewById(R.id.btnSignup);
        btnSignUp.setTypeface(tfHelvetica);

        btnFacebook = (LoginButton) view.findViewById(R.id.btnFacebook);
        btnFacebook.setTypeface(tfHelvetica);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signUp();
    }

    public void signUp() {
        btnFacebook.setReadPermissions(Arrays.asList("public_profile"));
        btnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                        }
                    }
                });
            }
        });
        btnSignUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();

                if (email.length() == 0 || password.length() == 0 ||
                        firstName.length() == 0 || lastName.length() == 0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(
                            R.string.missing_fields), Toast.LENGTH_SHORT).show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setPassword(password);
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                                startActivity(profileIntent);
                            } else {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
