
package com.codepath.shareyourtable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.application.ShareYourTableApplication;
import com.codepath.shareyourtable.client.YelpClient;
import com.codepath.shareyourtable.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class YelpListingsAdapter extends ArrayAdapter<Restaurant> {

    private static class ViewHolder {
        private ImageView ivRestaurantImage;
        private ImageView ivRatings;
        private TextView tvRestaurantName;
        private TextView tvRatings;
        private TextView tvAddress;
        private TextView tvNumber;
        //private TextView tvPhoneNumber;
    }

    private Context context;
    private YelpClient client;
    private ArrayList<Restaurant> restaurants;

    public YelpListingsAdapter(Context context, int resource, List<Restaurant> restaurants) {
        super(context, R.layout.restaurant_item, restaurants);
        this.context = context;
        client = ShareYourTableApplication.getRestClient();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Restaurant restaurant = (Restaurant)getItem(position);
       // populateResults();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.restaurant_item, parent, false);

            viewHolder.ivRestaurantImage = (ImageView) convertView
                    .findViewById(R.id.ivRestaurantImage);
            viewHolder.ivRatings = (ImageView) convertView.findViewById(R.id.ivRatings);
            viewHolder.tvRestaurantName = (TextView) convertView
                    .findViewById(R.id.tvRestaurantName);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.tvRatings = (TextView) convertView.findViewById(R.id.tvRatings);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            //viewHolder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvRestaurantName.setText(restaurant.getName());
        viewHolder.tvRatings.setText(Integer.toString(restaurant.getRating()) + 
                " " + context.getResources().getString(R.string.reviews));
        //viewHolder.tvPhoneNumber.setText("Phone number " + restaurant.getPhoneNumber() + "&&");
        String address = restaurant.getLocation().replace("[", "").replace("]", "").replace("\"", "").replace("," , " ");
        viewHolder.tvAddress.setText(address);
        viewHolder.ivRestaurantImage.setImageResource(android.R.color.transparent);
        viewHolder.tvNumber.setText(String.valueOf(position+1)+".");
       // viewHolder.userProfilePic.setImageResource(0);

        Picasso.with(getContext()).load(restaurant.getImage())
                .into(viewHolder.ivRestaurantImage);
        viewHolder.ivRatings.setImageResource(android.R.color.transparent);
        // viewHolder.userProfilePic.setImageResource(0);

         Picasso.with(getContext()).load(restaurant.getRatingImage())
                 .into(viewHolder.ivRatings);
        
        return convertView;
    }
}
