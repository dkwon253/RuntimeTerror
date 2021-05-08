package com.runtimeterror.model;

import com.runtimeterror.textparser.InputData;
import com.runtimeterror.textparser.Parser;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class GameProcessor {
    Map<String, Result<?>> gameMap;

    GameProcessor(boolean test) {
        // test constructor
    }

    GameProcessor() {
    }

    void start(Map<String, Result<?>> gameMap) {
        this.gameMap = gameMap;
        Parser.parseInput(gameMap);
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
            gameMap.put("viewLabel", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP"));
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
        String stairsRoomName = (String) gameMap.get("stairsRoom").getResult();
        @SuppressWarnings("unchecked")
        Map<String, Rooms> rooms = (Map<String, Rooms>) gameMap.get("rooms").getResult();
        Rooms stairsRoom = rooms.get(stairsRoomName);
        Rooms currentRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        if (hasStairs && "stairs".equals(noun)) {
            gameMap.put("didUseStairs", new Result<>(true));
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("didChangeRoom", new Result<>(true));
            gameMap.put("playerCurrentRoom", new Result<>(stairsRoom));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("The stairs have taken you to the " + stairsRoomName + "."));
        } else if ("stairs".equals(noun)) {
            gameMap.put("viewLabel", new Result<>("There are no stairs in " + currentRoom.getRoomName() + "."));
            gameMap.put("triedToUseStairs", new Result<>(true));
        }
        return gameMap;
    }

    Map<String, Result<?>> processUse(Map<String, Result<?>> gameMap) {
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        if (isProcessed) {
            return gameMap;
        }
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        String verb = inputData.getVerb();
        if ("USE".equals(verb) && noun != null) {
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            gameMap.put("triedToUseItem", new Result<>(true));
            useItem(gameMap);
        } else if("USE".equals(verb)) {
            gameMap.put("viewLabel", new Result<>("You can't use that."));
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
        if ("GET".equals(verb) && noun != null && currentRoom.doesItemExist(noun)) {
            @SuppressWarnings("unchecked")
            List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
            inventory.add(currentRoom.getAndRemoveRoomItem(noun));
            gameMap.put("didGetItem", new Result<>(true));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You have picked up a " + noun + "."));
        } else if ("GET".equals(verb)) {
            gameMap.put("triedToGetItem", new Result<>(true));
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
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("playerCurrentRoom", new Result<>(newRoom));
            gameMap.put("didChangeRoom", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            gameMap.put("isValidDirection", new Result<>(true));
            gameMap.put("viewLabel", new Result<>("You have changed rooms. You are now in the " + newRoom.getRoomName() + "."));
        } else if ("GO".equals(verb)) {
            gameMap.put("triedToGoDirection", new Result<>(true));
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
        boolean itemExist = false;
        String lookText = "";
        System.out.println("ran");
        for (Item item : inventory) {
            if (item.getName().equals(noun)) {
                itemExist = true;
                lookText = item.getDescription();
                break;
            }
        }
        String verb = inputData.getVerb();
        if ("LOOK".equals(verb) && itemExist) {
            gameMap.put("hidden", new Result<>(false));
            gameMap.put("isProcessed", new Result<>(true));
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
            gameMap.put("viewLabel", new Result<>(lookText));
            gameMap.put("lookText", new Result<>(lookText));
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
            gameMap.put("shouldMonsterChangeRooms", new Result<>(true));
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
            gameMap.put("viewLabel", new Result<>("You dropped a(n) " + itemToAdd.getName() + " in the " + currentRoom.getRoomName()));
            gameMap.put("isProcessed", new Result<>(true));
        } else if ("DROP".equals(verb)) {
            gameMap.put("viewLabel", new Result<>("You don't have a(n) " + noun + " to drop."));
            gameMap.put("isProcessed", new Result<>(true));
        }

        return gameMap;
    }

    Map<String, Result<?>> processMoveMonster(Map<String, Result<?>> gameMap) {
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        if (shouldMonsterChangeRooms) {
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

    @SuppressWarnings("unchecked")
    void useItem(Map<String, Result<?>> gameMap) {
        InputData inputData = (InputData) gameMap.get("inputData").getResult();
        String noun = inputData.getNoun();
        System.out.println(noun);
        List<Item> inventory = (List<Item>) gameMap.get("inventory").getResult();
        Item itemToRemove = new Item();
        gameMap.put("viewLabel", new Result<>("You don't have a(n) " + noun + "."));
        for (Item item : inventory) {
            if (item != null && noun.equals(item.getName())) {
                gameMap.put("triedToUseItem", new Result<>(false));
                gameMap.put("usedItem", new Result<>(true));
                gameMap.put("itemUsed", new Result<>(noun));
                gameMap.put("viewLabel", new Result<>("You have used your " + item.getName() + "."));
                itemToRemove = item;
                break;
            }
        }
        System.out.println((String) gameMap.get("dialogueLabel").getResult());
        inventory.remove(itemToRemove);
    }
}