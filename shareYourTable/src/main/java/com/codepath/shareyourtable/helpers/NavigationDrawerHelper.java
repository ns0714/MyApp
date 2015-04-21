package com.codepath.shareyourtable.helpers;

import com.codepath.shareyourtable.fragments.CreateEventFragment;
import com.codepath.shareyourtable.fragments.CreateProfileFragment;
import com.codepath.shareyourtable.fragments.FragmentNavigationDrawer;
import com.codepath.shareyourtable.fragments.LoginFragment;
import com.codepath.shareyourtable.fragments.LogoutFragment;
import com.codepath.shareyourtable.fragments.MyEventsFragment;
import com.codepath.shareyourtable.fragments.MyProfileFragment;
import com.codepath.shareyourtable.fragments.TimelineFragment;
import com.parse.ParseUser;

/**
 * Created by nsamant on 4/1/15.
 */
public class NavigationDrawerHelper {
    private static FragmentNavigationDrawer dlDrawer;
    private static ParseUser currentUser;

    public static void getNavigationDrawer(FragmentNavigationDrawer dlDrawer) {

      currentUser = ParseUser.getCurrentUser();
        // Add nav items
        if (currentUser != null) {
            dlDrawer.addNavItem("Events", "Events", TimelineFragment.class);
            dlDrawer.addNavItem("My Profile", "My Profile", MyProfileFragment.class);
            dlDrawer.addNavItem("My Events", "My Events", MyEventsFragment.class);
            dlDrawer.addNavItem("Create Event", "Create Event", CreateEventFragment.class);
            dlDrawer.addNavItem("Logout", "Logout", LogoutFragment.class);
        } else {
            dlDrawer.addNavItem("Login", "Login", LoginFragment.class);
            dlDrawer.addNavItem("Create Event", "Create Event", CreateEventFragment.class);
            dlDrawer.addNavItem("Create Profile", "Create Profile", CreateProfileFragment.class);
        }
    }
}
