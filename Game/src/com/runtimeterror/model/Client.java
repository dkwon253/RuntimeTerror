package com.runtimeterror.model;

import java.io.IOException;
import java.util.*;

public class Client {



    public static void main(String[] args) throws IOException {
        HashMap<String, Rooms> rooms = LoadRoomData.load();

        List<String> keysAsArray = new ArrayList<String>(rooms.keySet());
        Random r = new Random();
        Rooms randomRoom = rooms.get(keysAsArray.get(r.nextInt(keysAsArray.size())));

        System.out.println(randomRoom.getRoomName());

    }
}
