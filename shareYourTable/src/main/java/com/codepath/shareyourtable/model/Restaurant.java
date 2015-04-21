
package com.codepath.shareyourtable.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Restaurant implements Parcelable {

    private String name;
    private String location;
    private int rating;
    private String image;
    private String ratingImage;
    private String phoneNumber;
    private static ArrayList<String> categories;

    private static final String YELP_NAME = "name";
    private static final String YELP_ADDRESS = "display_address";
    private static final String YELP_LOCATION = "location";
    private static final String YELP_REVIEW_COUNT = "review_count";
    private static final String YELP_RATING_URL= "rating_img_url_large";
    private static final String YELP_IMAGE_URL = "image_url";
    private static final String YELP_PHONE = "display_phone";
    private static final String YELP_BUSINESSES = "businesses";
    private static final String YELP_CATEGORIES = "categories";


    private static ArrayList<Restaurant> restuarantResults;

    public String getRatingImage() {
        return ratingImage;
    }

    public void setRatingImage(String ratingImage) {
        this.ratingImage = ratingImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public static ArrayList<Restaurant> fromJSON(JSONArray jsonArray) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject restJson = null;
            try {
                Restaurant restaurant = new Restaurant();
                restJson = jsonArray.getJSONObject(i);
                restaurant.name = restJson.getString(YELP_NAME);

                String address = restJson.getJSONObject(YELP_LOCATION).getString(YELP_ADDRESS).replace("[", "").replace("]", "").replace("\"", "").replace("," , " ");
                restaurant.location = address;
                restaurant.rating = restJson.getInt(YELP_REVIEW_COUNT);
                restaurant.ratingImage = restJson.getString(YELP_RATING_URL);
                restaurant.image = restJson.getString(YELP_IMAGE_URL);
                restaurant.phoneNumber = restJson.getString(YELP_PHONE);
               /* JSONArray yelpCategories = restJson.getJSONArray(YELP_CATEGORIES);
                int length = yelpCategories.length();
                for(int j=0; j<length ;j++){
                    String c = yelpCategories.getJSONObject(j).toString();
                    categories.add(c);
                }*/
                restaurants.add(restaurant);
            } catch (JSONException ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return restaurants;
    }

    public static ArrayList<Restaurant> fromJSON(JSONObject jsonObject) {

        try {
            JSONArray businesses = jsonObject.getJSONArray(YELP_BUSINESSES);
            restuarantResults = fromJSON(businesses);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return restuarantResults;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(rating);
        out.writeString(name);
        out.writeString(location);
        out.writeString(image);
        out.writeString(ratingImage);
        out.writeString(phoneNumber);
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    private Restaurant(Parcel in) {
        rating = in.readInt();
        name = in.readString();
        location = in.readString();
        image = in.readString();
        ratingImage = in.readString();
        phoneNumber = in.readString();
    }

    public Restaurant() {
        // normal actions performed by class, it's still a normal object!
    }
}
