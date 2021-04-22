package com.runtimeterror.textparser;
import static com.runtimeterror.textparser.Verbs.*;


import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void parse_get_key() {
        InputData data = Parser.parseInput("get key");
        assertEquals(GET,data.getVerbType());
        assertEquals("get",data.getVerb());
        assertEquals("key",data.getNoun());
    }

    @Test
    public void parse_pick_up_key() {
        InputData data = Parser.parseInput("pick up key");
        assertEquals(GET,data.getVerbType());
        assertEquals("pick up",data.getVerb());
        assertEquals("key",data.getNoun());
    }

    @Test
    public void parse_go_west() {
        InputData data = Parser.parseInput("go west");
        assertEquals(GO,data.getVerbType());
        assertEquals("go",data.getVerb());
        assertEquals("west",data.getNoun());
    }

    @Test
    public void parse_look_at_car() {
        InputData data = Parser.parseInput("look at car");
        assertEquals(LOOK,data.getVerbType());
        assertEquals("look at",data.getVerb());
        assertEquals("car",data.getNoun());
    }

    @Test
    public void parse_hide() {
        InputData data = Parser.parseInput("hide");
        assertEquals(HIDE,data.getVerbType());
        assertEquals("hide",data.getVerb());
        assertEquals("",data.getNoun());
    }
    @Test
    public void parse_hide_with_punctuation() {
        InputData data = Parser.parseInput("hide.");
        assertEquals(HIDE,data.getVerbType());
        assertEquals("hide",data.getVerb());
        assertEquals("",data.getNoun());
    }

    @Test
    public void parse_check_out_big_chair_with_punctuation() {
        InputData data = Parser.parseInput("check out big chair!");
        assertEquals(LOOK,data.getVerbType());
        assertEquals("check out",data.getVerb());
        assertEquals("big chair",data.getNoun());
    }
}