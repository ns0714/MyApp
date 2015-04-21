package com.codepath.shareyourtable.helpers;

import android.content.Context;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddressHelper {

	public static LatLng
    getAddress(Context context, String address) {
		Geocoder coder = new Geocoder(context);
		LatLng latLng = null;
		try {
			List<android.location.Address> adresses = coder
					.getFromLocationName(address, 1);
			for (android.location.Address add : adresses) {
				// as country etc.
				double longitude = add.getLongitude();
				double latitude = add.getLatitude();
				latLng = new LatLng(latitude, longitude);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return latLng;
	}
}
