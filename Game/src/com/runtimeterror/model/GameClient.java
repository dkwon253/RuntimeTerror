package com.runtimeterror.model;

import com.amazonaws.services.dynamodbv2.xspec.L;
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
        setupGame();
    }

    private void setupGame() {
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
        gameMap = postGameProcessor.start(gameMap);
        return (String) gameMap.get("helpText").getResult();
    }

    @Override
    public String getRoomText() {
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        String itemUsed = (String) gameMap.get("itemUsed").getResult();
        return room.getRoomDescriptionText(itemUsed);
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
    public boolean hasMap() {
        @SuppressWarnings("unchecked")
        List<Item> inv = (List<Item>) gameMap.get("inventory").getResult();
        return inv.stream().filter(item -> item.getName().equals("map")).findFirst().orElse(null) != null;
    }

    @Override
    public boolean isMonsterNear() {
        @SuppressWarnings("unchecked")
        Map<String, Rooms> rooms = (Map<String, Rooms>) gameMap.get("availableRooms").getResult();
        Rooms monsterCurrentRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
        return rooms.containsValue(monsterCurrentRoom);
    }

    @Override
    public boolean isMonsterSameRoom() {
        Rooms monsterCurrentRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
        Rooms playerCurrentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return monsterCurrentRoom == playerCurrentRoom;
    }

    @Override
    public String getDialogue() {
        return (String) gameMap.get("dialogueLabel").getResult();
    }

    @Override
    public boolean isHealthIncrease() {
        return (boolean) gameMap.get("didIncreaseHealth").getResult();
    }

    @Override
    public int timeToEndGame() {
        return (int) gameMap.get("timeToEndGame").getResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> playerInventory() {
        List<Item> itemList = (List<Item>) gameMap.get("inventory").getResult();
        return Collections.unmodifiableList(itemList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasItems() {
        List<Item> itemList = (List<Item>) gameMap.get("inventory").getResult();
        return itemList.size() > 0;
    }

    @Override

    public List<Item> getRoomItems() {
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return room.getRoomsItems();
    }

    public boolean isPlayerClosedToDying() {
        return (boolean) gameMap.get("isCloseToDying").getResult();
    }

    @Override
    public String getMonsterLabel() {
        return (String) gameMap.get("monsterLabel").getResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Rooms> getAvailableRooms() {
        return (Map<String, Rooms>) gameMap.get("availableRooms").getResult();
    }

    @Override
    public boolean hasStairs() {
        return (boolean) gameMap.get("hasStairs").getResult();
    }

    @Override
    public void setupDifficulty(String level) {
        @SuppressWarnings("unchecked")
        HashMap<String, List<Integer>> difficultyMap = (HashMap<String, List<Integer>>) gameMap.get("difficultyMap").getResult();
        List<Integer> levelList = difficultyMap.get(level);
        gameMap.put("level", new Result<>(level));
        gameMap.put("playerHealth", new Result<>(levelList.get(0)));
        gameMap.put("monsterDamage", new Result<>(levelList.get(1)));
        gameMap.put("timeToEndGame", new Result<>(levelList.get(2)));
    }

    @Override
    public List<Leaderboard> getLeaderboard(int size) {
        return database.getTopLeaderboard(size);
    }

    @Override
    public boolean addToLeaderboard(String userName, int runtime) {
        String level = (String) gameMap.get("level").getResult();
        Leaderboard leaderboard = new Leaderboard(userName, level, runtime);
        return database.addLeaderboard(leaderboard);
    }


    @Override
    public int getMonsterLocation() {
        return 0;
    }

    @Override
    public void reset() {
        setupGame();
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