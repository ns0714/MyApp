package com.codepath.shareyourtable.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.adapter.EventsListingAdapter;
import com.codepath.shareyourtable.model.Event;
import com.codepath.shareyourtable.model.MyEvents;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nsamant on 4/9/15.
 */
public class MyEventsFragment extends Fragment {

    private ListView lvTimeline;
    private ProgressBar pbProgressBar;
    private EventsListingAdapter eventAdapter;
    private ArrayList<Event> events;
    private ArrayList<String> myAcceptedList;

    public static MyEventsFragment newInstance(){
        MyEventsFragment myEventsFragment = new MyEventsFragment();
        return myEventsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<Event>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        lvTimeline = (ListView) view.findViewById(R.id.lvTimelineEvents);
        pbProgressBar = (ProgressBar) view.findViewById(R.id.pbLoading);
        eventAdapter = new EventsListingAdapter(getActivity(), R.layout.eventsfeed_item, events);
        lvTimeline.setAdapter(eventAdapter);
        pbProgressBar.setVisibility(ProgressBar.VISIBLE);
        populateMyEvents();
        return view;
    }

    private void populateMyEvents() {

        ParseQuery<MyEvents> eventQuery = ParseQuery.getQuery("MyEvents");
        String user = ParseUser.getCurrentUser().getUsername();
        eventQuery.whereEqualTo("username", user);
        eventQuery.findInBackground(new FindCallback<MyEvents>() {
            public void done(List<MyEvents> objects, ParseException e) {
                if (e == null) {
                    for (MyEvents ev : objects) {
                        myAcceptedList = new ArrayList<String>();
                        System.out.print("Please come here + " +ev.getEventId());
                        myAcceptedList.add(ev.getEventId());
                    }
                    populateEvents();
                    //eventAdapter.notifyDataSetChanged();
                    pbProgressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    // Toast.makeText(getActivity(), "")
                }
            }
        });
    }

    public void populateEvents() {

        if (myAcceptedList != null) {
            for (String eventId : myAcceptedList) {
                System.out.print("****" + eventId);
                ParseQuery<Event> eventQuery = ParseQuery.getQuery("Event");
                eventQuery.whereEqualTo("objectId", eventId);
                //eventQuery.orderByAscending("eventDate");
                eventQuery.findInBackground(new FindCallback<Event>() {
                    public void done(List<Event> objects, ParseException e) {
                        if (e == null) {
                            for (Event event : objects) {
                                if (event.getEventDate().after(Calendar.getInstance().getTime())) {
                                    events.add(event);
                                }
                            }
                            eventAdapter.notifyDataSetChanged();
                            pbProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            // Toast.makeText(getActivity(), "")
                        }
                    }
                });
            }
        }
    }
}
