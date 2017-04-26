package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 25/04/2017.
 */

public class Component {
    String imageUrl,name,price,grandParent,parent,relativeUrl;
    public Component() {
    }

    public Component(String imageUrl, String name, String price, String grandParent, String parent, String relativeUrl) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.grandParent = grandParent;
        this.parent = parent;
        this.relativeUrl = relativeUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGrandParent() {
        return grandParent;
    }

    public void setGrandParent(String grandParent) {
        this.grandParent = grandParent;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }
}
