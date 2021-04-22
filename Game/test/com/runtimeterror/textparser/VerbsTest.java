package com.runtimeterror.textparser;

import org.junit.Test;

import static com.runtimeterror.textparser.Verbs.*;

import static org.junit.Assert.*;

public class VerbsTest {
    @Test
    public void TestGET_get() {
        assertEquals("get",GET.startVerb("get hammer"));
    }

    @Test
    public void TestGET_acquire() {
        assertEquals("acquire",GET.startVerb("acquire hammer"));
    }

    @Test
    public void TestGET_pickup() {
        assertEquals("pick up",GET.startVerb("pick up hammer"));
    }

    @Test
    public void TestGET_invalids() {
        assertEquals(null,GET.startVerb("throw hammer"));
        assertEquals(null,GET.startVerb("eat hammer"));
        assertEquals(null,GET.startVerb("kick hammer"));
        assertEquals(null,GET.startVerb("merge hammer"));
    }

    @Test
    public void TestGO_valid() {
        assertEquals("go",GO.startVerb("go east"));
        assertEquals("travel",GO.startVerb("travel east"));
        assertEquals("walk",GO.startVerb("walk east"));
        assertEquals("migrate",GO.startVerb("migrate east"));
    }

    @Test
    public void TestGo_invalid() {
        assertEquals(null,GO.startVerb("throw hammer"));
        assertEquals(null,GO.startVerb("eat hammer"));
        assertEquals(null,GO.startVerb("kick hammer"));
        assertEquals(null,GO.startVerb("merge hammer"));
    }

    @Test
    public void TestUSE_valid() {
        assertEquals("use",USE.startVerb("use hammer"));
        assertEquals("manipulate",USE.startVerb("manipulate hammer"));

    }

    @Test
    public void TestUSE_invalid() {
        assertEquals(null,USE.startVerb("throw hammer"));
        assertEquals(null,USE.startVerb("eat hammer"));
        assertEquals(null,USE.startVerb("kick hammer"));
        assertEquals(null,USE.startVerb("merge hammer"));
    }

    @Test
    public void TestLOOK_valid() {
        assertEquals("look",LOOK.startVerb("look hammer"));
        assertEquals("look at",LOOK.startVerb("look at hammer"));
        assertEquals("examine",LOOK.startVerb("examine hammer"));
        assertEquals("study",LOOK.startVerb("study hammer"));
    }

    @Test
    public void TestLOOK_invalid() {
        assertEquals(null,LOOK.startVerb("throw hammer"));
        assertEquals(null,LOOK.startVerb("eat hammer"));
        assertEquals(null,LOOK.startVerb("kick hammer"));
        assertEquals(null,LOOK.startVerb("merge hammer"));
    }

    @Test
    public void TestHide_valid() {
        assertEquals("hide",HIDE.startVerb("hide"));
        assertEquals("take cover",HIDE.startVerb("take cover"));

    }

    @Test
    public void TestHIDE_invalid() {
        assertEquals(null,HIDE.startVerb("jump"));
        assertEquals(null,HIDE.startVerb("cry"));
        assertEquals(null,HIDE.startVerb("cower"));
    }
}