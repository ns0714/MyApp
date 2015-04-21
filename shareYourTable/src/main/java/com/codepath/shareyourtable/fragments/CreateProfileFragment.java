
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.TimelineActivity;
import com.codepath.shareyourtable.helpers.ImageResizing;
import com.codepath.shareyourtable.model.Profile;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class CreateProfileFragment extends Fragment {

    private EditText etInterests;
    private EditText etAboutMe;
    private RadioButton rbAge18;
    private RadioButton rbAge30;
    private RadioButton rbAge50;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private CheckBox cbDating;
    private CheckBox cbHangout;
    private ImageView ivProfileImage;
    private Button btnCreateProfile;

    private Bitmap selectedImage;
    private ParseFile file;

    public String photoFileName = "photo.jpg";

    public static CreateProfileFragment newInstance() {
        CreateProfileFragment profileFragment = new CreateProfileFragment();
        return profileFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);
        etInterests = (EditText) view.findViewById(R.id.etInterests);
        etAboutMe = (EditText) view.findViewById(R.id.etAbout);
        rbAge18 = (RadioButton) view.findViewById(R.id.rbAge18);
        rbAge30 = (RadioButton) view.findViewById(R.id.rbAge30);
        rbAge50 = (RadioButton) view.findViewById(R.id.rbAge50);
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        cbDating = (CheckBox) view.findViewById(R.id.cbDating);
        cbHangout = (CheckBox) view.findViewById(R.id.cbHangout);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        btnCreateProfile = (Button) view.findViewById(R.id.btnCreateProfile);

        ivProfileImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        return view;
    }

    public void createProfile() {
        btnCreateProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Profile userProfile = new Profile();
                ParseUser username = ParseUser.getCurrentUser();

                userProfile.setInterests(etInterests.getText().toString());
                userProfile.setAboutMe(etAboutMe.getText().toString());
                userProfile.setUsername(username.getUsername());
                userProfile.setUserObj(ParseUser.getCurrentUser().getObjectId());

                ParseFile imgFile = getImageFile();
                if (imgFile != null) {
                    userProfile.setProfilePicture(imgFile);
                }
                if (rbAge18.isChecked()) {
                    userProfile.setAgeGroup(1);
                } else if (rbAge30.isChecked()) {
                    userProfile.setAgeGroup(2);
                } else if (rbAge50.isChecked()) {
                    userProfile.setAgeGroup(3);
                }

                if (rbMale.isChecked()) {
                    userProfile.setGender(getResources().getString(R.string.male));
                } else if (rbFemale.isChecked()) {
                    userProfile.setGender(getResources().getString(R.string.female));
                }

                if (cbDating.isChecked() && cbHangout.isChecked()) {
                    userProfile.setEventType(1);
                } else if (cbDating.isChecked()) {
                    userProfile.setEventType(2);
                } else if (cbHangout.isChecked()) {
                    userProfile.setEventType(3);
                }
                userProfile.saveInBackground();
                Intent intent = new Intent(getActivity(), TimelineActivity.class);
                startActivity(intent);
            }

            public ParseFile getImageFile() {
                file = new ParseFile(ImageResizing.bitmapToByteArray(selectedImage,
                        Bitmap.CompressFormat.JPEG));
                return file;
            }
        });
    }

    public void setImage(Bitmap image) {
        selectedImage = image;
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setImageBitmap(selectedImage);
    }

    private void showAlertDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ChooseImageFragment alertDialog = ChooseImageFragment.newInstance("Pick an action");
        alertDialog.show(fm, "fragment_choose_image");
    }
}
