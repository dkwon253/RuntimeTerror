package com.runtimeterror.model;

import com.runtimeterror.controller.GameInterface;

import java.util.*;

public class GameClient implements GameInterface, java.io.Serializable {
    private Database database;
    private GameProcessor gameProcessor;
    private PostGameProcessor postGameProcessor;
    private Map<String, Result<?>> gameMap;

    GameClient(boolean test) {
        // test constructor
    }

    public GameClient() {
        newDatabase();
        gameMap = getGameMap();
        newGameProcessor();
        newPostGameProcessor();
    }



    void newDatabase() {
        database = new Database();
    }

    void newGameProcessor() {
        gameProcessor = new GameProcessor();
    }

    void newPostGameProcessor() {
        postGameProcessor = new PostGameProcessor();
    }

    private Map<String, Result<?>> getGameMap() {
        return database.getDataAsHashMap();
    }

    @Override
    public String submitPlayerString(String userInput) {
        gameMap.put("input", new Result<>(userInput));
        gameProcessor.start(gameMap);
        postGameProcessor.start(gameMap);
        return (String) gameMap.get("helpText").getResult();
    }

    @Override
    public String getRoomText() {
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        System.out.println(room);
        return room.getRoomDescriptionText();
    }

    @Override
    public String getPLayerInventory() {
        @SuppressWarnings("unchecked")
        List<Item> inv = (List<Item>) gameMap.get("inventory").getResult();
        List<String> invString = new ArrayList<>();
        for (Item item : inv) {
            if (item != null) {
                invString.add(item.getName());
            }
        }
        return "Inventory: \n" + String.join(", ", invString);
    }


    @Override
    public String getMessageLabel() {
        return (String) gameMap.get("messageLabel").getResult();
    }

    @Override
    public void resetRound() {
        LoadRoomData.setGameMapRoundDefaults(gameMap);
    }

    @Override
    public String getRoomMapPath() {
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return currentRoom.getMapImagePath();
    }

    @Override
    public int getPlayerHealth() {
        return (int) gameMap.get("playerHealth").getResult();
    }

    @Override
    public int getMonsterLocation() {
        return -1;
    }

    @Override
    public void reset() {
    }

    @Override
    public String getRoomImagePath() {
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return room.getRoomImagePath();
    }

    @Override
    public boolean getPLayerStatus() {
        return (boolean) gameMap.get("hidden").getResult();
    }

    @Override
    public String getMapImagePath() {
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return room.getMapImagePath();
    }

    @Override
    public boolean isGameOver() {
        return (boolean) gameMap.get("isGameOver").getResult();
    }

    @Override
    public boolean isKilledByMonster() {
        return (boolean) gameMap.get("isKilledByMonster").getResult();
    }

}