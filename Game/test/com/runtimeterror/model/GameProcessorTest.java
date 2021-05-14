package com.runtimeterror.model;

import com.runtimeterror.model.*;
import com.runtimeterror.textparser.InputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameProcessorTest {
    Map<String, Result<?>> gameMap;
    GameProcessor gameProcessor;

    @Before
    public void setUp() {
        gameMap = dataSetup();
        gameProcessor = new GameProcessor(true);
    }

    @After
    public void cleanUp() {
        gameMap = dataSetup();
    }

    @Test
    public void testProcessCombat_shouldDecreaseHealthIsFalse_whenPlayerUseWeapon() {
        Rooms sameRoom = new Rooms();

        InputData  inputData = new InputData("USE", "gun");
        Item item = new Item("gun", "", "", "");
        gameMap.put("monsterCurrentRoom", new Result<>(sameRoom));
        gameMap.put("playerCurrentRoom", new Result<>(sameRoom));
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("inventory", new Result<>(new ArrayList<>(List.of(item))));
        gameMap.put("listOfWeapons", new Result<>(new ArrayList<>(List.of("gun"))));

        gameProcessor.processCombat(gameMap);

        boolean shouldDecreaseHealthFlag = (boolean) gameMap.get("shouldDecreaseHealthFlag").getResult();
        assertFalse(shouldDecreaseHealthFlag);

    }

    @Test
    public void testProcessCombat_shouldDecreaseHealthIsTrue_whenPlayerDoesNotUseWeapon() {
        Rooms sameRoom = new Rooms();
        InputData inputData = new InputData("GO", "east");
        gameMap.put("monsterCurrentRoom", new Result<>(sameRoom));
        gameMap.put("playerCurrentRoom", new Result<>(sameRoom));



        gameProcessor.processCombat(gameMap);

        boolean shouldDecreaseHealthFlag = (boolean) gameMap.get("shouldDecreaseHealthFlag").getResult();
        assertTrue(shouldDecreaseHealthFlag);

    }

    @Test
    public void testProcessGo_shouldChangeRoomFlagIsTrue_whenVerbIsGoAndNounIsValidDirection() {
        InputData inputData = new InputData("GO", "east");
        gameMap.put("inputData", new Result<>(inputData));
        Rooms room = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("east", room));
        gameMap.put("availableRooms", new Result<>(availableRooms));

        gameProcessor.processGo(gameMap);
        boolean shouldChangeRoom = (boolean) gameMap.get("shouldChangeRoomFlag").getResult();
        assertTrue(shouldChangeRoom);

    }

    @Test

    public void testProcessHelp_askedForHelpShouldBeTrue_whenVerbIsHelp() {
        InputData inputData = new InputData("HELP", null);
        gameMap.put("inputData", new Result<>(inputData));

        gameProcessor.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertTrue(askedForHelp);
    }

    @Test

    public void testProcessHelp_isProcessedIsTrue_whenVerbIsHelp() {
        InputData inputData = new InputData("HELP", null);
        gameMap.put("inputData", new Result<>(inputData));

        gameProcessor.processHelp(gameMap);
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        assertTrue(isProcessed);
    }
    @Test
    public void testProcessUse_isProcessedIsTrue_whenNounIsItem(){

        InputData  inputData = new InputData("USE", "gun");
        Item item = new Item("gun", "", "", "");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("inventory", new Result<>(new ArrayList<>(List.of(item))));

        gameProcessor.processUse(gameMap);
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        assertTrue(isProcessed);
    }

    @Test
    public void testProcessUse_shouldUseItemFlagIsTrue_whenNounIsItem(){

        InputData  inputData = new InputData("USE", "gun");
        Item item = new Item("gun", "", "", "");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("inventory", new Result<>(new ArrayList<>(List.of(item))));

        gameProcessor.processUse(gameMap);
        boolean shouldUseItem = (boolean) gameMap.get("shouldUseItemFlag").getResult();
        assertTrue(shouldUseItem);
    }
    @Test
    public void testProcessUse_shouldBeFalse_whenNounNonUseItem(){
        InputData  inputData = new InputData("USE", "stairs");
        Item item = new Item("gun", "", "", "");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("inventory", new Result<>(new ArrayList<>(List.of(item))));

        gameProcessor.processUse(gameMap);
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        assertFalse(isProcessed);

    }

    @Test
    public void testProcessGet_isProcessedIsTrue_whenNounIsRoomItem(){
        Rooms room = new Rooms();

        gameMap.put("playerCurrentRoom", new Result<>(room));
        gameMap.put("roomToRemoveItemFrom", new Result<>(room));
        gameMap.put("itemToGet", new Result<>("map"));
        InputData  inputData = new InputData("GET", "map");
        gameMap.put("inputData", new Result<>(inputData));
        gameProcessor.processGet(gameMap);
        boolean isProcessed = (boolean) gameMap.get("isProcessed").getResult();
        assertTrue(isProcessed);

    }


    @Test
    public void testProcessStairs_shouldChangeRoomFlagIsTure_whenNounIsStairsAndHasStairs(){
        InputData inputData = new InputData("TAKE", "stairs");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasStairs", new Result<>(true));
        gameMap.put("stairsRoom", new Result<>("Storage"));
        Rooms stairsRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", stairsRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processStairs(gameMap);
        boolean result = (boolean) gameMap.get("shouldChangeRoomFlag").getResult();
        assertTrue(result);
    }

    @Test
    public void testProcessStairs_viewLabelShowNoStairs_whenNounIsStairsButNoStairs(){
        InputData inputData = new InputData("TAKE", "stairs");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("stairsRoom", new Result<>("Storage"));
        Rooms stairsRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", stairsRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processStairs(gameMap);
        String result = (String) gameMap.get("viewLabel").getResult();
        assertTrue(result.equals("There are no stairs in Floor Two Hall."));
    }

    @Test
    public void testProcessStairs_isProcessedIsFalse_whenNounIsNotStairs(){
        InputData inputData = new InputData("TAKE", "elevator");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("stairsRoom", new Result<>("Storage"));
        Rooms stairsRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "String elevatorNeighbor","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", stairsRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processStairs(gameMap);
        boolean result = (boolean) gameMap.get("isProcessed").getResult();
        assertFalse(result);
    }

    @Test
    public void testProcessElevator_shouldChangeRoomFlagTrue_whenNounIsElevatorAndHasElevator(){
        InputData inputData = new InputData("TAKE", "elevator");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasElevator", new Result<>(true));
        gameMap.put("elevatorRoom", new Result<>("Storage"));
        Rooms elevatorRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "Floor Two Hall","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "Storage","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", elevatorRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processElevator(gameMap);
        boolean result = (boolean) gameMap.get("shouldChangeRoomFlag").getResult();
        assertTrue(result);
    }

    @Test
    public void testProcessElevator_viewLabelShowNoElevator_whenNounIsElevatorAndNoElevator(){
        InputData inputData = new InputData("TAKE", "elevator");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasElevator", new Result<>(false));
        gameMap.put("elevatorRoom", new Result<>("Storage"));
        Rooms elevatorRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "Floor Two Hall","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "Storage","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", elevatorRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processElevator(gameMap);
        String result = (String) gameMap.get("viewLabel").getResult();
        assertTrue(result.equals("There is no elevator in Floor Two Hall."));
    }

    @Test
    public void testProcessElevator_isProcessedIsFalse_whenNounIsNotElevator(){
        InputData inputData = new InputData("TAKE", "stairs");
        gameMap.put("inputData", new Result<>(inputData));
        gameMap.put("hasElevator", new Result<>(false));
        gameMap.put("elevatorRoom", new Result<>("Storage"));
        Rooms elevatorRoom = new Rooms("Storage", "", "", null, "String path", "String mPath",
                "Floor Two Hall", "Floor Two Hall","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        Rooms currentRoom = new Rooms("Floor Two Hall", "", "", null, "String path", "String mPath",
                "Storage", "Storage","String dialogueItem", "String dialogueFirst", "String " +
                "dialogueSecond",
                "String roomType");
        gameMap.put("playerCurrentRoom", new Result<>(currentRoom));
        Map<String, Rooms> availableRooms = new HashMap<>(Map.of("Storage", elevatorRoom));
        gameMap.put("rooms", new Result<>(availableRooms));
        gameProcessor.processElevator(gameMap);
        boolean result = (boolean) gameMap.get("isProcessed").getResult();
        assertFalse(result);
    }


    private Map<String, Result<?>> dataSetup() {
        Map<String, Result<?>> gameMap = new HashMap<>();
        Rooms defaultRoom = new Rooms();
        gameMap.put("monsterCurrentRoom", new Result<>(new Rooms()));
        gameMap.put("playerCurrentRoom", new Result<>(defaultRoom));
        gameMap.put("availableRooms", new Result<>(defaultRoom.getRoomNeighbors()));
        gameMap.put("rooms", new Result<>(new HashMap<String, Rooms>()));
        gameMap.put("triedToUseItem", new Result<>(false));
        gameMap.put("inventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("gameLoaded", new Result<>(false));
        gameMap.put("hasStairs", new Result<>(defaultRoom.hasStairs()));
        gameMap.put("stairsRoom", new Result<>(defaultRoom.getStairsNeighborName()));
        gameMap.put("startingRoom", new Result<>(defaultRoom));
        gameMap.put("helpText", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP"));
        gameMap.put("escapeItem", new Result<>("bolt cutters"));
        gameMap.put("listOfItems", new Result<>(new ArrayList<String>()));
        gameMap.put("input", new Result<>(""));
        gameMap.put("inputData", new Result<>(""));
        gameMap.put("usedStairs", new Result<>(false));
        gameMap.put("hidden", new Result<>(false));
        gameMap.put("isProcessed", new Result<>(false));
        gameMap.put("didChangeRoom", new Result<>(false));
        gameMap.put("addendumText", new Result<>(""));
        gameMap.put("itemUsed", new Result<>(""));
        gameMap.put("usedItem", new Result<>(false));
        gameMap.put("askedForHelp", new Result<>(false));
        gameMap.put("shouldMonsterChangeRoomFlag", new Result<>(false));
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
        gameMap.put("shouldSaveGame", new Result<>(false));
        gameMap.put("shouldLoadGame", new Result<>(false));
        gameMap.put("playerHealth", new Result<>(15));
        gameMap.put("monsterDamage", new Result<>(5));
        gameMap.put("dialogueLabel", new Result<>(""));
        gameMap.put("itemUsedItem", new Result<>(new Item()));
        gameMap.put("didIncreaseHealth", new Result<>(false));
        gameMap.put("timeToEndGame", new Result<>(300));
        gameMap.put("listOfWeapons", new Result<>(new ArrayList<String>()));
        gameMap.put("isCloseToDying", new Result<>(false));
        gameMap.put("isCombat", new Result<>(false));
        gameMap.put("lowPlayerHealth", new Result<>(5));
        gameMap.put("didFightMonster", new Result<>(false));
        gameMap.put("shouldDecreaseHealthFlag", new Result<>(false));
        gameMap.put("shouldUseItemFlag", new Result<>(false));
        gameMap.put("nonUseItems", new Result<>(new ArrayList<>(Arrays.asList("stairs", "elevator"))));
        gameMap.put("weaponInventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("itemToGetItem", new Result<>(new Item()));
        gameMap.put("itemToGet", new Result<>(""));
        gameMap.put("roomToRemoveItemFrom", new Result<>(new Rooms()));
        gameMap.put("roomToChangeTo", new Result<>(new Rooms()));
        gameMap.put("shouldChangeRoomFlag", new Result<>(false));
        gameMap.put("monsterLabel", new Result<>(""));
        List<Integer> easy = List.of(20, 4, 600);
        List<Integer> medium = List.of(17, 3, 500);
        List<Integer> hard = List.of(15, 2, 400);
        HashMap<String, List<Integer>> gameLevels = new HashMap<>();
        gameLevels.put("easy", easy);
        gameLevels.put("medium", medium);
        gameLevels.put("hard", hard);
        gameMap.put("difficultyMap", new Result<>(gameLevels));
        gameMap.put("level", new Result<>("easy"));
        gameMap.put("shouldDropItem", new Result<>(false));
        gameMap.put("roomToPutItem", new Result<>(new Rooms()));
        gameMap.put("itemToAddToRoom", new Result<>(new Item()));
        return gameMap;
    }
}