package com.runtimeterror.textparser;

import com.runtimeterror.model.Item;
import com.runtimeterror.model.Result;
import com.runtimeterror.model.Rooms;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ParserTest {

    //Parser parser = new Parser();
    Map<String, Result<?>> gameMap;

    @Before
    public void setUp() throws Exception {
        gameMap = dataSetup();
    }

    @Test
    public void parseInput() {
        Result<String> input = new Result<>("get axe");
        gameMap.put("input", input);
        InputData result = (InputData) Parser.parseInput(gameMap).get("inputData").getResult();
        InputData expected = new InputData("GET", "axe");
        assertTrue(expected.equals(result));
    }

    @Test
    public void findVerbGet() {
        assertEquals("GET", Parser.findVerb("get book"));
        assertEquals("GET", Parser.findVerb("acquire bolt cutters"));
        assertEquals("GET", Parser.findVerb("pick up axe"));
        assertEquals("GET", Parser.findVerb("grab apple"));
        assertEquals("GET", Parser.findVerb("obtain keys"));
        assertEquals("GET", Parser.findVerb("take bolt cutters"));
    }

    @Test
    public void findVerbGo() {
        assertEquals("GO", Parser.findVerb("go north"));
        assertEquals("GO", Parser.findVerb("travel south"));
        assertEquals("GO", Parser.findVerb("walk east"));
        assertEquals("GO", Parser.findVerb("move west"));
        assertEquals("GO", Parser.findVerb("migrate abroad"));
        assertEquals("GO", Parser.findVerb("trek bolt cutters"));
        assertEquals("GO", Parser.findVerb("wander bolt cutters"));
    }

    @Test
    public void findVerbLook() {
        assertEquals("LOOK", Parser.findVerb("look north"));
        assertEquals("LOOK", Parser.findVerb("look at south"));
        assertEquals("LOOK", Parser.findVerb("examine east"));
        assertEquals("LOOK", Parser.findVerb("check out west"));
        assertEquals("LOOK", Parser.findVerb("study abroad"));
        assertEquals("LOOK", Parser.findVerb("inspect bolt cutters"));
        assertEquals("LOOK", Parser.findVerb("scan bolt cutters"));
        assertEquals("LOOK", Parser.findVerb("appraise abroad"));
    }

    @Test
    public void findVerbUse() {
        assertEquals("USE", Parser.findVerb("use axe"));
        assertEquals("USE", Parser.findVerb("manipulate bolt cutters"));
    }

    @Test
    public void findVerbSave() {
        assertEquals("SAVE", Parser.findVerb("save"));
    }

    @Test
    public void findVerbLoad() {
        assertEquals("LOAD", Parser.findVerb("load"));
    }

    @Test
    public void findVerbHelp() {
        assertEquals("HELP", Parser.findVerb("help"));
    }

    @Test
    public void findVerbWait() {
        assertEquals("WAIT", Parser.findVerb("wait"));
    }

    @Test
    public void findNounItems() {
        assertEquals("axe", Parser.findNoun("get axe", gameMap));
        assertEquals("book", Parser.findNoun("use book", gameMap));
        assertEquals("cake", Parser.findNoun("acquire cake", gameMap));
        assertEquals("bolt cutters", Parser.findNoun("look bolt cutters", gameMap));
    }

    @Test
    public void findNounDirection() {
        assertEquals("north", Parser.findNoun("go north", gameMap));
        assertEquals("south", Parser.findNoun("move south", gameMap));
        assertEquals("east", Parser.findNoun("walk east", gameMap));
        assertEquals("west", Parser.findNoun("travel west", gameMap));
    }

    @Test
    public void findNounStairs() {
        assertEquals("stairs", Parser.findNoun("use stairs", gameMap));
    }

    private Map<String, Result<?>> dataSetup() {
        Map<String, Result<?>> gameMap = new HashMap<>();
        List<String> itemsList = new ArrayList<>();
        itemsList.addAll(Arrays.asList("axe", "book", "cake", "bolt cutters"));
        itemsList.addAll(Arrays.asList("north", "south", "east", "west"));
        itemsList.addAll(Arrays.asList("stairs"));
        Result<?> items = new Result<>(itemsList);
        gameMap.put("listOfItems", items);
        return gameMap;
    }
}