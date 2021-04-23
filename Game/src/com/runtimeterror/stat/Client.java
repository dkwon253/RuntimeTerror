package com.runtimeterror.stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) throws IOException {

        Scanner userInput = new Scanner(System.in);
        HashMap<String, Rooms> rooms = LoadRooms.load();
        HashMap<String, Rooms> masterBedroomNeighbors = new HashMap<>();
        HashMap<String, Rooms> masterBathroomNeighbors = new HashMap<>();

        masterBedroomNeighbors.put("north", rooms.get("Study"));
        masterBedroomNeighbors.put("east", rooms.get("FloorTwoHall"));
        masterBedroomNeighbors.put("west", rooms.get("MasterBathroom"));
        masterBathroomNeighbors.put("east", rooms.get("MasterBedroom"));

        rooms.get("MasterBathroom").setRoomNeighbors(masterBathroomNeighbors);
        rooms.get("MasterBedroom").setRoomNeighbors(masterBedroomNeighbors);

        Player player = new Player(rooms.get("MasterBathroom"));
        System.out.println(player.getDescription());
        player.getCurrentStatus();
        player.addToInventory(player.getCurrRoom().getItem());


        String userChoice = "";
        while (userChoice != "quit") {
            System.out.println("\n>");
            userChoice = userInput.nextLine();
            player.changeRoom(userChoice);
            player.addToInventory(player.getCurrRoom().getItem());

        }
    }
}
