package com.runtimeterror.stat;

import java.io.IOException;
import java.util.*;

public class Rooms implements java.io.Serializable{

    //Fields
    private String roomName;
    private String roomDescription;
    private HashMap<String, Rooms> roomNeighbors = null;
    private String hidingLocation;
    private Item item;
    private boolean NPCVisited = false;
    private boolean NPCQuestCompleted = false;


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

    public String getRoomDescriptionText(Player player) {

        String NPCDialoge = getNPC_Dialoge(player);

        String result = "Location:\n" + this.roomName;
        result += "\n\n" + this.roomDescription + NPCDialoge + "\n";
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

    public String getNPC_Dialoge(Player player) {
        String result = "";

        if(NPCQuestCompleted){
            return "";
        }

        if(this.roomName.equals("Bedroom Two") && NPCVisited){
            result = "\n\n\"Looks like you have not brought me any delicious food yet. You wont be getting any advice from me young one. You can bet you will be eaten alive pretty soon.\"";
        }

        if(this.roomName.equals("Bedroom Two") && !NPCVisited){
            NPCVisited = true;
            result = "\n\nHearing your footsteps the old man wakes up and opens his eyes. \"Well, well, look what we have here. Another potential meal for that monster. Young one, you only have a slim chance of escaping that evil monster. I am locked up and have no chance of escaping. Go to the kitchen and bring me some delicious food, and I will give you a hint that will help you in escaping from the cursed building.\"";
        }

        if(this.roomName.equals("Bedroom Two") && player.getInventory().contains("cake")){
            result = " \n\n\"Thank you for bringing some of this delicious cake. For having taken this risk upon yourself, I will give you this advice. Go down to the basement level, in one of those rooms you will find some cutters that will help you open some doors throughout this building. Finding those cutters is your only way to escape. Now go!!!!!!\" ";
            NPCQuestCompleted = true;
            player.removeItemFromInventory("cake");

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