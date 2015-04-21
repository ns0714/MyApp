package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.ViewEventActivity;
import com.codepath.shareyourtable.adapter.EventsListingAdapter;
import com.codepath.shareyourtable.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimelineFragment extends Fragment {
    
    private ListView lvTimeline;
    private ProgressBar pbProgressBar;
    private EventsListingAdapter eventAdapter;
    private ArrayList<Event> events;

    public static TimelineFragment newInstance() {
        TimelineFragment timelineFrag = new TimelineFragment();
        return timelineFrag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<Event>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        pbProgressBar = (ProgressBar) view.findViewById(R.id.pbLoading);
        lvTimeline = (ListView) view.findViewById(R.id.lvTimelineEvents);
        eventAdapter = new EventsListingAdapter(getActivity(), R.layout.eventsfeed_item, events);
        lvTimeline.setAdapter(eventAdapter);
        pbProgressBar.setBackgroundColor(getResources().getColor(R.color.btn_color_airbnb));
        pbProgressBar.setVisibility(ProgressBar.VISIBLE);
        populateEvents();

        return view;
    }
    public void populateEvents() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery("Event");
        eventQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        eventQuery.orderByAscending("eventDate");
        eventQuery.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> eventsList, ParseException e) {
                if (e == null) {
                    for(Event event : eventsList){
                        if(event.getEventDate().after(Calendar.getInstance().getTime())) {
                            events.add(event);
                        }
                    }
                    eventAdapter.notifyDataSetChanged();
                    pbProgressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    Toast.makeText(getActivity(), "No events found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent viewEventIntent = new Intent(getActivity(), ViewEventActivity.class);
                viewEventIntent.putExtra("username", events.get(position).getUsername());
                viewEventIntent.putExtra("objectId", events.get(position).getObjectId());
                startActivity(viewEventIntent);
            }
        });
    }
}
