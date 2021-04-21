package com.runtimeterror.stat;

import java.util.HashMap;

public class Rooms {

    //Fields
    private String roomName;
    private String roomDescription;
    private String[] roomNeighbors;
    private String hidingLocation;

    //Constructor
    public Rooms(String roomName, String roomDescription, String[] roomNeighbors, String hidingLocation) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomNeighbors = roomNeighbors;
        this.hidingLocation = hidingLocation;
    }

    //Getters & Setters
    public String getRoomName() {
        return roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String[] getRoomNeighbors() {
        return roomNeighbors;
    }

    public void setRoomNeighbors(String[] roomNeighbors) {
        this.roomNeighbors = roomNeighbors;
    }

    public String getHidingLocation() {
        if (hidingLocation == null){
            return "This room does not have any hiding locations.";
        }
        return hidingLocation;
    }

    public void setHidingLocation(String hidingLocation) {
        this.hidingLocation = hidingLocation;
    }
}