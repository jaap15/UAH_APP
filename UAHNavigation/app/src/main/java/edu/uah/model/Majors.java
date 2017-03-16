package edu.uah.model;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Majors {
    private long id;
    private String name;
    private String description;

    public void setAll(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return name;
    }
    public void setTitle(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String print() {
        return " ID:" + this.id + " Name:" + this.name + " Description:" + this.description;
    }
}
