package com.runtimeterror.model;

import com.runtimeterror.textparser.InputData;

import java.io.*;
import java.util.*;

class PostGameProcessor {

    Map<String, Result<?>> start(Map<String, Result<?>> gameMap) {
        processRoomChange(gameMap);
        processMonsterRoomChange(gameMap);
        processHealthIncrease(gameMap);
        processEscape(gameMap);
        processGetItem(gameMap);
        processSavingGameState(gameMap);
        processHealthDecrease(gameMap);
        processUseItem(gameMap);
        processGameOverCheck(gameMap);
        processGetMessageLabel(gameMap);
        return processLoadingGameState(gameMap);
    }

    Map<String, Result<?>> processRoomChange(Map<String, Result<?>> gameMap) {
        boolean shouldChangeRoomFlag = (boolean) gameMap.get("shouldChangeRoomFlag").getResult();
        if (shouldChangeRoomFlag) {
            Rooms newRoom = (Rooms) gameMap.get("roomToChangeTo").getResult();
            gameMap.put("playerCurrentRoom", new Result<>(newRoom));
            gameMap.put("hasStairs", new Result<>(newRoom.hasStairs()));
            gameMap.put("stairsRoom", new Result<>(newRoom.getStairsNeighborName()));
            gameMap.put(("availableRooms"), new Result<>(newRoom.getRoomNeighbors()));
        }
        return gameMap;
    }

