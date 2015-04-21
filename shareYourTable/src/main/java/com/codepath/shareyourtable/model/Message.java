package com.codepath.shareyourtable.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserId() {
        return getString("userId");
    }
    
    public String getBody() {
        return getString("body");
    }
    
    public void setUserId(String userId) {
        put("userId", userId);
    }
    
    public void setBody(String body) {
        put("body", body);
    }

    public String getUsername(){
        return getString("username");
    }

    public void setUsername(String username){
        put("username", username);
    }

    public String getReceiver(){
        return getString("receiver");
    }

    public void setReceiver(String receiver){
        put("receiver", receiver);
    }

    public String getEventId(){
        return getString("eventId");
    }

    public void setEventId(String eventId){
        put("eventId", eventId);
    }
}
