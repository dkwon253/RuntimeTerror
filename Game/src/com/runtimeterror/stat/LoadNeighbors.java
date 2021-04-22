package com.runtimeterror.stat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;

public class LoadNeighbors {
 //WORK IN PROGRESS
    public static HashMap<String, Rooms> load() throws IOException{//This method loads data from csv file and creates rooms
        HashMap<String, Rooms> roomList = new HashMap<>();

        Files.lines(Path.of("Game/RoomData/roomNeighbors.csv")).forEach(line -> {
            String[] tokens = line.split(",");
            String roomName = tokens[0];
            String itemName = "null".equals(tokens[1]) ? null : tokens[1];
            String itemType = "null".equals(tokens[2]) ? null : tokens[2];
            String itemDescription = "null".equals(tokens[3]) ? null : tokens[3];
            String hidingSpot = "null".equals(tokens[4]) ? null : tokens[4];
            String description = tokens[5];

            roomList.put(roomName, new Rooms(roomName, description, hidingSpot, new Item(itemName, itemType, itemDescription )));

        });

        return roomList;
    }

}
