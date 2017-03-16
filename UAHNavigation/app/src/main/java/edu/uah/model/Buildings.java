package edu.uah.model;

import android.graphics.Bitmap;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Buildings {
    private long id;
    private String name;
    private String description;
    private String address;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String print() {
        return " ID:" + this.id + " Name:" + this.name + " Description:" + this.description + " Address:" + this.address;
    }
}
