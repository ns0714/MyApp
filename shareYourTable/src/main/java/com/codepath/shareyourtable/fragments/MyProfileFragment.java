
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.TimelineActivity;
import com.codepath.shareyourtable.helpers.ImageResizing;
import com.codepath.shareyourtable.model.Profile;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MyProfileFragment extends Fragment {
    
    private ImageView ivProfileImg;
    private RadioButton rbAge18;
    private RadioButton rbAge30;
    private RadioButton rbAge50;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private CheckBox cbDating;
    private CheckBox cbHangout;
    private EditText etInterests;
    private EditText etAboutMe;
    private FloatingActionButton btnSave;
    private ObservableScrollView scProfile;

    private String age18;
    private String age30;
    private String age50;
    private String male;
    private ParseFile file;
    private Bitmap selectedImage;
    private Profile currProfile;

    public final String APP_TAG = "ShareYourTable";

    public static MyProfileFragment newInstance() {
        MyProfileFragment myProfileFrag = new MyProfileFragment();
        Bundle args = new Bundle();
        myProfileFrag.setArguments(args);
        return myProfileFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ivProfileImg = (ImageView)view.findViewById(R.id.ivProfileImage);
        rbAge18 = (RadioButton) view.findViewById(R.id.rbAge18);
        rbAge30 = (RadioButton) view.findViewById(R.id.rbAge30);
        rbAge50 = (RadioButton) view.findViewById(R.id.rbAge50);
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        cbDating = (CheckBox) view.findViewById(R.id.cbDating);
        cbHangout = (CheckBox) view.findViewById(R.id.cbHangout);
        etInterests = (EditText) view.findViewById(R.id.etInterests);
        etAboutMe = (EditText) view.findViewById(R.id.etAbout);
        btnSave = (FloatingActionButton) view.findViewById(R.id.btnSave);
        scProfile = (ObservableScrollView) view.findViewById(R.id.scProfile);

        btnSave.attachToScrollView(scProfile);

        age18 = getActivity().getResources().getString(R.string.age_group_18);
        age30 = getActivity().getResources().getString(R.string.age_group_30);
        age50 = getActivity().getResources().getString(R.string.age_group_50);
        male = getActivity().getResources().getString(R.string.male);


        // Specify which class to query
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        // Specify the object id
        String objectId = ParseUser.getCurrentUser().getObjectId();
        query.whereEqualTo("userObj", objectId);

        query.findInBackground(new FindCallback<Profile>() {
            public void done(List<Profile> profiles, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    currProfile = profiles.get(0);
                    file = currProfile.getParseFile("profilePicture");
                    try {
                        Bitmap bm = BitmapFactory.decodeByteArray(file.getData(), 0, file.getData().length);
                        ivProfileImg.setImageBitmap(bm);

                        int age =currProfile.getAgeGroup();
                        if(getAgeGroup(age) == age18){
                            rbAge18.setChecked(true);
                        }else if(getAgeGroup(age) == age30){
                            rbAge30.setChecked(true);
                        }else if(getAgeGroup(age) == age50){
                            rbAge50.setChecked(true);
                        }

                        if(currProfile.getGender() == male){
                            rbMale.setChecked(true);
                        }else {
                            rbFemale.setChecked(true);
                        }

                        if(currProfile.getEventType() == 1){
                            cbDating.setSelected(true);
                            cbHangout.setSelected(true);
                        }else if(currProfile.getEventType() == 2){
                            cbDating.setSelected(true);
                        }else if(currProfile.getEventType() == 3){
                            cbHangout.setSelected(true);
                        }

                        etInterests.setText(currProfile.getInterests());
                        etAboutMe.setText(currProfile.getAboutMe());

                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });

        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser username = ParseUser.getCurrentUser();
                //getFullName(username.getUsername());
                currProfile.setInterests(etInterests.getText().toString());
                currProfile.setAboutMe(etAboutMe.getText().toString());
                currProfile.setUsername(username.getUsername());
                currProfile.setUserObj(ParseUser.getCurrentUser().getObjectId());

                if(file != null){
                    currProfile.setProfilePicture(file);
                }else {
                    ParseFile imgFile = getImageFile();
                    if (imgFile != null) {
                        currProfile.setProfilePicture(imgFile);
                    }
                }
                if (rbAge18.isChecked()) {
                    currProfile.setAgeGroup(1);
                } else if (rbAge30.isChecked()) {
                    currProfile.setAgeGroup(2);
                } else if (rbAge50.isChecked()) {
                    currProfile.setAgeGroup(3);
                }

                if (rbMale.isChecked()) {
                    currProfile.setGender(getResources().getString(R.string.male));
                } else if (rbFemale.isChecked()) {
                    currProfile.setGender(getResources().getString(R.string.female));
                }

                if (cbDating.isChecked() && cbHangout.isChecked()) {
                    currProfile.setEventType(1);
                }else if (cbDating.isChecked()) {
                    currProfile.setEventType(2);
                }else if (cbHangout.isChecked()) {
                    currProfile.setEventType(3);
                }
                currProfile.saveInBackground();
                Intent intent = new Intent(getActivity(), TimelineActivity.class);
                startActivity(intent);
            }
            public ParseFile getImageFile() {
                file = new ParseFile(ImageResizing.bitmapToByteArray(selectedImage,
                        Bitmap.CompressFormat.JPEG));
                return file;
            }
        });
        return view;
    }

    private void showAlertDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ChooseImageFragment alertDialog = ChooseImageFragment.newInstance("Pick an action");
        alertDialog.show(fm,"fragment_choose_image");
    }


    public String getAgeGroup(int group){
        if(group == 1){
           return age18;
        }else if(group == 2){
            return age30;
        }else if(group ==3){
            return age50;
        }
        return "";
    }
/*
    public void getFullName(String username){
        ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    System.out.println("@@@@NAME" + objects.get(0));

                } else {

                }
            }
        });
    }*/

    public void setImage(Bitmap image) {
        System.out.print("i want bitmap " + image);
        selectedImage = image;
        ivProfileImg.setImageResource(android.R.color.transparent);
        ivProfileImg.setImageBitmap(selectedImage);
    }

}
