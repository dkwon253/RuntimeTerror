package com.runtimeterror.controller;

public class SwingController {
    private GameInterface gi;

    //CTOR
    public SwingController(GameInterface gi){
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

    public String getInventory(){
        return gi.getPLayerInventory();
    }

    public boolean getStatus() { return gi.getPLayerStatus(); }

    public int getMonsterData() { return gi.getMonsterLocation(); }

    public void startNewGame() { gi.reset(); }
}