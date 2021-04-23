package com.runtimeterror.stat;

import java.util.ArrayList;
import java.util.List;

public class Player {

    //FIELDS
    private final String name;
    private final String description;
    private List<Item> Inventory;

    private Rooms currRoom;


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
    public void addToInventory(Item item) {
        this.Inventory.add(item);
        System.out.println("You added a " + item.getName() + " to your inventory");
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



    public void getLocation(){
        System.out.println(currRoom.getRoomDescription());
    }

    public void getCurrentStatus(){
        this.getLocation();
        this.getCurrRoom().getHidingLocation();
        this.getCurrRoom().getRoomItem();
    }
    //GETTERS AND SETTERS
    public List<Item> getInventory() {
        return this.Inventory;
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

}
