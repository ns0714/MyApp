
package com.codepath.shareyourtable.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Profile")
public class Profile extends ParseObject {
    
    public Profile(){
        super();
    }

    public String getUsername() {
        return getString("username");
    }
    
    public String getUserObjId() {
        return getString("userObj");
    }

    public String getFirstName() {
        return getString("firstName");
    }

    public String getLastName() {
        return getString("lastName");
    }

    public String getGender() {
        return getString("gender");
    }

    public int getAgeGroup() {
        return getInt("ageGroup");
    }

    public String getLookingFor() {
        return getString("lookingFor");
    }

    public String getInterests() {
        return getString("interests");
    }

    public ParseFile getProfilePicture() {
        return getParseFile("profilePicture");
    }

    public String getLocation() {
        return getString("location");
    }
    
    public String getAboutMe() {
        return getString("about");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    public void setLastName(String lastName) {
        put("lastName", lastName);
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

    public void setAgeGroup(int ageGroup) {
        put("ageGroup", ageGroup);
    }

    public void setLookingFor(String lookingFor) {
        put("lookingFor", lookingFor);
    }

    public void setInterests(String interests) {
        put("interests", interests);
    }

    public void setProfilePicture(ParseFile profilePicture) {
        put("profilePicture", profilePicture);
    }

    public void setLocation(String location) {
        put("location", location);
    }
    
    public void setAboutMe(String about) {
        put("about", about);
    }
    
    public void setUserObj(String userObj) {
        put("userObj", userObj);
    }

    public int getEventType() {
        return getInt("eventType");
    }

    public void setEventType(int eventType) {
        put("eventType", eventType);
    }

}
