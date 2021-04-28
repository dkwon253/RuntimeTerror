package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.stat.*;
import com.runtimeterror.textparser.InputData;
import com.runtimeterror.textparser.Parser;
import static com.runtimeterror.textparser.Verbs.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameClient implements GameInterface, java.io.Serializable{
    HashMap<String, Rooms> rooms;
    Player player;
    Monster monster;
    String addendumText = "Test Addendum text";
    boolean gameLoaded = false;

    GameClient(){
        try {
            rooms = LoadRoomData.load();
        }
        catch (Exception e){
            System.out.printf("Failed to load the game files:");
            System.out.println(e.getMessage());
        }
        player = new Player(rooms.get("Master Bathroom"));
        monster = new Monster(rooms.get("Boiler"));
    }




    @Override
    public String getRoomText() {
        String result = player.getCurrRoom().getRoomDescriptionText(player);

        if (!"".equals(addendumText)) {
            result += "\n\n";
            result += addendumText;
            addendumText = "";
        }
        return result;
    }

    @Override
    public String getPLayerInventory() {
        List<Item> inv = player.getInventory();
        List<String> invString =  new ArrayList<>();
        for (Item item : inv){
            invString.add(item.getName());
        }
        String result = "Inventory: \n" + String.join(", ", invString);
        return result;
    }

    @Override
    public String submitPlayerString(String inputString) {
        String result = "";
        InputData parsedInput = Parser.parseInput(inputString);
        // check if the player requested a get command.
        if  (parsedInput == null){
            result = "I don't understand \"" +inputString +"\"";
        }
        else if (GET.equals(parsedInput.getVerbType())){
            result = processGet(parsedInput);
        }
        // check if the player requested a move command.
        else if (GO.equals(parsedInput.getVerbType())) {
            result = processMove(parsedInput);
        }
        // check if the player requested a use command.
        else if (USE.equals(parsedInput.getVerbType())) {
            result = processUse(parsedInput);
        }
        // check if the player requested a look command.
        else if (LOOK.equals(parsedInput.getVerbType())) {
            result = processLook(parsedInput);
        }
        // check if the player requested a hide command.
        else if (HIDE.equals(parsedInput.getVerbType())) {
            result = "HIDE commands not yet implemented";
        }
        else if (SAVE.equals(parsedInput.getVerbType())) {
            result = saveGame();
        }
        else if (LOAD.equals(parsedInput.getVerbType())) {
            result = loadGame();
        }
        return result;
    }

    private String processMove(InputData data){
        monster.changeRoom(rooms);
        return player.changeRoom(data.getNoun());

    }

    private String processGet(InputData data) {
        String result = "";
        if ("stairs".equals(data.getNoun()) && data.getVerb().equals("take")){
            result = processUseStairs();
        }
        else if(("lift".equals(data.getNoun()) || "elevator".equals(data.getNoun())) && data.getVerb().equals("take")){
            result = processUseElevator();
        }
        else {
            Item item = player.getCurrRoom().removeItemFromRoom(data.getNoun());
            if (item != null) {
                result = player.addToInventory(item);
            } else {
                result = "Cannot get " + data.getNoun() + ".";
            }
        }
        return result;
    }

    private String processUse(InputData data) {
        String result = "";
        if ("stairs".equals(data.getNoun()) && data.getVerb().equals("use")){
            result = processUseStairs();
        }
        else if(("lift".equals(data.getNoun()) || "elevator".equals(data.getNoun())) && data.getVerb().equals("use")){
            result = processUseElevator();
        }
        else {
            addendumText = UseInventoryItemProcessor.useItem(data,player,rooms);
        }
        return result;
    }

    private String  processLook(InputData data) {
        String result = "";
        boolean found = false;
        for (Item item : player.getInventory()){
            if (item.getName().equals(data.getNoun())){
                found = true;
                this.addendumText = item.getDescription();
                break;
            }
        }
        if (!found && player.getCurrRoom().getItem() != null){
            if (player.getCurrRoom().getItem().getName().equals(data.getNoun())){
                found = true;
                this.addendumText = player.getCurrRoom().getItem().getDescription();
            }
        }
        if (!found){
            result = "There is no " + data.getNoun() + ".";
        }
        return result;
    }

    private String processUseStairs(){
        String result = "";
        if (player.getCurrRoom().getRoomName().equals("Floor Two Hall")){
            player.setCurrRoom(rooms.get("Main Hall"));
        }
        else if (player.getCurrRoom().getRoomName().equals("Main Hall")){
            player.setCurrRoom(rooms.get("Floor Two Hall"));
        }
        else if (player.getCurrRoom().getRoomName().equals("Kitchen")){
            player.setCurrRoom(rooms.get("Basement"));
        }
        else if (player.getCurrRoom().getRoomName().equals("Basement")){
            player.setCurrRoom(rooms.get("Kitchen"));
        }
        else {
            result = "There are no stairs to use.";
        }
        return result;
    }

    private String processUseElevator(){
        String result = "";
        if (player.getCurrRoom().getRoomName().equals("Floor Two Hall")){
            player.setCurrRoom(rooms.get("Storage"));
        }
        else if (player.getCurrRoom().getRoomName().equals("Storage")){
            player.setCurrRoom(rooms.get("Floor Two Hall"));
        }
        else {
            result = "There is no elevator to use.";
        }
        return result;
    }




    private String saveGame(){
        HashMap<String, Object> gameObjects = new HashMap<String, Object>();
        gameObjects.put("rooms", rooms);
        gameObjects.put("player", player);
        gameObjects.put("monster", monster);
        try {
            FileOutputStream fos = new FileOutputStream("Game/gameData/savedGameData.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameObjects);
            oos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Failed to load the game files:");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "game saved";
    }

    private String loadGame(){
        String result = "";
        if(!gameLoaded){
            try {

                FileInputStream fis = new FileInputStream("Game/gameData/savedGameData.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);

                HashMap<String,Object> data = (HashMap<String,Object>)ois.readObject();
                fis.close();

                rooms = (HashMap<String, Rooms>) data.get("rooms");
                player = (Player) data.get("player");
                monster = (Monster) data.get("monster");
                gameLoaded = true;
                result = "game loaded from last checkpoint";

            } catch (FileNotFoundException | ClassNotFoundException e) {
                return "game could not be loaded";
            } catch (IOException e) {
                return "game could not be loaded";
            }catch (Exception e){
                return "game could not be loaded";
            }
        }else {
            result = "You have already loaded the game. You cannot do it again";
        }
        return result;
    }

}
