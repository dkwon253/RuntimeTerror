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


    public void moveMonsterToRandomNeighbor(){
        String[] directions = {"north","east","south","west"};
        List<String> movableDirectionsList = new ArrayList<>();

        for (String direction : directions) {
            if (this.getCurrRoom().getRoomNeighbors().get(direction) != null){
                movableDirectionsList.add(direction);
            }
        }
        Random r = new Random();
        int randomitem = r.nextInt(movableDirectionsList.size());
        this.changeRoom(this.getCurrRoom().getRoomNeighbors().get(movableDirectionsList.get(randomitem)));
    }


    //GETTERS & SETTERS
    public Rooms getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(Rooms currRoom) {
        this.currRoom = currRoom;
    }
}

