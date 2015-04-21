package com.codepath.shareyourtable.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject{
    
    public String getUsername() {
        return getString("username");
    }

    public String getLocation() {
        return getString("where");
    }

    public String getFullName() {
        return getString("fullName");
    }

    public ParseFile getProfileImg() {
        return getParseFile("profileImg");
    }

    public void setFullName(String fullName) {
        put("fullName", fullName);
    }

    public void setProfileImg(ParseFile profileImg) {
        put("profileImg", profileImg);
    }

    public void setLocation(String where) {
        put("where", where);
    }
    
    public String getInstallationId() {
        return getString("installationId");
    }
    
    public void setInstallationId(String installationId) {
        put("installationId", installationId);
    }

    public String getTime() {
        return getString("time");
    }

    public void setTime(String time) {
        put("time", time);
    }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        put("date", date);
    }
    
    public String getTableFor() {
        return getString("tableFor");
    }
    
    public void setTableFor(String tableFor) {
        put("tableFor", tableFor);
    }
    
    public String getLookingFor() {
        return getString("lookingFor");
    }
    
    public void setLookingFor(String lookingFor) {
        put("lookingFor", lookingFor);
    }
    
    public String getPersonalMsg() {
        return getString("personalMsg");
    }
    
    public void setPersonalMsg(String personalMsg) {
        put("personalMsg", personalMsg);
    }
    
    public void setUsername(String username) {
        put("username", username);
    }

    public ArrayList<String> getAcceptedList(){
        return (ArrayList)getList("accepted");
    }

    public void setAcceptedList(ArrayList<String> acceptedList){
        put("accepted", acceptedList);
    }

    public Date getEventDate() {
        return getDate("eventDate");
    }

    public void setEventDate(Date eventDate) {
        put("eventDate", eventDate);
    }

    public ArrayList<String> getAttendeesList(){
        return (ArrayList)getList("attendees");
    }

    public void setAttendeesList(ArrayList<String> attendeesList){
        put("attendees", attendeesList);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }
}
