package com.runtimeterror.controller;

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
}