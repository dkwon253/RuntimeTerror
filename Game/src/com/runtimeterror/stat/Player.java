package com.runtimeterror.stat;

import java.util.ArrayList;
import java.util.List;

public class Player implements java.io.Serializable{

    //FIELDS
    private final String name;
    private final String description;
    private List<Item> Inventory;

    private Rooms currRoom;
    private boolean isHidden = false;

    //CONSTRUCTORS
    public Player(Rooms currRoom) {
        this.currRoom = currRoom;
        this.name = "playerOne";
        this.description = "You are a middle age man who is trapped in a Mysterious and spooky Mansion";
        this.Inventory = new ArrayList<>();
    }

    public Player(Rooms currRoom,String name, String description) {
        this.name = name;
        this.currRoom = currRoom;
        this.description = description;
        this.Inventory = new ArrayList<>();
    }

    //BUSINESS METHODS
    public String addToInventory(Item item) {
        this.Inventory.add(item);
        return "You added a(n) " + item.getName() + " to your inventory";
    }

    public String changeRoom(String direction){
        String result = "";
        if(this.getCurrRoom().getRoomNeighbors().get(direction) == null || !this.getCurrRoom().getRoomNeighbors().containsKey(direction)){
            result = "You cant go this way";
        }else{
            this.currRoom = getCurrRoom().getRoomNeighbors().get(direction);
        }
        return result;
    }

    //player to be able to hide in a room when hiding place is available
    public void hide(){
            isHidden = true;
    }

    public void unHide(){
        if (isHidden){
            isHidden = false;
        }
    }

    //instructions for the player
    public String help(){
        return "We are here to help. \n" +
                "To change location: Input (verb + noun) . For example: 'Go East' or 'Move East'. \n" +
                "To get an item: Input (verb + noun). For example: 'get pizza' or 'pick up pizza'. \n" +
                "To examine an item of location: Input (verb + noun). For example: 'look masterBedRoom' or 'scan masterBedRoom'. \n" +
                "To hide: Input (verb). For example: 'hide' or 'take cover'\n" +
                "To use an item: input (verb + noun). For example: 'use key' or 'manipulate key'";
    }

    //GETTERS & SETTERS
    public void getLocation(){
        System.out.println(currRoom.getRoomDescription());
    }

    public void getCurrentStatus(){
        this.getLocation();
        this.getCurrRoom().getHidingLocation();
        this.getCurrRoom().getRoomItem();
    }

    public boolean hasItem(String itemName){
        for (Item item : Inventory){
            if (item.getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    //GETTERS AND SETTERS
    public List<Item> getInventory() {
        return this.Inventory;
    }

    public void removeItemFromInventory(String itemName){
        Inventory.removeIf(item -> item.getName().equals(itemName));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Rooms getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(Rooms currRoom) {
        this.currRoom = currRoom;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
