package com.example.current_location_app;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class MyLocationListener implements LocationListener {

    private Context context;
    public static Location location;

    public MyLocationListener(Context context){
        this.context = context;
        location = new Location("starter");
        location.setLatitude(0);
        location.setLongitude(0);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
