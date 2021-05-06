package com.runtimeterror.main;

import com.runtimeterror.model.*;
import com.runtimeterror.textparser.InputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class GameClientNewTest {
    Map<String, Result<?>> gameMap;
    GameClientNew gameClientNew;

    @Before
    public void setUp() {
        gameMap = dataSetup();
        gameClientNew = new GameClientNew(true);
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
        gameClientNew.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertTrue(askedForHelp);
    }

    @Test
    public void testProcessHelp_askedForHelpIsFalse_whenInputDataVerbIsNotHELP() {
        gameMap.put("inputData", new Result<>(new InputData("NOTHELP", "nothelp")));
        gameClientNew.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertFalse(askedForHelp);
    }

    @Test
    public void testProcessHelp_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameClientNew.processHelp(gameMap);
        boolean askedForHelp = (boolean) gameMap.get("askedForHelp").getResult();
        assertFalse(askedForHelp);
    }

    @Test
    public void testProcessStairs_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameClientNew.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsTrue_whenNounIsStairsAndRoomHasStairs() {
        gameMap.put("hasStairs", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "stairs")));
        gameClientNew.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertTrue(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsStairsAndRoomDoesNotHaveStairs() {
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("inputData", new Result<>(new InputData("USE", "stairs")));
        gameClientNew.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsNotStairsAndRoomHasStairs() {
        gameMap.put("hasStairs", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessStairs_didUseStairsIsFalse_whenNounIsNotStairsAndRoomDoesNotHaveStairs() {
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processStairs(gameMap);
        boolean didUseStairs = (boolean) gameMap.get("didUseStairs").getResult();
        assertFalse(didUseStairs);
    }

    @Test
    public void testProcessUse_WontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertFalse(triedToUseItem);
    }

    @Test
    public void testProcessUse_triedToUseItemIsTrue_whenVerbIsUSE() {
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertTrue(triedToUseItem);
    }

    @Test
    public void testProcessUse_triedToUseItemIsFalse_whenVerbIsNotUSE() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameClientNew.processUse(gameMap);
        boolean triedToUseItem = (boolean) gameMap.get("triedToUseItem").getResult();
        assertFalse(triedToUseItem);
    }

    @Test
    public void testProcessUse_shouldMonsterChangeRoomsIsTrue_whenVerbIsUSE() {
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processUse(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertTrue(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessUse_shouldMonsterChangeRoomsIsFalse_whenVerbIsNotUSE() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameClientNew.processUse(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertFalse(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessUse_hiddenIsFalse_whenVerbIsUSE() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processUse(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertFalse(hidden);
    }

    @Test
    public void testProcessUse_hiddenRemainsTrue_whenVerbIsNotUSE() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameClientNew.processUse(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertTrue(hidden);
    }

    @Test
    public void testProcessGet_hiddenRemainsTrue_whenVerbIsNotGET() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("USE", "axe")));
        gameClientNew.processGet(gameMap);
        boolean hidden = (boolean) gameMap.get("hidden").getResult();
        assertTrue(hidden);
    }

    @Test
    public void testProcessGet_hiddenRemainsTrue_whenVerbIsGETAndItemNotInRoom() {
        gameMap.put("hidden", new Result<>(true));
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameClientNew.processGet(gameMap);
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
        gameClientNew.processGet(gameMap);
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
        gameClientNew.processGet(gameMap);
        boolean didGetItem = (boolean) gameMap.get("didGetItem").getResult();
        assertTrue(didGetItem);
    }

    @Test
    public void testProcessGet_didGetItemIsFalse_whenVerbIsGETAndItemNotInRoom() {
        gameMap.put("inputData", new Result<>(new InputData("GET", "axe")));
        gameClientNew.processGet(gameMap);
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
        gameClientNew.processGet(gameMap);
        boolean shouldMonsterChangeRooms = (boolean) gameMap.get("shouldMonsterChangeRooms").getResult();
        assertTrue(shouldMonsterChangeRooms);
    }

    @Test
    public void testProcessGet_wontProcess_whenIsProcessedIsTrue() {
        gameMap.put("isProcessed", new Result<>(true));
        gameClientNew.processGet(gameMap);
        boolean didGetItem = (boolean) gameMap.get("didGetItem").getResult();
        assertFalse(didGetItem);
    }


    @Test
    public void processGet() {
    }

    @Test
    public void processGo() {
    }

    @Test
    public void processHide() {
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
        gameMap.put("input", new Result<>(""));
        gameMap.put("inputData", new Result<>(""));
        gameMap.put("hasStairs", new Result<>(false));
        gameMap.put("stairsRoom", new Result<>(new Rooms()));
        gameMap.put("usedStairs", new Result<>(false));
        gameMap.put("hidden", new Result<>(false));
        gameMap.put("isProcessed", new Result<>(false));
        gameMap.put("monsterCurrentRoom", new Result<>(new Rooms()));
        gameMap.put("playerCurrentRoom", new Result<>(new Rooms()));
        gameMap.put("didChangeRoom", new Result<>(false));
        gameMap.put("addendumText", new Result<>(""));
        gameMap.put("rooms", new Result<>(new ArrayList<Rooms>()));
        gameMap.put("itemUsed", new Result<>(""));
        gameMap.put("usedItem", new Result<>(false));
        gameMap.put("triedToUseItem", new Result<>(false));
        gameMap.put("inventory", new Result<>(new ArrayList<Item>()));
        gameMap.put("gameLoaded", new Result<>(false));
        gameMap.put("player", new Result<>(new Player()));
        gameMap.put("monster", new Result<>(new Monster()));
        gameMap.put("startingRoom", new Result<>(new Rooms()));
        gameMap.put("helpText", new Result<>("commands: HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP"));
        gameMap.put("askedForHelp", new Result<>(false));
        gameMap.put("escapeItem", new Result<>("bolt cutters"));
        gameMap.put("shouldMonsterChangeRooms", new Result<>(false));
        gameMap.put("listOfItems", new Result<>(new ArrayList<String>()));
        gameMap.put("didMonsterMove", new Result<>(false));
        gameMap.put("isGameOver", new Result<>(false));
        gameMap.put("isKilledByMonster", new Result<>(false));
        gameMap.put("didUseStairs", new Result<>(false));
        gameMap.put("didGetItem", new Result<>(false));
        return gameMap;
    }
}