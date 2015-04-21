
package com.codepath.shareyourtable.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.activities.TimelineActivity;
import com.codepath.shareyourtable.activities.YelpActivity;
import com.codepath.shareyourtable.helpers.DateHelper;
import com.codepath.shareyourtable.model.Event;
import com.codepath.shareyourtable.model.Profile;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventFragment extends Fragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView tvChoosePlace;
    private TextView tvSelectTime;
    private TextView tvSelectDate;
    private Spinner spTableFor;
    private RadioButton rbDating;
    private RadioButton rbHangout;
    private EditText etPersonalMsg;
    private Button btnShoutOut;
    private static String restaurant;
    private static String address;
    private int eDay;
    private int eMonth;
    private int eYear;
    private int eHour;
    private int eMin;
    private String eDate;
    private String eTime;
    private int spinnerVal;
    private ArrayAdapter<Integer> spinnerAdapter;
    private ArrayList<Integer> spinnerValues;

    private ParseFile imgFile;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    final Calendar calendar = Calendar.getInstance();

    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);


    public static CreateEventFragment newInstance(String restaurantName, String restAddress) {
        CreateEventFragment eventFrag = new CreateEventFragment();
        Bundle args = new Bundle();
        restaurant = restaurantName;
        address = restAddress;
        args.putString("restaurantName", restaurantName);
        eventFrag.setArguments(args);
        return eventFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerValues = new ArrayList<Integer>();
        addSpinnerValues();
        spinnerAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, spinnerValues);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        tvChoosePlace = (TextView) view.findViewById(R.id.tvChoosePlace);
        tvSelectDate = (TextView) view.findViewById(R.id.tvChooseDate);
        tvSelectTime = (TextView) view.findViewById(R.id.tvChooseTime);
        spTableFor = (Spinner) view.findViewById(R.id.spTableFor);
        rbDating = (RadioButton) view.findViewById(R.id.rbDating);
        rbHangout = (RadioButton) view.findViewById(R.id.rbHangout);
        etPersonalMsg = (EditText) view.findViewById(R.id.etPersonalMsg);
        btnShoutOut = (Button) view.findViewById(R.id.btnShoutOut);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTableFor.setSelection(1);
        spTableFor.setAdapter(spinnerAdapter);

        if (restaurant != null) {
            tvChoosePlace.setText(restaurant);
        }


        return view;
    }

    @Override
    public void onActivityCreated(
            Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            eDate = savedInstanceState.getString("date");
            System.out.print("Before setting date" + tvSelectDate.getText().toString());
            tvSelectDate.setText(eDate);
            System.out.print("After setting date" + tvSelectDate.getText().toString());
            eTime = savedInstanceState.getString("time");
            spinnerVal = savedInstanceState.getInt("spinner");
            String personal = savedInstanceState.getString("personal_msg");
            etPersonalMsg.setText(personal);
            System.out.print("Saved state??" + eDate);
        }
        createEvent();
    }

    public void createEvent() {

        tvChoosePlace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yelpIntent = new Intent(getActivity(), YelpActivity.class);
                startActivity(yelpIntent);
            }
        });
       /* if(eDate !=null){
            tvSelectDate.setText(eDate);
        }*/
        tvSelectDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setYearRange(2015, 2020);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        if(eTime !=null){
            tvSelectTime.setText(eTime);
        }
        tvSelectTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        btnShoutOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveEvent();
                Intent timelineIntent = new Intent(getActivity(), TimelineActivity.class);
                startActivity(timelineIntent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("date", eDate);
        outState.putString("time", eTime );
        outState.putString("personal_msg", etPersonalMsg.getText().toString() );
        outState.putInt("spinner", Integer.parseInt(spTableFor.getSelectedItem().toString()));
        super.onSaveInstanceState(outState);
    }

    public void saveEvent(){
        Event event = new Event();
        event.setLocation(restaurant);

        if (eDate !=null) {
            tvSelectDate.setText(eDate);
        }

        event.setDate(tvSelectDate.getText().toString());

        event.setInstallationId(ParseInstallation.getCurrentInstallation().getInstallationId());
        System.out.print("Date"+DateHelper.getDateFromString(eDate));
        event.setEventDate(DateHelper.getDateFromString(eDate));
        event.setTime(tvSelectTime.getText().toString());
        spinnerVal = (Integer.parseInt(spTableFor.getSelectedItem().toString()));
        spTableFor.setSelection(spinnerVal);
        event.setTableFor(String.valueOf(spTableFor.getSelectedItem().toString()));

        if (rbDating.isChecked()) {
            event.setLookingFor(getActivity().getResources().getString(R.string.dating));
        } else if (rbHangout.isChecked()) {
            event.setLookingFor(getActivity().getResources().getString(R.string.hangout));
        }

        event.setPersonalMsg(etPersonalMsg.getText().toString());
        event.setUsername(ParseUser.getCurrentUser().getUsername());
        event.setFullName(ParseUser.getCurrentUser().get("firstName").toString() + " "
                + ParseUser.getCurrentUser().get("lastName").toString());
        getUserProfilePic(event);
        event.setAddress(address);
        event.saveInBackground();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        eDay = day;
        eMonth = month+1;
        eYear = year;
        String monthString = new DateFormatSymbols().getMonths()[eMonth-1];
        eDate = eDay + "-" + monthString + "-" +  eYear;
        tvSelectDate.setText(eDate);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String min ="";
        eHour = hourOfDay;
        eMin = minute;
        String timeFormat;
        if(eHour <12){
            timeFormat ="AM";
        }else{
            eHour = eHour -12;
            timeFormat ="PM";
        }
        if(minute >=0 && minute < 10){
           min  = "0"+minute;
        }
        eTime = eHour + ":" + min + " " + timeFormat;
        tvSelectTime.setText(eTime);
    }

    public void getUserProfilePic(final Event event) {
        // Specify which class to query
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        // Specify the object id
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Profile>() {
            public void done(List<Profile> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    String firstItemId = itemList.get(0).getObjectId();

                    imgFile = itemList.get(0).getProfilePicture();
                    event.setProfileImg(imgFile);
                    event.saveInBackground();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addSpinnerValues(){
        for (int i = 1; i <= 10; i++) {
            spinnerValues.add(i);
        }
    }
}
