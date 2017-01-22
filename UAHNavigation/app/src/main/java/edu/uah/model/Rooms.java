package edu.uah.model;

/**
 * Created by Daniel on 1/16/2017.
 */

public class Rooms {
    private long id;
    private int building_id;
    private int room_number;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getBuilding() {
        return building_id;
    }
    public void setBuilding(int building_id) {
        this.building_id = building_id;
    }

    public int getRoom() {
        return room_number;
    }

    public void setRoom(int room_number) {
        this.room_number = room_number;
    }
}
