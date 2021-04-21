package com.runtimeterror.stat;

import java.util.ArrayList;
import java.util.List;

public class Player {

    //FIELDS
    private final String name;
    private final String description;
    private List<Item> Inventory;
//    private Room currRoom;


    //CONSTRUCTORS
    public Player() {
        this.name = "playerOne";
        this.description = "You are a middle age man who is trapped in a Mysterious and spooky Mansion";
        this.Inventory = new ArrayList<>();
    }
    public Player(String name, String description) {
        this.name = name;
        this.description = description;
        this.Inventory = new ArrayList<>();
    }
    //BUSINESS METHODS
    public void addToInventory(String item, String type, String description) {
        this.Inventory.add(new Item(item, type, description ));
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

//    public Room getCurrRoom() {
//        return currRoom;
//    }


}
