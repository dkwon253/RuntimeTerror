package com.runtimeterror.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PostGameProcessorTest {
    Map<String, Result<?>> gameMap;
    PostGameProcessor postGameProcessor;


    @Before
    public void setUp() {
        gameMap = new HashMap<>();
        postGameProcessor = new PostGameProcessor();
    }

    @Test
    public void testProcessRoomChange_playerCurrentRoomShouldChange_whenShouldChangeRoomFlagIsTrue() {
        gameMap.put("shouldChangeRoomFlag", new Result<>(true));
        Rooms preRoom = new Rooms();
        Rooms roomToChangeTo = new Rooms();
        gameMap.put("playerCurrentRoom", new Result<>(preRoom));
        gameMap.put("roomToChangeTo", new Result<>(roomToChangeTo));
        postGameProcessor.processRoomChange(gameMap);

        Rooms postRoom = (Rooms) gameMap.get("roomToChangeTo").getResult();
        assertNotSame(preRoom, postRoom);
    }

    @Test
    public void testProcessRoomChange_playerCurrentRoomShouldNotChange_whenShouldChangeRoomFlagIsFalse() {
        Rooms preRoom = new Rooms();
        gameMap.put("playerCurrentRoom", new Result<>(preRoom));
        gameMap.put("shouldChangeRoomFlag", new Result<>(false));
        postGameProcessor.processRoomChange(gameMap);
        Rooms postRoom = (Rooms) gameMap.get("playerCurrentRoom").getResult();
        assertSame(preRoom, postRoom);
    }

    @Test
    public void testProcessHealthIncrease_healthShouldIncrease_whenUsedItemIsTrueAndItemUsedIsHealthType() {
        Item item = new Item("", "health", "", "");
        gameMap.put("usedItem", new Result<>(true));
        gameMap.put("itemUsedItem", new Result<>(item));
        int prePlayerHealth = 10;
        gameMap.put("playerHealth", new Result<>(prePlayerHealth));

        postGameProcessor.processHealthIncrease(gameMap);
        int postPlayerHealth = (int) gameMap.get("playerHealth").getResult();
        assertNotEquals(prePlayerHealth, postPlayerHealth);

    }



    @After
    public void cleanUp() {
    }
}