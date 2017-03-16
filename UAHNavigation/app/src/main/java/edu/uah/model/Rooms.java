package edu.uah.model;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Rooms {
    private long id;
    private long building_id;
    private String room_number;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getBuilding() {
        return building_id;
    }
    public void setBuilding(long building_id) {
        this.building_id = building_id;
    }

    public String getRoom() {
        return room_number;
    }

    public void setRoom(String room_number) {
        this.room_number = room_number;
    }

    public String print() {
        return " ID:" + this.id + " Building_ID:" + this.building_id + " Room_Num:" + this.room_number;
    }
}
