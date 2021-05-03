package com.runtimeterror.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;

public class LoadRoomData {

    public static HashMap<String, Rooms> load() throws IOException{//This method loads data from csv file and creates rooms
        HashMap<String, Rooms> roomList = new HashMap<>();

        Files.lines(Path.of("Game/RoomData/data.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String itemName = "null".equals(tokens[1]) ? null : tokens[1];
            String itemType = "null".equals(tokens[2]) ? null : tokens[2];
            String itemDescription = "null".equals(tokens[3]) ? null : tokens[3];
            String hidingSpot = "null".equals(tokens[4]) ? null : tokens[4];
            String description = tokens[9];
            String path = tokens[10];

            if(itemName == null){
                roomList.put(roomName, new Rooms(roomName, description, hidingSpot, null,path));

            }else{
                roomList.put(roomName, new Rooms(roomName, description, hidingSpot, new Item(itemName, itemType, itemDescription ),path));

            }

        });

        Files.lines(Path.of("Game/RoomData/data.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String east = "null".equals(tokens[5]) ? null : tokens[5];
            String west = "null".equals(tokens[6]) ? null : tokens[6];
            String north = "null".equals(tokens[7]) ? null : tokens[7];
            String south = "null".equals(tokens[8]) ? null : tokens[8];

            HashMap<String, Rooms> neigborbor = new HashMap<>();
            neigborbor.put("east",  roomList.get(east));
            neigborbor.put("west",  roomList.get(west));
            neigborbor.put("north", roomList.get(north));
            neigborbor.put("south", roomList.get(south));

            roomList.get(roomName).setRoomNeighbors(neigborbor);
        });

        return roomList;
    }
}
