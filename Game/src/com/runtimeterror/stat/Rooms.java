package com.runtimeterror.stat;

import java.util.HashMap;
import java.util.List;

public class Rooms {

    //Fields
    private String roomName;
    private String roomDescription;
    private HashMap<String, Rooms> roomNeighbors = null;
    private String hidingLocation;
    private Item item;


    //Constructor
    public Rooms(String roomName, String roomDescription, String hidingLocation, Item item) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomNeighbors = roomNeighbors;
        this.hidingLocation = hidingLocation;
        this.item = item;
    }

    public void getRoomItem() {
        System.out.println(item.getDescription());
    }

    //Getters & Setters
    public String getRoomName() {
        return roomName;
    }

    public Item getItem() {
        return item;
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

    public void getHidingLocation() {
        if (hidingLocation == null){
            System.out.println("This room does not have any hiding locations.");
        }else
            {System.out.println("There is a hiding spot " + hidingLocation);}
    }

    public void setHidingLocation(String hidingLocation) {
        this.hidingLocation = hidingLocation;
    }
}