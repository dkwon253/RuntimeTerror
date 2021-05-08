package com.runtimeterror.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


class PostGameProcessor {

    Map<String, Result<?>> start(Map<String, Result<?>> gameMap) {
        processMonsterEncounter(gameMap);
        processRoomChange(gameMap);
        processHealthIncrease(gameMap);
        processGameOverCheck(gameMap);
        processSavingGameState(gameMap);
        processGetMessageLabel(gameMap);
        return processLoadingGameState(gameMap);
    }

    Map<String, Result<?>> processMonsterEncounter(Map<String, Result<?>> gameMap) {
        boolean didMonsterMove = (boolean) gameMap.get("didMonsterMove").getResult();
        if (didMonsterMove) {
            Rooms playerCurrentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
            Rooms monsterCurrentRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
            if (playerCurrentRoom == monsterCurrentRoom) {
                int playerHealth = (int) gameMap.get("playerHealth").getResult();
                int monsterDamage = (int) gameMap.get("monsterDamage").getResult();
                gameMap.put("playerHealth", new Result<>(playerHealth - monsterDamage));
                gameMap.put("viewLabel", new Result<>("The monster is here!"));
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

    Map<String, Result<?>> processGameOverCheck(Map<String, Result<?>> gameMap) {
        int playerHealth = (int) gameMap.get("playerHealth").getResult();
        if(playerHealth <= 0) {
            gameMap.put("isGameOver", new Result<>(true));
            gameMap.put("isKilledByMonster", new Result<>(true));
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
            System.out.println("loading....");
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, Object> data = (HashMap<String, Object>) ois.readObject();
            fis.close();
            System.out.println(gameMap);
            gameMap = (Map<String, Result<?>>) data.get("gameMap");
            gameMap.put("messageLabel", new Result<>("Previous game loaded."));
        } catch (Exception e) {
            return gameMap;
        }
        return gameMap;
    }

}