package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 28/04/2017.
 */

public class UserModel {
    String name,position,imageUrl,address;
    FirebaseLatling location;

    public UserModel() {
    }

    public UserModel(String name, String position, String imageUrl, String address, FirebaseLatling location) {
        this.name = name;
        this.position = position;
        this.imageUrl = imageUrl;
        this.address = address;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public FirebaseLatling getLocation() {
        return location;
    }

    public void setLocation(FirebaseLatling location) {
        this.location = location;
    }
}
