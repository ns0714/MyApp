
package com.codepath.shareyourtable.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.helpers.BitMapHelper;
import com.enrique.stackblur.StackBlurManager;

public class HomeFragment extends Fragment {

    private Button btnCreateAcc;
    private Button btnLogin;
    private ImageView ivBackgroundImg;
    private OnItemSelectedListener listener;

    public static HomeFragment newInstance() {
        HomeFragment homefragment = new HomeFragment();
        return homefragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Typeface tfHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helveticaneue-webfont.ttf");
        btnCreateAcc = (Button) view.findViewById(R.id.btnCreateAcc);
        btnCreateAcc.setTypeface(tfHelvetica);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setTypeface(tfHelvetica);
        ivBackgroundImg = (ImageView) view.findViewById(R.id.ivBackgroundImg);
        ivBackgroundImg.setBackground(getBlurImage());
        ivBackgroundImg.setBackground(getBlurImage());
        ivBackgroundImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view;
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        public void onButtonItemSelected(Button btn);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createLoginOptions();
    }

    // Store the listener (activity) that will have events fired once the
    // fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    public void createLoginOptions() {
        btnCreateAcc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onButtonItemSelected(btnCreateAcc);
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onButtonItemSelected(btnLogin);
            }
        });
    }

    public Drawable getBlurImage() {
        StackBlurManager stackBlurManager = new StackBlurManager(BitMapHelper.drawableToBitmap(getActivity().getResources().getDrawable(R.drawable.home_background)));
        stackBlurManager.process(3);
        Drawable drawable = new BitmapDrawable(getResources(), stackBlurManager.returnBlurredImage());
        return drawable;
    }

}
