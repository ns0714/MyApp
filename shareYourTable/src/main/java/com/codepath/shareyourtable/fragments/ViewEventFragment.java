package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.TimelineActivity;
import com.codepath.shareyourtable.model.Event;
import com.codepath.shareyourtable.model.MyEvents;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ViewEventFragment extends Fragment {

    private ImageView ivBackground;
    private TextView tvFullName;
    private TextView tvEventDay;
    private TextView tvLocation;
    private TextView tvAddress;
    private ImageView ivCalenderIcon;
    private ImageView ivLocationIcon;
    private ImageView ivProfilePic;
    private ImageButton ibAccept;
    private ListView lvAcceptedUsers;
    private ArrayAdapter<String> acceptedAdapter;

    private String username;
    private String eventId;
    private ParseFile imgFile;
    private Event event;
    private ArrayList<String> acceptedList;

    private int year;
    private int month;
    private String fullMonth;
    private int day;

    public static ViewEventFragment newInstance() {
        ViewEventFragment fragment = new ViewEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        username = getActivity().getIntent().getStringExtra("username");
        eventId = getActivity().getIntent().getStringExtra("objectId");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
        tvFullName = (TextView) view.findViewById(R.id.tvFullName);
        tvEventDay = (TextView) view.findViewById(R.id.tvEventDay);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        ivCalenderIcon = (ImageView) view.findViewById(R.id.ivCalenderIcon);
        ivLocationIcon = (ImageView) view.findViewById(R.id.ivLocationIcon);
        lvAcceptedUsers = (ListView) view.findViewById(R.id.lvAcceptedUsers);
        ibAccept = (ImageButton) view.findViewById(R.id.ibAccept);

        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereEqualTo("objectId", eventId);
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    event = events.get(0);
                    acceptedList = event.getAcceptedList();
                    if (acceptedList == null) {
                        lvAcceptedUsers.setVisibility(View.GONE);
                        acceptedList = new ArrayList<String>();
                    } else {
                        acceptedList = (ArrayList) event.getList("accepted");
                        acceptedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, acceptedList);
                        lvAcceptedUsers.setAdapter(acceptedAdapter);
                    }

                    tvFullName.setText(String.valueOf(event.getFullName()));
                    tvEventDay.setText(event.getDate() + " @ " + event.getTime());
                    if (event.getAddress() != null) {
                        tvLocation.setText(event.getLocation());
                        tvAddress.setText(event.getAddress());
                    } else {
                        tvLocation.setText(event.getLocation());
                    }
                    imgFile = event.getProfileImg();
                    byte[] bitmapdata;
                    if (imgFile != null) {
                        try {
                            bitmapdata = imgFile.getData();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0,
                                    bitmapdata.length);
                            ivProfilePic.setImageBitmap(bitmap);

                            ivBackground.setImageBitmap(bitmap);
                            ivBackground.setAlpha(50);

                        } catch (ParseException ex) {
                            // TODO Auto-generated catch block
                            ex.printStackTrace();
                        }
                    }
                } else {
                    //objectRetrievalFailed();
                }
            }
        });

        ibAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptedList = event.getAcceptedList();
                if (acceptedList == null) {
                    acceptedList = new ArrayList<String>();
                } else {
                    acceptedList = (ArrayList) event.getList("accepted");
                }
                if(!acceptedList.contains(ParseUser.getCurrentUser().getUsername())) {
                    acceptedList.add(ParseUser.getCurrentUser().getUsername());
                    event.setAcceptedList(acceptedList);
                }
                event.saveInBackground();

                MyEvents myEvent = new MyEvents();
                myEvent.setUsername(ParseUser.getCurrentUser().getUsername());
                myEvent.setEventId(eventId);
                myEvent.saveInBackground();

                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("installationId", event.getInstallationId());

                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage(event.getFullName() + " accepted your event");
                push.sendInBackground();
                Intent timelineIntent = new Intent(getActivity(), TimelineActivity.class);
                startActivity(timelineIntent);
            }
        });

        ivCalenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                calendarIntent.setData(Events.CONTENT_URI);
                calendarIntent.setType("vnd.android.cursor.item/event");
                calendarIntent.putExtra(Events.TITLE, event.getLocation());
                calendarIntent.putExtra(Events.EVENT_LOCATION,
                        event.getAddress());
                getDate(event.getEventDate());
                calendarIntent.putExtra(Events.DESCRIPTION, "");
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        event.getTime());


                GregorianCalendar startTime = new GregorianCalendar(year,
                        month, day, Integer.parseInt(event.getTime().substring(0,1)),0);
               // GregorianCalendar endTime = new GregorianCalendar(year, month,
                 //       day, Integer.parseInt(event.getEventEndTime().substring(0,2)), 0);
                // calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,
                // true);
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        startTime.getTimeInMillis());
               // calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                 //       endTime.getTimeInMillis());

                calendarIntent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
                calendarIntent.putExtra(Events.AVAILABILITY,
                        Events.AVAILABILITY_BUSY);
                startActivity(calendarIntent);
            }
        });

        return view;
    }


    public void getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            fullMonth = new SimpleDateFormat("MMMM").format(date);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }
    }

    public String getEventId(){
        if(eventId != null){
            return eventId;
        }
        return "";
    }
}
