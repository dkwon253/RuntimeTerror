package com.runtimeterror.model;

import java.util.*;

public class Rooms implements java.io.Serializable {

    //Fields
    private String roomName;
    private String roomDescription;
    private HashMap<String, Rooms> roomNeighbors = null;
    private String hidingLocation;
    private Item item;
    private final List<Item> roomsItems = new ArrayList<>();
    private boolean NPCVisited = false;
    private boolean NPCQuestCompleted = false;
    private String RoomImagePath;
    private boolean hasStairs;
    private Rooms stairsNeighbor;
    private String RoomMapPath;
    private String stairsNeighborName;
    private final Map<String, String> dialogueMap = new HashMap<>();
    private String dialogueItem;
    private String roomType;


    //Constructor

    public Rooms() {
    }

    public Rooms(String roomName, String roomDescription, String hidingLocation, Item item, String path, String mPath,
                 String stairsNeighbor, String dialogueItem, String dialogueFirst, String dialogueSecond, String roomType) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.hidingLocation = hidingLocation;
        setRoomType(roomType);
        setItem(item);
        this.RoomImagePath = path;
        setMapImagePath(mPath);
        setStairsNeighborName(stairsNeighbor);
        setDialogue(dialogueItem, dialogueFirst, dialogueSecond);
    }

    //Getters & Setters
    public String getRoomName() {
        return roomName;
    }

    public String getRoomImagePath() {
        return RoomImagePath;
    }

    public HashMap<String, Rooms> getRoomNeighbors() {
        return roomNeighbors;
    }

    public void setRoomNeighbors(HashMap<String, Rooms> roomNeighbors) {
        this.roomNeighbors = roomNeighbors;
    }

    public void setStairsNeighbor(Rooms stairsNeighbor) {
        if (stairsNeighbor != null) {
            this.stairsNeighbor = stairsNeighbor;
            setHasStairs();
        }
    }


    public Item getAndRemoveRoomItem(String itemName) {
        Item itemToReturn = roomsItems.stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst().orElse(null);
        roomsItems.remove(itemToReturn);
        return itemToReturn;
    }

    public boolean hasStairs() {
        return hasStairs;
    }


    public String getHidingLocation() {
        return hidingLocation;
    }

    private void setHasStairs() {
        this.hasStairs = true;
    }

    public void setMapImagePath(String location) {
        this.RoomMapPath = location;
    }

    public String getMapImagePath() {
        return this.RoomMapPath;
    }

    public String getStairsNeighborName() {
        return this.stairsNeighborName;
    }

    public void setStairsNeighborName(String neighborName) {
        if (neighborName != null) {
            this.stairsNeighborName = neighborName;
            setHasStairs();
        }
    }

    public String getRoomDescriptionText(String itemUsed) {
        StringJoiner items = new StringJoiner(", ");
        for (Item item : roomsItems) {
            items.add(item.getName());
        }
        String result = "Location:\n" + this.roomName;
        result += "\n\n" + this.roomDescription + "\n";
        if (!this.roomsItems.isEmpty()) {
            result += "\nItem(s): " + items.toString();
        }
        result += "\nExits:";
        String[] directions = {"north", "east", "south", "west"};
        for (String direction : directions) {
            if (roomNeighbors != null && roomNeighbors.get(direction) != null) {
                result += " " + direction;
            }
        }
        if(NPCQuestCompleted) {
            result += "\n\n" + dialogueDeterminer(dialogueItem);
        } else {
            result += "\n\n" + dialogueDeterminer(itemUsed);
        }
        return result;
    }

    public void setItem(Item item) {
        if (item != null) {
            this.item = item;
            roomsItems.add(item);
        }
    }

    public void addItem(Item item) {
        roomsItems.add(item);
    }

    public boolean doesItemExist(String item) {
        boolean result = false;
        for (Item roomItem : roomsItems) {
            if (roomItem.getName().equals(item)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public String dialogueDeterminer(String item) {
        if(item.equals(dialogueItem)){
            NPCQuestCompleted = true;
        }
        return dialogueMap.getOrDefault(item, "");
    }

    private void setDialogue(String dialogueItem, String dialogueFirst, String dialogueSecond) {
        if(dialogueItem != null) {
            this.dialogueItem = dialogueItem;
            dialogueMap.put("", dialogueFirst);
            dialogueMap.put(dialogueItem, dialogueSecond);
        }
    }

    private void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }
}