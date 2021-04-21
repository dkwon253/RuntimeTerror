package com.runtimeterror.stat;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        Rooms MasterBathroom = new Rooms("MasterBathroom", "dirty bathroom", new String[]{"MasterBedroom"}, null, new Item("key", "tool", "you see a shiny key" ));
        Rooms MasterBedroom = new Rooms("MasterBedroom", "creepy bedroom", new String[]{"MasterBathroom", "Study", "Floor2Hall"}, "under the bed", new Item("axe", "weapon", "you see a rusty axe on the floor next to a can of pepsi" ));

        Player player = new Player(MasterBathroom);
        System.out.println(player.getDescription());
        player.getCurrentStatus();
        player.addToInventory(player.getCurrRoom().getItem());

    }

}
