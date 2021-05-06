package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.model.*;
import com.runtimeterror.textparser.InputData;
import com.runtimeterror.textparser.Parser;

import java.io.*;
import java.util.*;

import static com.runtimeterror.utils.Pipe.apply;

class GameClientNew implements GameInterface, java.io.Serializable {
    private Database database;
    private boolean gameLoaded;

    GameClientNew(boolean test) {
        // test constructor
    }
    GameClientNew() {
        newDatabase();
        gameLoaded = false;
    }

    @Override
    public String submitPlayerString(String userInput) {
        Map<String, Result<?>> gameMap = getGameMap();
        gameMap.put("input", new Result<>(userInput));
        gameMap = gameProcessor(gameMap);
        return (String) gameMap.get("helpText").getResult();
    }

    Map<String, Result<?>> gameProcessor(Map<String, Result<?>> gameMap) {
        return
        apply(gameMap)
                .pipe(Parser::parseInput)
                .pipe(this::processHelp)
                .pipe(this::processStairs)
                .pipe(this::processUse)
                .pipe(this::processGet)
                .pipe(this::processGo)
                .pipe(this::processHide)
                .pipe(this::processMoveMonster)
                .pipe(this::processSaveGame)
                .pipe(this::processLoadGame)
                .pipe(this::processSkipPlayerTurn)
                .pipe(this::processMonsterEncounter)
                .pipe(this::processRoomChange)
                .result();
    }


