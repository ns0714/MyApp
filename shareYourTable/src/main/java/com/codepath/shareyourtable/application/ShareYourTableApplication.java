
package com.codepath.shareyourtable.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.client.YelpClient;
import com.codepath.shareyourtable.model.Event;
import com.codepath.shareyourtable.model.Message;
import com.codepath.shareyourtable.model.MyEvents;
import com.codepath.shareyourtable.model.Profile;
import com.codepath.shareyourtable.model.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ShareYourTableApplication extends Application {
    
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        ShareYourTableApplication.context = this;
        ParseObject.registerSubclass(Profile.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(MyEvents.class);
        //ShareYourTableApplication.context = this;
        // Initialize Parse key here
        Parse.initialize(this, getString(R.string.parseApplicationId), getString(R.string.parseClientId));
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
      //Test creation of object
      /*  ParseObject testObject = new ParseObject("TestObject");
        testObject.put("OO", "mar");
        testObject.saveInBackground();*/
       
    }
    
    public static YelpClient getRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, ShareYourTableApplication.context);
    }
}
