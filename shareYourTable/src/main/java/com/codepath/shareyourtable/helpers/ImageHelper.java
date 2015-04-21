package com.codepath.shareyourtable.helpers;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by nsamant on 4/4/15.
 */
public class ImageHelper {

    public static final String APP_TAG = "ShareYourTable";
    public static Uri getPhotoFileUri(String photoFileName){

        System.out.print("I WANT THE IMAGE NAME "+ photoFileName);
        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + photoFileName));
    }
}