    Map<String, Result<?>> processHelp(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        if ("HELP".equals(verb)) {
            gameMap.put("askedForHelp", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processStairs(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.getOrDefault("isProcessed", new Result<>(false)).getResult();
        if (isProcessed) {
            return gameMap;
        }

        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        boolean hasStairs = (boolean) gameMap.get("hasStairs").getResult();
        Rooms stairsRoom = (Rooms) gameMap.get("stairsRoom").getResult();
        if (hasStairs && "stairs".equals(noun)) {
            gameMap.put("didUseStairs", new Result<>(true));
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("playerCurrentRoom", new Result<>(stairsRoom));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processUse(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        if ("USE".equals(verb)) {
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("triedToUseItem", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            useItem(gameMap);
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processGet(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }

        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        String noun = inputData.getNoun();
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        Item roomItem = currentRoom.getItem();
        if ("GET".equals(verb) && noun != null && roomItem != null && noun.equals(roomItem.getName())) {
            List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
            inventory.add(currentRoom.getItem());
            currentRoom.removeRoomItem();
            gameMap.put("didGetItem", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processGo(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }

        Map<String, Rooms> availableRooms = (HashMap<String, Rooms>) gameMap.get("availableRooms").getResult();
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        Rooms newRoom = availableRooms.get(noun);
        if (newRoom != null) {
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("playerCurrentRoom", new Result<>(newRoom));
            gameMap.put("didChangeRoom", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processHide(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        if ("HIDE".equals(verb)) {
            gameMap.put("hidden", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processMoveMonster(Map<String, Result<?>> gameMap) {
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        if (shouldMonsterChangeRooms) {
            Random r = new Random();
            Map<String, Rooms> allRooms = (HashMap<String, Rooms>) gameMap.get("rooms").getResult();
            List<Rooms> allRoomsList = new ArrayList<>(allRooms.values());
            int randomInt = r.nextInt(allRoomsList.size() - 1) + 1;
            Rooms monsterNewRoom = allRoomsList.get(randomInt);
            gameMap.put("monsterCurrentRoom", new Result<>(monsterNewRoom));
            gameMap.put("didMonsterMove", new Result<>(true));
        }
        return gameMap;
    }


    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processSaveGame(Map<String, Result<?>> gameMap) {
        HashMap<String, Object> gameObjects = new HashMap<>();
        HashMap<String, Rooms> rooms = (HashMap<String, Rooms>) gameMap.get("rooms").getResult();
        Player player = (Player) gameMap.get("player").getResult();
        Monster monster = (Monster) gameMap.get("monster").getResult();

        gameObjects.put("rooms", rooms);
        gameObjects.put("player", player);
        gameObjects.put("monster", monster);
        try {
            FileOutputStream fos = new FileOutputStream("Game/gameData/savedGameData.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameObjects);
            oos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Failed to load the game files:");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processLoadGame(Map<String, Result<?>> gameMap) {
        boolean gameLoaded = (boolean) gameMap.get("gameLoaded").getResult();
        String result = "";
        if (!gameLoaded) {
            try {

                FileInputStream fis = new FileInputStream("Game/gameData/savedGameData.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);

                HashMap<String, Object> data = (HashMap<String, Object>) ois.readObject();
                fis.close();

                gameMap.put("rooms", new Result<>(data.get("rooms")));
                gameMap.put("player", new Result<>(data.get("player")));
                gameMap.put("monster", new Result<>(data.get("monster")));
                gameMap.put("gameLoaded", new Result<>(true));
                result = "game loaded from last checkpoint";

            } catch (Exception e) {
                return gameMap;
            }
        } else {
            result = "You have already loaded the game. You cannot do it again";
        }
        return gameMap;
    }

    Map<String, Result<?>> processSkipPlayerTurn(Map<String, Result<?>> gameMap) {
        return gameMap;
    }

    Map<String, Result<?>> processMonsterEncounter(Map<String, Result<?>> gameMap) {
        boolean didMonsterMove = (boolean) gameMap.get("didMonsterMove").getResult();
        if (didMonsterMove) {
            Rooms playerCurrentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
            Rooms monsterCurrentRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
            if (playerCurrentRoom == monsterCurrentRoom) {
                gameMap.put("isGameOver", new Result<>(true));
                gameMap.put("isKilledByMonster", new Result<>(true));
            }
        }
        return gameMap;
    }

    Map<String, Result<?>> processRoomChange(Map<String, Result<?>> gameMap) {
        gameMap.put("isProcessed", new Result<>(false));
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();
        if (didChangeRoom) {
            Rooms newRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
            gameMap.put("hasStairs", new Result<>(newRoom.hasStairs()));
            gameMap.put("stairsRoom", new Result<>(newRoom.getStairsNeighbor()));
            gameMap.put(("availableRooms"), new Result<>(newRoom.getRoomNeighbors()));
        }
        return gameMap;
    }

    Map<String, Result<?>> getGameMap() {
        return database.getDataAsHashMap();
    }

    void newDatabase() {
        database = new Database();
    }

    @SuppressWarnings("unchecked")
    public void useItem(Map<String, Result<?>> gameMap){
        InputData inputData = (InputData)  gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
        Item itemToRemove = new Item();
        for (Item item : inventory) {
            if(item != null && noun.equals(item.getName())) {
                gameMap.put("usedItem", new Result<>(true));
                gameMap.put("itemUsed", new Result<>(noun));
                itemToRemove = item;
                break;
            } else {
                gameMap.put("usedItem", new Result<>(false));
            }
        }
        inventory.remove(itemToRemove);
    }

    @Override
    public String getRoomText() {
        Map<String, Result<?>> gameMap = getGameMap();
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        System.out.println(room);
        return room.getRoomDescriptionText();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getPLayerInventory() {
        Map<String, Result<?>> gameMap = getGameMap();
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
    public int getMonsterLocation() {
        return -1;
    }

    @Override
    public void reset() {
        gameLoaded = false;
    }

    @Override
    public String getRoomImagePath() {
        Map<String, Result<?>> gameMap = getGameMap();
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return room.getRoomImagePath();
    }

    @Override
    public boolean getPLayerStatus() {
        Map<String, Result<?>> gameMap = getGameMap();
        return (boolean) gameMap.get("hidden").getResult();
    }

    @Override
    public String getMapImagePath() {
        Map<String, Result<?>> gameMap = getGameMap();
        Rooms room = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        return room.getMapImagePath();
    }

    @Override
    public boolean isGameOver() {
        Map<String, Result<?>> gameMap = getGameMap();
        return (boolean) gameMap.get("isGameOver").getResult();
    }

    @Override
    public boolean isKilledByMonster() {
        Map<String, Result<?>> gameMap = getGameMap();
        return (boolean) gameMap.get("isKilledByMonster").getResult();
    }

}