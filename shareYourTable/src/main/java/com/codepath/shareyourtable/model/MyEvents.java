package com.codepath.shareyourtable.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by nsamant on 4/9/15.
 */
@ParseClassName("MyEvents")
public class MyEvents extends ParseObject {

    public String getUsername(){
        return getString("username");
    }

    public void setUsername(String username){
        put("username", username);
    }

    public String getEventId(){
        return getString("eventId");
    }

    public void setEventId(String eventId){
        put("eventId", eventId);
    }
}
