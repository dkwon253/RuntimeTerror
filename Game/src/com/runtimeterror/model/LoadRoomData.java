package com.runtimeterror.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadRoomData {

    static Rooms defaultRoom;

    public static Map<String, Result<?>> loadData() throws IOException {
        Map<String, Result<?>> gameMap = new HashMap<>();
        HashMap<String, Rooms> roomList = new HashMap<>();
        List<String> listOfItems = new ArrayList<>();
        setupRoomData(roomList, listOfItems);
        setupNeighbors(roomList);
        setupGameMapDefaults(gameMap, roomList, listOfItems);
        setUpStaticLabels(gameMap);
        return gameMap;
    }

    private static void setupRoomData(HashMap<String, Rooms> roomList, List<String> listOfItems) throws IOException {
        AtomicInteger i = new AtomicInteger();
        Files.lines(Path.of("Game/RoomData/data.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String itemName = "null".equals(tokens[1]) ? null : tokens[1];
            String itemType = "null".equals(tokens[2]) ? null : tokens[2];
            String itemDescription = "null".equals(tokens[3]) ? null : tokens[3];
            String hidingSpot = "null".equals(tokens[4]) ? null : tokens[4];
            String stairsNeighbor = "null".equals(tokens[12]) ? null : tokens[12];
            String description = tokens[9];
            String path = tokens[10];
            String mapPath = tokens[11];

            Rooms newRoom;
            if (itemName == null) {
                newRoom = new Rooms(roomName, description, hidingSpot, null, path, mapPath, stairsNeighbor);

            } else {
                listOfItems.add(itemName);
                newRoom = new Rooms(roomName, description, hidingSpot, new Item(itemName, itemType, itemDescription), path, mapPath, stairsNeighbor);

            }
            if (i.get() == 0) {
                defaultRoom = newRoom;
            }
            roomList.put(roomName, newRoom);
            i.getAndIncrement();
        });
        addDirections(listOfItems);
    }

    private static void addDirections(List<String> listOfItems) {
        listOfItems.add("east");
        listOfItems.add("north");
        listOfItems.add("south");
        listOfItems.add("west");
        listOfItems.add("stairs");
        listOfItems.add("elevator");
    }

    private static void setupNeighbors(HashMap<String, Rooms> roomList) throws IOException {
        Files.lines(Path.of("Game/RoomData/data.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String east = "null".equals(tokens[5]) ? null : tokens[5];
            String west = "null".equals(tokens[6]) ? null : tokens[6];
            String north = "null".equals(tokens[7]) ? null : tokens[7];
            String south = "null".equals(tokens[8]) ? null : tokens[8];
            String stairs = "null".equals(tokens[10]) ? null : tokens[10];

            HashMap<String, Rooms> neighbor = new HashMap<>();
            neighbor.put("east", roomList.get(east));
            neighbor.put("west", roomList.get(west));
            neighbor.put("north", roomList.get(north));
            neighbor.put("south", roomList.get(south));

            roomList.get(roomName).setRoomNeighbors(neighbor);
            roomList.get(roomName).setStairsNeighbor(roomList.get(stairs));
        });
    }

    private static void setupGameMapDefaults(Map<String, Result<?>> gameMap, HashMap<String, Rooms> roomList, List<String> listOfItems) {
        gameMap.put("monsterCurrentRoom", new Result<>(new Rooms()));
        gameMap.put("playerCurrentRoom", new Result<>(defaultRoom));
        gameMap.put("availableRooms", new Result<>(defaultRoom.getRoomNeighbors()));
        gameMap.put("rooms", new Result<>(roomList));
        gameMap.put("inventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("gameLoaded", new Result<>(false));
        gameMap.put("hasStairs", new Result<>(defaultRoom.hasStairs()));
        gameMap.put("stairsRoom", new Result<>(defaultRoom.getStairsNeighborName()));
        gameMap.put("player", new Result<>(new Player()));
        gameMap.put("monster", new Result<>(new Monster()));
        gameMap.put("startingRoom", new Result<>(defaultRoom));
        gameMap.put("helpText", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP"));
        gameMap.put("escapeItem", new Result<>("bolt cutters"));
        gameMap.put("listOfItems", new Result<>(listOfItems));
        setGameMapRoundDefaults(gameMap);
    }

    private static void setUpStaticLabels(Map<String, Result<?>> gameMap) {
        Map<String, String> staticLabels  = new HashMap<>();
        staticLabels.put("didUseStairs", "You used the stairs.");
        staticLabels.put("triedToUseStairs", "There are no stairs in this room.");
        staticLabels.put("triedToUseItem", "You don't have that item.");
        staticLabels.put("usedItem", "You used an item.");
        staticLabels.put("didGetItem", "You picked up an item.");
        staticLabels.put("didChangeRoom", "You went in a direction");
        staticLabels.put("triedToGoDirection", "You can't go that direction.");
        staticLabels.put("triedToGetItem", "The room doesn't have that item.");
        staticLabels.put("triedToHide", "You can't hide in this room.");
        staticLabels.put("didLoadGame", "Your previous save has been loaded.");
        staticLabels.put("triedToLoadGame", "You cannot load that game.");
        staticLabels.put("triedToSaveGame", "There was a problem saving the game.");
        staticLabels.put("didSaveGame", "Your game was saved.");
        staticLabels.put("", "");
        gameMap.put("staticLabels", new Result<>(Collections.unmodifiableMap(staticLabels)));
    }

    public static void setGameMapRoundDefaults(Map<String, Result<?>> gameMap) {
        gameMap.put("input", new Result<>(""));
        gameMap.put("inputData", new Result<>(""));
        gameMap.put("usedStairs", new Result<>(false));
        gameMap.put("hidden", new Result<>(false));
        gameMap.put("isProcessed", new Result<>(false));
        gameMap.put("didChangeRoom", new Result<>(false));
        gameMap.put("addendumText", new Result<>(""));
        gameMap.put("itemUsed", new Result<>(""));
        gameMap.put("usedItem", new Result<>(false));
        gameMap.put("triedToUseItem", new Result<>(false));
        gameMap.put("askedForHelp", new Result<>(false));
        gameMap.put("shouldMonsterChangeRooms", new Result<>(false));
        gameMap.put("didMonsterMove", new Result<>(false));
        gameMap.put("isGameOver", new Result<>(false));
        gameMap.put("isKilledByMonster", new Result<>(false));
        gameMap.put("didUseStairs", new Result<>(false));
        gameMap.put("didGetItem", new Result<>(false));
        gameMap.put("triedToGoDirection", new Result<>(false));
        gameMap.put("triedToGetItem", new Result<>(false));
        gameMap.put("triedToHide", new Result<>(false));
        gameMap.put("triedToUseStairs", new Result<>(false));
        gameMap.put("viewLabel", new Result<>(""));
        gameMap.put("messageLabel", new Result<>(""));

    }
}
