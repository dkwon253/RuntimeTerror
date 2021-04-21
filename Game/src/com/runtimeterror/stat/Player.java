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
    public void addToInventory(String item, String type, String description) {
        this.Inventory.add(new Item(item, type, description ));
        System.out.println("You added a " + item + " to your inventory");
    }

    //GETTERS AND SETTERS
    public void getInventory() {
        System.out.println(this.Inventory);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Rooms getCurrRoom() {
        System.out.println("you are currently in " + currRoom.getRoomName());
        System.out.println(" This is a "+currRoom.getRoomDescription());
        return currRoom;
    }

    public void setCurrRoom(Rooms currRoom) {
        this.currRoom = currRoom;
    }

}