    Map<String, Result<?>> processHealthIncrease(Map<String, Result<?>> gameMap) {
        boolean usedItem = (boolean) gameMap.get("usedItem").getResult();
        Item itemUsedItem = (Item) gameMap.get("itemUsedItem").getResult();
        if (usedItem && itemUsedItem.getType().equals("health")) {
            int playerHealth = (int) gameMap.get("playerHealth").getResult();
            gameMap.put("playerHealth", new Result<>(playerHealth + 5));
            gameMap.put("viewLabel", new Result<>("You have increased your health!"));
            gameMap.put("didIncreaseHealth", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processEscape(Map<String, Result<?>> gameMap) {
        boolean usedItem = (boolean) gameMap.get("usedItem").getResult();
        Item itemUsedItem = (Item) gameMap.get("itemUsedItem").getResult();
        Rooms playerCurrentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        if (usedItem && "escape".equals(itemUsedItem.getType()) && "escape".equals(playerCurrentRoom.getRoomType())) {
            gameMap.put("viewLabel", new Result<>("You have used the " + itemUsedItem.getName() + " to escape!"));
            gameMap.put("isGameOver", new Result<>(true));
        }
        return gameMap;
    }


    Map<String, Result<?>> processGetMessageLabel(Map<String, Result<?>> gameMap) {
        String viewLabel = (String) gameMap.get("viewLabel").getResult();
        gameMap.put("messageLabel", new Result<>(viewLabel));
        return gameMap;
    }

    Map<String, Result<?>> processSavingGameState(Map<String, Result<?>> gameMap) {
        boolean shouldSaveGame = (boolean) gameMap.get("shouldSaveGame").getResult();
        if (!shouldSaveGame) {
            return gameMap;
        }
        HashMap<String, Object> gameObjects = new HashMap<>();
        gameObjects.put("gameMap", gameMap);
        try {
            FileOutputStream fos = new FileOutputStream("Game/gameData/savedGameData.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameObjects);
            oos.flush();
            oos.close();
            fos.close();
            gameMap.put("viewLabel", new Result<>("Your game has been saved."));
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load the game files:");
            gameMap.put("viewLabel", new Result<>("The game failed to save."));
            System.out.println(e.getMessage());
        } catch (IOException e) {
            gameMap.put("viewLabel", new Result<>("The game failed to save."));
            e.printStackTrace();
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processLoadingGameState(Map<String, Result<?>> gameMap) {
        boolean shouldLoadGame = (boolean) gameMap.get("shouldLoadGame").getResult();
        if (!shouldLoadGame) {
            return gameMap;
        }
        try {
            FileInputStream fis = new FileInputStream("Game/gameData/savedGameData.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, Object> data = (HashMap<String, Object>) ois.readObject();
            fis.close();
            gameMap = (Map<String, Result<?>>) data.get("gameMap");
            gameMap.put("messageLabel", new Result<>("Previous game loaded."));
        } catch (Exception e) {
            return gameMap;
        }
        return gameMap;
    }

    Map<String, Result<?>> processHealthDecrease(Map<String, Result<?>> gameMap) {
        boolean shouldDecreaseHealthFlag = (boolean) gameMap.get("shouldDecreaseHealthFlag").getResult();
        if (shouldDecreaseHealthFlag) {
            int playerHealth = (int) gameMap.get("playerHealth").getResult();
            int monsterDamage = (int) gameMap.get("monsterDamage").getResult();
            int lowPlayerHealth = (int) gameMap.get("lowPlayerHealth").getResult();
            playerHealth -= monsterDamage;
            gameMap.put("playerHealth", new Result<>(playerHealth));
            gameMap.put("isCloseToDying", new Result<>(playerHealth == lowPlayerHealth));
        }
        return gameMap;
    }

    Map<String, Result<?>> processMonsterRoomChange(Map<String, Result<?>> gameMap) {
        boolean shouldMonsterChangeRoomFlag = (boolean) gameMap.get("shouldMonsterChangeRoomFlag").getResult();
        if(shouldMonsterChangeRoomFlag) {
            Rooms newRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
            Queue<Rooms> roomsQueue = new LinkedList<>();
            @SuppressWarnings("unchecked")
            HashMap<String, Rooms> allRooms = (HashMap<String, Rooms>) gameMap.get("rooms").getResult();
            Rooms rooms = allRooms.get("Master Bedroom");
            List<Rooms> monsterAvailableRooms = new ArrayList<>();
            roomsQueue.add(newRoom);
            Rooms room;
            while (roomsQueue.size() > 0) {
                room = roomsQueue.remove();
                if (!monsterAvailableRooms.contains(room)) {
                    monsterAvailableRooms.add(room);
                    Collection<Rooms> innerRooms = allRooms.get(room.getRoomName()).getRoomNeighbors().values();
                    innerRooms.stream().filter(Objects::nonNull).forEach(roomsQueue::add);
                }
            }
            int randomInt = new Random().nextInt(monsterAvailableRooms.size());
            //gameMap.put(("monsterCurrentRoom"), new Result<>(monsterAvailableRooms.get(randomInt)));
            gameMap.put(("monsterCurrentRoom"), new Result<>(rooms));
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processGetItem(Map<String, Result<?>> gameMap) {
        boolean shouldGetItemFlag = (boolean) gameMap.get("shouldGetItemFlag").getResult();
        if (shouldGetItemFlag) {
            Rooms roomToRemoveItemFrom = (Rooms) gameMap.get("roomToRemoveItemFrom").getResult();
            String itemString = (String) gameMap.get("itemToGet").getResult();
            List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
            inventory.add(roomToRemoveItemFrom.getAndRemoveRoomItem(itemString));
        }
        return gameMap;
    }

    Map<String, Result<?>> processUseItem(Map<String, Result<?>> gameMap) {
        boolean shouldUseItemFlag = (boolean) gameMap.get("shouldUseItemFlag").getResult();
        if (shouldUseItemFlag) {
            Item item = (Item) gameMap.get("itemUsedItem").getResult();
            @SuppressWarnings("unchecked")
            List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
            inventory.remove(item);
        }
        return gameMap;
    }

    Map<String, Result<?>> processGameOverCheck(Map<String, Result<?>> gameMap) {
        int playerHealth = (int) gameMap.get("playerHealth").getResult();
        if (playerHealth <= 0) {
            gameMap.put("isGameOver", new Result<>(true));
            gameMap.put("isKilledByMonster", new Result<>(true));
        }
        return gameMap;
    }

}