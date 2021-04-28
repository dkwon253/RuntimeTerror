package com.runtimeterror.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Monster implements java.io.Serializable{
    //Fields

    private Rooms currRoom;

    //CONSTRUCTORS
    public Monster(Rooms currRoom) {
        this.currRoom = currRoom;
    }

    //BUSINESS METHODS
    public void changeRoom(Rooms rooms){
        this.setCurrRoom((rooms));
        System.out.println("Monster's current room " + currRoom.getRoomName());
    };

//    public Rooms getRandomRoom(HashMap<String, Rooms> rooms){
//        List<String> keysAsArray = new ArrayList<String>(rooms.keySet());
//        Random r = new Random();
//        Rooms randomRoom = rooms.get(keysAsArray.get(r.nextInt(keysAsArray.size())));
//        return randomRoom;
//    }

    //GETTERS & SETTERS
    public Rooms getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(Rooms currRoom) {
        this.currRoom = currRoom;
    }
}

