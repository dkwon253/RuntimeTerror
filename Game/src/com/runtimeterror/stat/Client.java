package com.runtimeterror.stat;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        Rooms MasterBathroom = new Rooms("MasterBathroom", "dirty bathroom", new String[]{"MasterBedroom"}, null);
        Rooms MasterBedroom = new Rooms("MasterBedroom", "creepy bedroom", new String[]{"MasterBathroom", "Study", "Floor2Hall"}, "under the bed");

        Player player = new Player(MasterBathroom);
        System.out.println(player.getName());
        System.out.println(player.getDescription());

        player.getCurrRoom();
        System.out.println("You see a rusty axe on the floor next to a can of pepsi.");
        player.addToInventory("axe", "weapon", "old rusty axe");
        player.getInventory();
        System.out.println("You see a key.");
        player.addToInventory("key", "weapon", "old rusty axe");
        player.getInventory();
        player.setCurrRoom(MasterBedroom);
        player.getCurrRoom();



    }

}
