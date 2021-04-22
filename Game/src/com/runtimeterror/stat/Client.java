package com.runtimeterror.stat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        Rooms MasterBathroom = new Rooms("MasterBathroom", "dirty bathroom", null, new Item("key", "tool", "you see a shiny key"));
        Rooms MasterBedroom = new Rooms("MasterBedroom", "creepy bedroom", "under the bed", new Item("axe", "weapon", "you see a rusty axe on the floor next to a can of pepsi"));
        Rooms Study = new Rooms("Study", "StudyRoom", null, new Item("key", "tool", "you see a shiny key"));
        Rooms Floor2Hall = new Rooms("Floor2Hall", "dirty bathroom", null, new Item("key", "tool", "you see a shiny key"));


        HashMap<String, Rooms> masterBedroomNeighbors = new HashMap<>();
        HashMap<String, Rooms> masterBathroomNeighbors = new HashMap<>();

        masterBedroomNeighbors.put("north", Study);
        masterBedroomNeighbors.put("east", Floor2Hall);
        masterBedroomNeighbors.put("west", MasterBathroom);
        masterBathroomNeighbors.put("east", MasterBedroom);

        MasterBathroom.setRoomNeighbors(masterBathroomNeighbors);
        MasterBedroom.setRoomNeighbors(masterBedroomNeighbors);

        Player player = new Player(MasterBathroom);
        System.out.println(player.getDescription());
        player.getCurrentStatus();
        player.addToInventory(player.getCurrRoom().getItem());


        String userChoice = "";
        while (userChoice != "quit") {
            System.out.println("\n>");
            userChoice = userInput.nextLine();
            player.changeRoom(userChoice);

        }
    }
}
