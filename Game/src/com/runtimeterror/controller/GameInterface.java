package com.runtimeterror.controller;

public interface GameInterface {
    // Used to get a formatted string object of the room information.
    public String getRoomText();

    // Used to get a formatted string of the players items
    public String getPLayerInventory();

    // Submit player input.  Pass the player entered string to the game and gets player message back.
    public String submitPlayerString(String inputString);
}