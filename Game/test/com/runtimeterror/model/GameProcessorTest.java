package com.runtimeterror.model;

import com.runtimeterror.model.*;
import com.runtimeterror.textparser.InputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

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
    public void submitPlayerString() {
    }

    @Test
    public void gameProcessor() {
    }

    @Test
    public void testProcessHelp_askedForHelpIsTrue_whenInputDataVerbIsHELP() {
        gameMap.put("inputData", new Result<>(new InputData("HELP", "help")));
        gameProcessor.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertTrue(askedForHelp);
    }

    @Test
    public void testProcessHelp_askedForHelpIsFalse_whenInputDataVerbIsNotHELP() {
        gameMap.put("inputData", new Result<>(new InputData("NOTHELP", "nothelp")));
        gameProcessor.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertFalse(askedForHelp);
    }

    @Test
    public void testProcessHelp_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameProcessor.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertFalse(askedForHelp);
    }

    @Test
    public void testProcessStairs_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameProcessor.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsTrue_whenNounIsStairsAndRoomHasStairs() {
        gameMap.put("hasStairs", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "stairs")));
        gameProcessor.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertTrue(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsStairsAndRoomDoesNotHaveStairs() {
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("inputData", new Result<>(new InputData("USE", "stairs")));
        gameProcessor.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsNotStairsAndRoomHasStairs() {
        gameMap.put("hasStairs", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsNotStairsAndRoomDoesNotHaveStairs() {
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessUse_WontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertFalse(triedToUseItem);
    }

    @Test
    public void testProcessUse_triedToUseItemIsTrue_whenVerbIsUSE() {
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertTrue(triedToUseItem);
    }

    @Test
    public void testProcessUse_triedToUseItemIsFalse_whenVerbIsNotUSE() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameProcessor.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertFalse(triedToUseItem);
    }

    @Test
    public void testProcessUse_shouldMonsterChangeRoomsIsTrue_whenVerbIsUSE() {
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processUse(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertTrue(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessUse_shouldMonsterChangeRoomsIsFalse_whenVerbIsNotUSE() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameProcessor.processUse(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertFalse(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessUse_hiddenIsFalse_whenVerbIsUSE() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processUse(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertFalse(hidden);
    }

    @Test
    public void testProcessUse_hiddenRemainsTrue_whenVerbIsNotUSE() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameProcessor.processUse(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertTrue(hidden);
    }

    @Test
    public void testProcessGet_hiddenRemainsTrue_whenVerbIsNotGET() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameProcessor.processGet(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertTrue(hidden);
    }

    @Test
    public void testProcessGet_hiddenRemainsTrue_whenVerbIsGETAndItemNotInRoom() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameProcessor.processGet(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertTrue(hidden);
    }

    @Test
    public void testProcessGet_hiddenIsFalse_whenVerbIsGETAndItemInRoom() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        Rooms newRoom = new Rooms();
        Item newItem = new Item("axe", "axe", "sharp axe");
        newRoom.setItem(newItem);
        gameMap.put("playerCurrentRoom", new Result<>(newRoom));
        gameProcessor.processGet(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertFalse(hidden);
    }

    @Test
    public void testProcessGet_didGetItemIsTrue_whenVerbIsGETAndItemInRoom() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        Rooms newRoom = new Rooms();
        Item newItem = new Item("axe", "axe", "sharp axe");
        newRoom.setItem(newItem);
        gameMap.put("playerCurrentRoom", new Result<>(newRoom));
        gameProcessor.processGet(gameMap);
        boolean didGetItem = (boolean) gameMap.get("didGetItem").getResult();
        assertTrue(didGetItem);
    }

    @Test
    public void testProcessGet_didGetItemIsFalse_whenVerbIsGETAndItemNotInRoom() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameProcessor.processGet(gameMap);
        boolean didGetItem = (boolean) gameMap.get("didGetItem").getResult();
        assertFalse(didGetItem);
    }

    @Test
    public void testProcessGet_shouldMonsterChangeRoomsIsTrue_whenVerbIsGETAndItemInRoom() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        Rooms newRoom = new Rooms();
        Item newItem = new Item("axe", "axe", "sharp axe");
        newRoom.setItem(newItem);
        gameMap.put("playerCurrentRoom", new Result<>(newRoom));
        gameProcessor.processGet(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertTrue(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessGet_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameProcessor.processGet(gameMap);
        boolean didGetItem = (boolean) gameMap.get("didGetItem").getResult();
        assertFalse(didGetItem);
    }

    @Test
    public void testProcessGo_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameProcessor.processGo(gameMap);
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();
        assertFalse(didChangeRoom);
    }

    @Test
    public void testProcessGo_playerCurrentRoomShouldChange_whenVerbIsGOAndDirectionIsValid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GO", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("east", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        Rooms playerCurrentRoomAfterRun = (Rooms) gameMap.get("playerCurrentRoom").getResult();

        assertNotSame(playerCurrentRoomBeforeRun, playerCurrentRoomAfterRun);
    }

    @Test
    public void testProcessGo_shouldMonsterChangeRoomsIsTrue_whenVerbIsGOAndDirectionIsValid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GO", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("east", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();

        assertTrue(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessGo_didChangeRoomIsTrue_whenVerbIsGOAndDirectionIsValid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GO", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("east", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();

        assertTrue(didChangeRoom);
    }

    @Test
    public void testProcessGo_hiddenIsFalse_whenVerbIsGOAndDirectionIsValid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GO", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("east", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();

        assertFalse(hidden);
    }

    @Test
    public void testProcessGo_didChangeRoomIsFalse_whenVerbIsGOAndDirectionIsInvalid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GO", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("west", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();

        assertFalse(didChangeRoom);
    }

    @Test
    public void testProcessGo_didChangeRoomIsFalse_whenVerbIsNotGoAndDirectionIsValid() {
        Rooms playerCurrentRoomBeforeRun = new Rooms();
        gameMap.put("inputData", new Result<>(new InputData("GET", "east")));
        Rooms validRoom = new Rooms();
        Map<String, Rooms> availableRooms = new HashMap<>();
        availableRooms.put("east", validRoom);
        gameMap.put("availableRooms", new Result<>(availableRooms));
        gameMap.put("playerCurrentRoom", new Result<>(playerCurrentRoomBeforeRun));
        gameProcessor.processGo(gameMap);
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();

        assertFalse(didChangeRoom);
    }


    @Test
    public void processHide_wontProcess_w() {
        gameMap.put("isProcessed", new Result<>(true));
        gameProcessor.processGo(gameMap);
        boolean didChangeRoom = (boolean) gameMap.get("didChangeRoom").getResult();
        assertFalse(didChangeRoom);
    }

    @Test
    public void processMoveMonster() {
    }

    @Test
    public void processSaveGame() {
    }

    @Test
    public void processLoadGame() {
    }

    @Test
    public void processSkipPlayerTurn() {
    }

    @Test
    public void processMonsterEncounter() {
    }

    @Test
    public void processRoomChange() {
    }

    @Test
    public void getGameMap() {
    }

    @Test
    public void getRoomText() {
    }

    @Test
    public void getPLayerInventory() {
    }

    @Test
    public void getMonsterLocation() {
    }

    @Test
    public void reset() {
    }

    @Test
    public void getRoomImagePath() {
    }

    @Test
    public void getPLayerStatus() {
    }

    @Test
    public void getMapImagePath() {
    }

    @Test
    public void isGameOver() {
    }

    @Test
    public void isKilledByMonster() {
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
        gameMap.put("player", new Result<>(new Player()));
        gameMap.put("monster", new Result<>(new Monster()));
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
        return gameMap;
    }
}