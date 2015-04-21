package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.helpers.ImageHelper;
import com.codepath.shareyourtable.helpers.RequestCodes;


/**
 * Created by nsamant on 3/31/15.
 */
public class ChooseImageFragment extends DialogFragment {

    private TextView tvCamera;
    private TextView tvGallery;
    public String photoFileName = "photo.jpg";

    public ChooseImageFragment() {

    }

    public static ChooseImageFragment newInstance(String title) {
        ChooseImageFragment imgFrag = new ChooseImageFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        imgFrag.setArguments(args);
        return imgFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);
        tvCamera = (TextView) view.findViewById(R.id.tvCamera);
        tvGallery = (TextView) view.findViewById(R.id.tvGallery);
        String title = getArguments().getString("title", "Choose a profile picture");
        getDialog().setTitle(title);

      /*  PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(MediaStore.ACTION_IMAGE_CAPTURE)) {
            tvCamera.setVisibility(View.GONE);
        }*/

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ImageHelper.getPhotoFileUri(photoFileName)); // set the image file name
                // Start the image capture intent to take photo

                getActivity().startActivityForResult(cameraIntent, RequestCodes.CAMERA_REQUEST);

            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (photoLibraryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    photoLibraryIntent.setType("image/*");
                    getActivity().startActivityForResult(photoLibraryIntent,
                            RequestCodes.GALLERY_REQUEST);
                }
            }
        });

        return view;
    }
}