package com.runtimeterror.controller;

import com.runtimeterror.model.Item;

import java.util.List;

public interface GameInterface {
    // Used to get a formatted string object of the room information.
    public String getRoomText();

    // Used to get a formatted string of the players items
    public String getPLayerInventory();

    // Used to get a formatted string of the players items
    public boolean getPLayerStatus();

    // Submit player input.  Pass the player entered string to the game and gets player message back.
    public String submitPlayerString(String inputString);

    // User to get the Monster's location in relation to the player. 0 = monster in same room. 1 = monster in adjacent
    // room.  otherwise it returns -1.
    public int getMonsterLocation();

    public void reset();

    public String getRoomImagePath();

    public String getMapImagePath();

    boolean isGameOver();

    boolean isKilledByMonster();

    String getMessageLabel();

    void resetRound();

    String getRoomMapPath();

    int getPlayerHealth();

    boolean hasMap();

    boolean isMonsterNear();

    boolean isMonsterSameRoom();

    String getDialogue();

    boolean isHealthIncrease();

    int timeToEndGame();

    List<Item> playerInventory();
}