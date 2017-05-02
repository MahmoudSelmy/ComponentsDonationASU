package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 01/05/2017.
 */

public class FirebaseLatling {
    private double latitude,longitude;

    public FirebaseLatling() {
    }

    public FirebaseLatling(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
