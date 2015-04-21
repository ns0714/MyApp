
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.MapActivity;
import com.codepath.shareyourtable.activities.RestaurantDetailActivity;
import com.codepath.shareyourtable.adapter.YelpListingsAdapter;
import com.codepath.shareyourtable.application.ShareYourTableApplication;
import com.codepath.shareyourtable.client.YelpClient;
import com.codepath.shareyourtable.helpers.NetworkHelper;
import com.codepath.shareyourtable.listeners.EndlessScrollListener;
import com.codepath.shareyourtable.model.Restaurant;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

public class YelpFragment extends Fragment {

    private ListView lvYelpListings;
    private TextView tvMap;
    private ProgressBar pb;
    private ArrayList<Restaurant> restaurants;
    private YelpListingsAdapter yelpAdapter;
    private YelpClient client;
    private static String searchTerm;
    private static String searchLocation;
    private int offset =0;

    private static final int LIMIT = 20;
    public static YelpFragment newInstance(String term, String location) {
        YelpFragment yelpFrag = new YelpFragment();
        Bundle args = new Bundle();
        searchTerm = term;
        searchLocation = location;
        args.putString("term", term);
        args.putString("location", location);
        yelpFrag.setArguments(args);
        return yelpFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants = new ArrayList<Restaurant>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("find", searchTerm);
        outState.putString("location", searchLocation);
        outState.putParcelableArrayList("restaurants", restaurants);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yelp, container, false);
        lvYelpListings = (ListView) view.findViewById(R.id.lvRestaurantListings);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvMap = (TextView) view.findViewById(R.id.tvMap);
        yelpAdapter = new YelpListingsAdapter(getActivity(), R.layout.restaurant_item, restaurants);
        lvYelpListings.setAdapter(yelpAdapter);
        client = ShareYourTableApplication.getRestClient();

        pb.setVisibility(ProgressBar.VISIBLE);
        populateResults(LIMIT,offset);

        lvYelpListings.setOnItemClickListener(

                new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Restaurant restaurant = restaurants.get(position);
                Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
                i.putExtra("restaurant", restaurant);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in,
                        R.anim.left_out);
            }
        });

        tvMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putParcelableArrayListExtra("restaurants", restaurants);
                startActivity(mapIntent);
            }
        });

        lvYelpListings.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreDataFromYelp();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            searchTerm = savedInstanceState.getString("find");
            searchLocation = savedInstanceState.getString("location");
            restaurants = savedInstanceState.getParcelableArrayList("restaurants");
            yelpAdapter.notifyDataSetChanged();
        }
    }

    private void loadMoreDataFromYelp() {
        offset = offset+LIMIT;
        populateResults(LIMIT, offset);
    }

    private void populateResults(int limit,int offset) {

        if (!NetworkHelper.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network),
                    Toast.LENGTH_SHORT).show();
        } else {
            client.getSearchResults(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject jsonObj) {
                    Log.d("DEBUG", jsonObj.toString());
                    restaurants.addAll(Restaurant.fromJSON(jsonObj));
                    yelpAdapter.notifyDataSetChanged();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }

                @Override
                public void onFailure(Throwable e, JSONObject js) {
                    Log.d("DEBUG", e.toString());
                    Log.d("DEBUG", js.toString());
                }
            }, searchTerm, searchLocation, limit, offset);
        }
    }
}

