package com.runtimeterror.model;

import com.runtimeterror.textparser.InputData;
import com.runtimeterror.textparser.Parser;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class GameProcessor {

    GameProcessor(boolean test) {
        // test constructor
    }

    GameProcessor() {
    }

    void start(Map<String, Result<?>> gameMap) {
        Parser.parseInput(gameMap);
        processCombat(gameMap);
        processHelp(gameMap);
        processStairs(gameMap);
        processUse(gameMap);
        processGet(gameMap);
        processGo(gameMap);
        processLook(gameMap);
        processHide(gameMap);
        processDrop(gameMap);
        processMoveMonster(gameMap);
        processSaveGame(gameMap);
        processLoadGame(gameMap);
        processSkipPlayerTurn(gameMap);
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processCombat(Map<String, Result<?>> gameMap) {
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        String noun = inputData.getNoun();
        Rooms playerCurrentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        Rooms monsterCurrentRoom = (Rooms) gameMap.get("monsterCurrentRoom").getResult();
        if (playerCurrentRoom == monsterCurrentRoom) {
            List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
            List<String> listOfWeapons = (List<String>) gameMap.get("listOfWeapons").getResult();
            boolean isWeapon = listOfWeapons.stream().anyMatch(weaponItem -> weaponItem.equals(noun));
            Item item = inventory.stream().filter(invItem -> invItem.getName().equals(noun)).findFirst().orElse(null);
            if ("USE".equals(verb) && isWeapon && item != null) {
                gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
                gameMap.put("shouldUseItemFlag", new Result<>(true));
                gameMap.put("itemUsedItem", new Result<>(item));
                gameMap.put("itemUsed", new Result<>(noun));
                gameMap.put("shouldDecreaseHealthFlag", new Result<>(false));
                gameMap.put("monsterLabel", new Result<>("You fought the monster using your " + noun + " and won!"));
            } else {
                gameMap.put("monsterLabel", new Result<>("You chickened out and didn't fight the monster. You have lost health."));
                gameMap.put("shouldDecreaseHealthFlag", new Result<>(true));
                gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            }
        }
        return gameMap;
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
            gameMap.put("viewLabel", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,HELP"));
        }
        return gameMap;
    }

    Map<String, Result<?>> processStairs(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }

        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        boolean hasStairs = (boolean) gameMap.get("hasStairs").getResult();
        String stairsRoomName = (String) gameMap.get("stairsRoom").getResult();
        @SuppressWarnings("unchecked")
        Map<String, Rooms> rooms = (Map<String, Rooms>) gameMap.get("rooms").getResult();
        Rooms stairsRoom = rooms.get(stairsRoomName);
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        if (hasStairs && "stairs".equals(noun)) {
            gameMap.put("roomToChangeTo", new Result<>(stairsRoom));
            gameMap.put("shouldChangeRoomFlag", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("The stairs have taken you to the " + stairsRoomName + "."));
        } else if ("stairs".equals(noun)) {
            gameMap.put("viewLabel", new Result<>("There are no stairs in " + currentRoom.getRoomName() + "."));
        }
        return gameMap;
    }

    @SuppressWarnings("unchecked")
    Map<String, Result<?>> processUse(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        String verb = inputData.getVerb();
        List<String> nonUseItems = (List<String>) gameMap.get("nonUseItems").getResult();
        List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
        if ("USE".equals(verb) && !nonUseItems.contains(noun)) {
            Item foundItem = inventory.stream().filter((item) -> item.getName().equals(noun)).findFirst().orElse(null);
            if (foundItem != null) {
                gameMap.put("isProcessed", new Result<>(true));
                gameMap.put("shouldUseItemFlag", new Result<>(true));
                gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
                gameMap.put("usedItem", new Result<>(true));
                gameMap.put("itemUsed", new Result<>(noun));
                gameMap.put("itemUsedItem", new Result<>(foundItem));
                gameMap.put("viewLabel", new Result<>("You have used your " + foundItem.getName() + "."));
            } else {
                gameMap.put("isProcessed", new Result<>(true));
                gameMap.put("viewLabel", new Result<>("You don't have a(n) " + noun + "."));
            }
        }
        return gameMap;
    }

    Map<String, Result<?>> processGet(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        String noun = inputData.getNoun();
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        Item roomItem = currentRoom.doesItemExist(noun);
        if ("GET".equals(verb) && noun != null && roomItem != null) {
            gameMap.put("roomToRemoveItemFrom", new Result<>(currentRoom));
            gameMap.put("itemToGet", new Result<>(noun));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You have picked up a " + noun + "."));
            gameMap.put("shouldGetItemFlag", new Result<>(true));
        } else if ("GET".equals(verb)) {
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("The " + currentRoom.getRoomName() + " does not have that item."));
        }
        return gameMap;
    }

    Map<String, Result<?>> processGo(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        @SuppressWarnings("unchecked")
        Map<String, Rooms> availableRooms = (HashMap<String, Rooms>) gameMap.get("availableRooms").getResult();
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        String verb = inputData.getVerb();
        Rooms newRoom = availableRooms.get(noun);
        if ("GO".equals(verb) && newRoom != null) {
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("roomToChangeTo", new Result<>(newRoom));
            gameMap.put("shouldChangeRoomFlag", new Result<>(true));
            gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You have changed rooms. You are now in the " + newRoom.getRoomName() + "."));
        } else if ("GO".equals(verb)) {
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You can't go that way!"));
        }
        return gameMap;
    }

    Map<String, Result<?>> processLook(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        @SuppressWarnings("unchecked")
        List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
        Item foundItem = inventory.stream().filter(item -> item.getName().equals(noun)).findFirst().orElse(null);
        String verb = inputData.getVerb();
        if ("LOOK".equals(verb) && foundItem != null) {
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            gameMap.put("viewLabel", new Result<>(foundItem.getDescription()));
        } else if ("LOOK".equals(verb)) {
            gameMap.put("triedToLook", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You don't have that item in your inventory"));
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
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        if ("HIDE".equals(verb) && currentRoom.getHidingLocation() != null) {
            gameMap.put("hidden", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You have hidden inside the " + currentRoom.getHidingLocation() + "."));
        } else if ("HIDE".equals(verb)) {
            gameMap.put("triedToHide", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("There are no hiding spots in the " + currentRoom.getRoomName() + "."));
        }
        return gameMap;
    }

    Map<String, Result<?>> processDrop(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        String noun = inputData.getNoun();
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        @SuppressWarnings("unchecked")
        List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
        Item itemToAdd = inventory.stream()
                .filter(item -> item.getName().equals(noun))
                .findFirst().orElse(null);

        if ("DROP".equals(verb) && itemToAdd != null) {
            currentRoom.addItem(itemToAdd);
            inventory.remove(itemToAdd);
            gameMap.put("viewLabel", new Result<>("You dropped a(n) " + itemToAdd.getName() + " in the "
                    + currentRoom.getRoomName()));
            gameMap.put("isProcessed", new Result<>(true));
        } else if ("DROP".equals(verb)) {
            gameMap.put("viewLabel", new Result<>("You don't have a(n) " + noun + " to drop."));
            gameMap.put("isProcessed", new Result<>(true));
        }

        return gameMap;
    }

    Map<String, Result<?>> processMoveMonster(Map<String, Result<?>> gameMap) {
        boolean shouldMonsterChangeRoomFlag = (boolean) gameMap.get("shouldMonsterChangeRoomFlag").getResult();
        if (shouldMonsterChangeRoomFlag) {
            Random r = new Random();
            @SuppressWarnings("unchecked")
            Map<String, Rooms> allRooms = (HashMap<String, Rooms>) gameMap.get("rooms").getResult();
            List<Rooms> allRoomsList = new ArrayList<>(allRooms.values());
            int randomInt = r.nextInt(allRoomsList.size() - 1) + 1;
            Rooms monsterNewRoom = allRoomsList.get(randomInt);
            gameMap.put("monsterCurrentRoom", new Result<>(monsterNewRoom));
            gameMap.put("didMonsterMove", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processSaveGame(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        if ("SAVE".equals(verb)) {
            gameMap.put("shouldSaveGame", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
        }

        return gameMap;
    }

    Map<String, Result<?>> processLoadGame(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String verb = inputData.getVerb();
        boolean gameLoaded = (boolean) gameMap.get("gameLoaded").getResult();
        if ("LOAD".equals(verb) && !gameLoaded) {
            gameMap.put("shouldLoadGame", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processSkipPlayerTurn(Map<String, Result<?>> gameMap) {
        return gameMap;
    }
}