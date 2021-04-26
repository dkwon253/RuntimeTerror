package com.runtimeterror.stat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public HashMap<String, Rooms> getRoomNeighbors() {
        return roomNeighbors;
    }

    public void setRoomNeighbors(HashMap<String, Rooms> roomNeighbors) {
        this.roomNeighbors = roomNeighbors;
    }

    public String getHidingLocation() {
        return hidingLocation;
    }

    public void setHidingLocation(String hidingLocation) {
        this.hidingLocation = hidingLocation;
    }

    public String getRoomDescriptionText(){
        String result = "Location:\n" + this.roomName;
        result += "\n\n" + this.roomDescription + "\n";
        if (this.item != null){
            result += "\nItem: " + this.item.getName();
        }
        result += "\nExits:";
        String[] directions = {"north","east","south","west"};
        for (String direction : directions) {
            if (roomNeighbors.get(direction) != null){
                result += " " + direction;
            }
        }
        return result;
    }

    public Item removeItemFromRoom(String itemName){
        if (this.item != null) {
            if (item.getName().equals(itemName)){
                Item temp = this.item;
                this.item = null;
                return  temp;
            }
            else{
                return null;
            }
        }
        return null;
    }
}