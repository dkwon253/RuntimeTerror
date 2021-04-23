package com.runtimeterror.stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) throws IOException {

        Scanner userInput = new Scanner(System.in);
        HashMap<String, Rooms> rooms = LoadRoomData.load();

        Player player = new Player(rooms.get("MasterBathroom"));
        System.out.println(player.getDescription());
        player.getCurrentStatus();
        player.addToInventory(player.getCurrRoom().getItem());

        String userChoice = "";
        while (userChoice != "quit") {
            System.out.println("\n>");
            userChoice = userInput.nextLine();
            String[] choices  = userChoice.split(" ");

            player.changeRoom(choices[1]);

        }


    }
}
