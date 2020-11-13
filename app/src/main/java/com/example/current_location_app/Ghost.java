package com.example.current_location_app;

import android.location.Location;

public class Ghost {

    private int image;
    private String name;
    private boolean isEaten;
    private Location location;

    public Ghost(int image, String name, boolean isEaten, double lat, double lng) {
        this.image = image;
        this.name = name;
        this.isEaten = isEaten;
        this.location = new Location(getName());
        this.location.setLatitude(lat);
        this.location.setLongitude(lng);
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
