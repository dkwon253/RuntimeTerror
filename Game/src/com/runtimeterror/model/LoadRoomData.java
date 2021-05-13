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
        List<String> weaponList = new ArrayList<>();
        List<String> listOfItems = new ArrayList<>();
        setupRoomData(roomList, listOfItems, weaponList);
        setupNeighbors(roomList);
        setupGameMapDefaults(gameMap, roomList, listOfItems, weaponList);
        return gameMap;
    }

    private static void setupRoomData(HashMap<String, Rooms> roomList, List<String> listOfItems, List<String> weaponList) throws IOException {
        AtomicInteger i = new AtomicInteger();
        Files.lines(Path.of("Game/RoomData/data.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String itemName = "null".equals(tokens[1]) ? null : tokens[1];
            String itemType = "null".equals(tokens[2]) ? null : tokens[2];
            String itemDescription = "null".equals(tokens[3]) ? null : tokens[3];
            String hidingSpot = "null".equals(tokens[4]) ? null : tokens[4];
            String stairsNeighbor = "null".equals(tokens[12]) ? null : tokens[12];
            String dialogueItem = "null".equals(tokens[13]) ? null : tokens[13];
            String dialogueFirst = "null".equals(tokens[14]) ? null : tokens[14];
            String dialogueSecond = "null".equals(tokens[15]) ? null : tokens[15];
            String roomType = "null".equals(tokens[16]) ? null : tokens[16];
            String itemImagePath = "null".equals(tokens[17]) ? null : tokens[17];
            String elevatorNeighbor = "null".equals(tokens[18]) ? null : tokens[18];
            String description = tokens[9];
            String path = tokens[10];
            String mapPath = tokens[11];

            Rooms newRoom;
            if (itemName == null) {
                newRoom = new Rooms(roomName, description, hidingSpot, null, path, mapPath,
                        stairsNeighbor, elevatorNeighbor, dialogueItem, dialogueFirst, dialogueSecond, roomType);


            } else {
                listOfItems.add(itemName);
                Item item = new Item(itemName, itemType, itemDescription, itemImagePath);
                newRoom = new Rooms(roomName, description, hidingSpot, item, path, mapPath,
                        stairsNeighbor, elevatorNeighbor, dialogueItem, dialogueFirst, dialogueSecond, roomType);
                if ("weapon".equals(itemType)) {
                    weaponList.add(itemName);
                }
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
            String stairs = "null".equals(tokens[12]) ? null : tokens[12];
            String elevator = "null".equals(tokens[18]) ? null : tokens[18];

            HashMap<String, Rooms> neighbor = new HashMap<>();
            neighbor.put("east", roomList.get(east));
            neighbor.put("west", roomList.get(west));
            neighbor.put("north", roomList.get(north));
            neighbor.put("south", roomList.get(south));

            roomList.get(roomName).setRoomNeighbors(neighbor);
            roomList.get(roomName).setStairsNeighbor(roomList.get(stairs));
            roomList.get(roomName).setElevatorNeighbor(roomList.get(elevator));
        });
    }

    private static void setupGameMapDefaults(Map<String, Result<?>> gameMap, HashMap<String, Rooms> roomList, List<String> listOfItems, List<String> weaponList) {
        List<Integer> easy = List.of(20, 4, 600);
        List<Integer> medium = List.of(17, 3, 500);
        List<Integer> hard = List.of(15, 2, 300);
        HashMap<String, List<Integer>> gameLevels = new HashMap<>();
        gameLevels.put("easy", easy);
        gameLevels.put("medium", medium);
        gameLevels.put("hard", hard);
        gameMap.put("monsterCurrentRoom", new Result<>(new Rooms()));
        gameMap.put("playerCurrentRoom", new Result<>(defaultRoom));
        gameMap.put("availableRooms", new Result<>(defaultRoom.getRoomNeighbors()));
        gameMap.put("rooms", new Result<>(roomList));
        gameMap.put("inventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("gameLoaded", new Result<>(false));
        gameMap.put("hasStairs", new Result<>(defaultRoom.hasStairs()));
        gameMap.put("stairsRoom", new Result<>(defaultRoom.getStairsNeighborName()));
        gameMap.put("hasElevator", new Result<>(defaultRoom.hasElevator()));
        gameMap.put("elevatorRoom", new Result<>(defaultRoom.getElevatorNeighborName()));
        gameMap.put("startingRoom", new Result<>(defaultRoom));
        gameMap.put("helpText", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP"));
        gameMap.put("escapeItem", new Result<>("bolt cutters"));
        gameMap.put("listOfItems", new Result<>(listOfItems));
        gameMap.put("listOfWeapons", new Result<>(weaponList));
        gameMap.put("timeToEndGame", new Result<>(300));
        gameMap.put("lowPlayerHealth", new Result<>(5));
        gameMap.put("playerHealth", new Result<>(15));
        gameMap.put("monsterDamage", new Result<>(5));
        gameMap.put("nonUseItems", new Result<>(new ArrayList<>(Arrays.asList("stairs", "elevator"))));
        gameMap.put("weaponInventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("difficultyMap", new Result<>(gameLevels));
        gameMap.put("level", new Result<>("easy"));
        setGameMapRoundDefaults(gameMap);
    }

    public static void setGameMapRoundDefaults(Map<String, Result<?>> gameMap) {
        gameMap.put("input", new Result<>(""));
        gameMap.put("inputData", new Result<>(""));
        gameMap.put("usedStairs", new Result<>(false));
        gameMap.put("usedElevator", new Result<>(false));
        gameMap.put("hidden", new Result<>(false));
        gameMap.put("isProcessed", new Result<>(false));
        gameMap.put("didChangeRoom", new Result<>(false));
        gameMap.put("addendumText", new Result<>(""));
        gameMap.put("itemUsed", new Result<>(""));
        gameMap.put("usedItem", new Result<>(false));
        gameMap.put("itemUsedItem", new Result<>(new Item()));
        gameMap.put("triedToUseItem", new Result<>(false));
        gameMap.put("askedForHelp", new Result<>(false));
        gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(false));
        gameMap.put("didMonsterMove", new Result<>(false));
        gameMap.put("isGameOver", new Result<>(false));
        gameMap.put("isKilledByMonster", new Result<>(false));
        gameMap.put("didUseStairs", new Result<>(false));
        gameMap.put("didUseElevator", new Result<>(false));
        gameMap.put("didGetItem", new Result<>(false));
        gameMap.put("triedToGoDirection", new Result<>(false));
        gameMap.put("triedToGetItem", new Result<>(false));
        gameMap.put("triedToHide", new Result<>(false));
        gameMap.put("triedToUseStairs", new Result<>(false));
        gameMap.put("viewLabel", new Result<>(""));
        gameMap.put("messageLabel", new Result<>(""));
        gameMap.put("shouldSaveGame", new Result<>(false));
        gameMap.put("shouldLoadGame", new Result<>(false));
        gameMap.put("dialogueLabel", new Result<>(""));
        gameMap.put("didIncreaseHealth", new Result<>(false));
        gameMap.put("isCloseToDying", new Result<>(false));
        gameMap.put("isCombat", new Result<>(false));
        gameMap.put("didFightMonster", new Result<>(false));
        gameMap.put("shouldDecreaseHealthFlag", new Result<>(false));
        gameMap.put("shouldUseItemFlag", new Result<>(false));
        gameMap.put("shouldGetItemFlag", new Result<>(false));
        gameMap.put("itemToGetItem", new Result<>(new Item()));
        gameMap.put("itemToGet", new Result<>(""));
        gameMap.put("roomToRemoveItemFrom", new Result<>(new Rooms()));
        gameMap.put("roomToChangeTo", new Result<>(new Rooms()));
        gameMap.put("shouldChangeRoomFlag", new Result<>(false));
        gameMap.put("monsterLabel", new Result<>(""));

    }
}