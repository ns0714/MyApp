package com.codepath.shareyourtable.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject{
    public String getEmail() {
        return getString("email");
    }

    public String getFirstname() {
        return getString("firstname");
    }

    public String getLastName() {
        return getString("lastname");
    }

    public String getPassword() {
        return getString("password");
    }
    
    public void setEmail(String email) {
        put("email", email);
    }

    public void setFirstName(String firstname) {
        put(firstname, "firstname");
    }

    public void setLastName(String lastname) {
        put(lastname,"lastname");
    }

    public void setPassword(String password) {
        put(password, "password");
    }
    
}
