package com.runtimeterror.stat;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Player player = new Player();
        System.out.println(player.getName());
        System.out.println(player.getDescription());
        System.out.println("You see a rusty axe on the floor next to a can of pepsi.");
        player.addToInventory("axe", "weapon", "old rusty axe");
        player.getInventory();



        System.out.println("You see a key.");
        player.addToInventory("key", "weapon", "old rusty axe");
    }

}
