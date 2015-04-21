package com.codepath.shareyourtable.helpers;

/**
 * Created by nsamant on 4/2/15.
 */

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more efficient means
 * such as through injection directly into interested classes.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}