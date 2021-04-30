package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.model.*;
import com.runtimeterror.textparser.InputData;
import com.runtimeterror.textparser.Parser;
import static com.runtimeterror.textparser.Verbs.*;

import java.io.*;
import java.util.*;

public class GameClient implements GameInterface, java.io.Serializable{
    HashMap<String, Rooms> rooms;
    Player player;
    Monster monster;
    String addendumText = "";
    boolean gameLoaded = false;

    GameClient(){
        gameLoaded = false;
        addendumText = "";
        try {
            rooms = LoadRoomData.load();
        }
        catch (Exception e){
            System.out.printf("Failed to load the game files:");
            System.out.println(e.getMessage());
        }
        player = new Player(rooms.get("Master Bathroom"));
        monster = new Monster(rooms.get("Bedroom Two"));
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
    public int getMonsterLocation() {
        if (checkAdjacentRoom()) {
            return 1;
        }
        else if (player.getCurrRoom().equals(monster.getCurrRoom())){
            return 0;
        }
        return -1;
    }

    @Override
    public void reset() {
        gameLoaded = false;
        addendumText = "";
        try {
            rooms = LoadRoomData.load();
        }
        catch (Exception e){
            System.out.printf("Failed to load the game files:");
            System.out.println(e.getMessage());
        }
        player = new Player(rooms.get("Master Bathroom"));
        monster = new Monster(rooms.get("Bedroom Two"));
    }

    @Override
    public boolean getPLayerStatus() {
        return player.isHidden();
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
            result = processHide();
        }
        else if (SAVE.equals(parsedInput.getVerbType())) {
            result = saveGame();
        }
        else if (LOAD.equals(parsedInput.getVerbType())) {
            result = loadGame();
        }
        else if (WAIT.equals(parsedInput.getVerbType())) {
            result = skipPlayerTurn();
        }
        if(player.getCurrRoom().equals(monster.getCurrRoom())){
            result = monsterEncounter();
        }
        return result;
    }

    private String processMove(InputData data){
        String result = player.changeRoom(data.getNoun());
        if (result == "You cant go this way"){
            return result;
        }
        monster.moveMonsterToRandomNeighbor();
        checkAdjacentRoom();
        monsterEncounter();
        player.unHide();
        return result;
    }

    private String processGet(InputData data) {
        String result = "";
        player.unHide();
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
                monster.moveMonsterToRandomNeighbor();
                checkAdjacentRoom();
                monsterEncounter();
            } else {
                result = "Cannot get " + data.getNoun() + ".";
            }
        }
        return result;
    }

    private String processUse(InputData data) {
        String result = "";
        player.unHide();
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

    private String processHide(){
        String result = "";
        String hidingSpot = player.getCurrRoom().getHidingLocation();
        if (hidingSpot != null && !player.isHidden()){
            result = "Using the " + hidingSpot + ", you attempt to hide.";
            player.hide();
            monster.moveMonsterToRandomNeighbor();
            checkAdjacentRoom();
            monsterEncounter();
        }
        else {
            result = "There is no where to hide.";
        }
        return result;
    }

    private String processUseStairs(){
        String result = "";
        if (player.getCurrRoom().getRoomName().equals("Floor Two Hall")){
            player.setCurrRoom(rooms.get("Main Hall"));
            monster.changeRoom(rooms.get("Gazebo"));
        }
        else if (player.getCurrRoom().getRoomName().equals("Main Hall")){
            player.setCurrRoom(rooms.get("Floor Two Hall"));
            monster.changeRoom(rooms.get("Master Bathroom"));

        }
        else if (player.getCurrRoom().getRoomName().equals("Kitchen")){
            player.setCurrRoom(rooms.get("Basement"));
            monster.changeRoom(rooms.get("Electrical"));

        }
        else if (player.getCurrRoom().getRoomName().equals("Basement")){
            player.setCurrRoom(rooms.get("Kitchen"));
            monster.changeRoom(rooms.get("Gazebo"));

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
            monster.changeRoom(rooms.get("Boiler"));

        }
        else if (player.getCurrRoom().getRoomName().equals("Storage")){
            player.setCurrRoom(rooms.get("Floor Two Hall"));
            monster.changeRoom(rooms.get("Gazebo"));

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

    public String monsterEncounter(){
        if(monster.getCurrRoom().getRoomName() == player.getCurrRoom().getRoomName()){
            if (player.isHidden()){
                System.out.println("Monster is here but you are hidden and safe.");
                return "Monster is here but you are hidden and safe.";
            }
            if(!player.isHidden()){
                System.out.println("Monster caught you. You are now dead. Game Over...");
                return "Monster caught you. You are now dead. Game Over...";
                //System.exit(0);
            }
        }
        return "";
    }

    private boolean checkAdjacentRoom(){
        String[] directions = {"north","east","south","west"};
        for (String direction : directions) {
            if (player.getCurrRoom().getRoomNeighbors().get(direction) != null){
                if (monster.getCurrRoom() == player.getCurrRoom().getRoomNeighbors().get(direction)){
                    //System.out.println("Monster is nearby.");
                    return true;
                }
            }
        }
        return false;
    }

    private String skipPlayerTurn(){
     monster.moveMonsterToRandomNeighbor();
     return "Monster has moved but still lurking around other rooms.";
    }


}
