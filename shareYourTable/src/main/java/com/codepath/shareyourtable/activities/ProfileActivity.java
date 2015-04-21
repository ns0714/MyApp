
package com.codepath.shareyourtable.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.fragments.CreateProfileFragment;
import com.codepath.shareyourtable.fragments.MyProfileFragment;
import com.codepath.shareyourtable.helpers.ImageHelper;
import com.codepath.shareyourtable.helpers.RequestCodes;
import com.codepath.shareyourtable.model.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ProfileActivity extends ActionBarActivity {

    private CreateProfileFragment createProfileFragment;
    private MyProfileFragment myProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //Check if user has created a profile
        ParseQuery<Profile> query = ParseQuery
                .getQuery(Profile.class);
        query.whereEqualTo("username", ParseUser.getCurrentUser()
                .getUsername());

        query.findInBackground(new FindCallback<Profile>() {
            public void done(List<Profile> profiles, ParseException e) {
                if (profiles.size() > 0 && e == null) {
                    //if the profile for username is found then open the profile
                    initializeMyProfileFragment();
                } else {
                    //if not then ask the user to create a profile
                    initializeCreateProfileFragment();
                }
            }
        });
    }

    public void initializeCreateProfileFragment() {
        createProfileFragment = CreateProfileFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frProfile, createProfileFragment);
        ft.commit();
    }

    public void initializeMyProfileFragment() {
        myProfileFragment = MyProfileFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frProfile, myProfileFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        inflater.inflate(R.menu.event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                openSearch();
                return true;
            case R.id.event:
                openCreateEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearch() {
        Intent intent = new Intent(getApplicationContext(), YelpActivity.class);
        startActivity(intent);
    }

    private void openCreateEvent() {
        Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
        startActivity(intent);
    }

    //Called from CreateProfile Fragment and my profile fragment to set the profile image.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap selectedImage;
        //called when user is selecting picture from Gallery/Photos
        if (resultCode == RESULT_OK && requestCode == RequestCodes.GALLERY_REQUEST) {
            if (data != null) {
                Uri photoUri = data.getData();

                System.out.print("I WANT THE IMAGE NAME "+ photoUri);
                // Do something with the photo based on Uri
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), photoUri);
                    System.out.print("i want bitmap " + selectedImage);
                    if(selectedImage != null) {

                        if(myProfileFragment != null) {
                            myProfileFragment.setImage(selectedImage);
                        }else if(createProfileFragment !=null){
                            createProfileFragment.setImage(selectedImage);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //called when user is using Camera to click picture
        }else if(resultCode == RESULT_OK && requestCode == RequestCodes.CAMERA_REQUEST){
            Uri takenPhotoUri = ImageHelper.getPhotoFileUri("photo.jpg");
            // by this point we have the camera photo on disk
            Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

            // Load the taken image into a preview
            if(takenImage != null) {

                if(myProfileFragment != null) {
                    myProfileFragment.setImage(takenImage);
                }else if(createProfileFragment !=null){
                    createProfileFragment.setImage(takenImage);
                }
            }
        }
    }
}
