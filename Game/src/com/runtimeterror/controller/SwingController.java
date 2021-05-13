package com.runtimeterror.controller;

import com.runtimeterror.model.Item;
import com.runtimeterror.model.Leaderboard;
import com.runtimeterror.model.Rooms;

import java.util.List;
import java.util.Map;

public class SwingController {
    private GameInterface gi;

    //CTOR
    public SwingController(GameInterface gi) {
        // done: Setup with game interface
        this.gi = gi;
    }

    // GUI TEST FUNCTIONS
    public String processInput(String input) {
        return gi.submitPlayerString(input);
    }

    public String getRoomDesc() {
        return gi.getRoomText();
    }

    public String getInventory() {
        return gi.getPLayerInventory();
    }

    public String getRoomImagePath() {
        return gi.getRoomImagePath();
    }

    public boolean getStatus() {
        return gi.getPLayerStatus();
    }

    public int getMonsterData() {
        return gi.getMonsterLocation();
    }

    public void startNewGame() {
        gi.reset();
    }

    public boolean isGameOver() {
        return gi.isGameOver();
    }

    public boolean isKilledByMonster() {
        return gi.isKilledByMonster();
    }

    public String getMessageLabel() {
        return gi.getMessageLabel();
    }

    public void resetRound() {
        gi.resetRound();
    }

    public String getRoomMapPath() {
        return gi.getRoomMapPath();
    }

    public int getPlayerHealth() {
        return gi.getPlayerHealth();
    }

    public boolean hasMap() { return gi.hasMap(); }

    public boolean isMonsterNear() {return gi.isMonsterNear();}

    public boolean isMonsterSameRoom() {return gi.isMonsterSameRoom();}

    public String getDialogue() {return gi.getDialogue();}

    public boolean isHealthIncrease() {return gi.isHealthIncrease();}

    public int getTimeToEndGame() {return gi.timeToEndGame();}

    public List<Item> getPlayerItems() {return gi.playerInventory();}

    public List<Item> getRoomItems() {return gi.getRoomItems();}

    public boolean hasItems() {return gi.hasItems();}

    public boolean isCloseToDying() { return gi.isPlayerClosedToDying(); }

    public String getMonsterLabel() {return gi.getMonsterLabel();}

    public Map<String, Rooms> getAvailableRooms() {return gi.getAvailableRooms();}

    public boolean hasStairs() {return gi.hasStairs();}

    public boolean hasElevator() {return gi.hasElevator();}

    public void setupGameDifficulty(String level) {gi.setupDifficulty(level);}

    public List<Leaderboard> getLeaderboard(int size) {return gi.getLeaderboard(size);}

    public boolean addToLeaderboard(String userName, int runtime) {return gi.addToLeaderboard(userName, runtime);}

    public boolean isBloodLost() { return gi.isBloodLost();}
}