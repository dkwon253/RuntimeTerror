package com.runtimeterror.uitest;

import com.runtimeterror.controller.GameInterface;

import java.util.ArrayList;
import java.util.List;

public class FauxGame implements GameInterface {
    private String roomDesc;
    List<String> inv = new ArrayList<>();

    public FauxGame() {
        roomDesc = "Location:\nMaster Bathroom\n\nDescription:\nYou wake up in a strange bathroom.  You have no " +
                "idea whose house you are in or how you got here.  You hear water dripping in the shower in the corner " +
                "of the room.  There is a door to the east.";
    }

    @Override
    public boolean getPLayerStatus() {
        return false;
    }

    @Override
    public int getMonsterLocation() {
        return 0;
    }

    @Override
    public String getRoomText() {
        return roomDesc;
    }

    @Override
    public String getPLayerInventory() {
        String result = "Inventory:\n";
        result += String.join(", ",inv);
        return result;
    }

    @Override
    public String submitPlayerString(String inputString) {
        String result = "";
        String[] splitString = inputString.split(" ");
        if("go".equals(splitString[0])){
            if ("north".equals(splitString[1])){
                roomDesc = "Location:\nNorthern Bathroom\n\nDescription:\n";
                roomDesc += "Shoreditch drinking vinegar taxidermy flannel, migas echo park neutra salvia authentic locavore live-edge actually meh. Pickled biodiesel heirloom next level flannel normcore. Letterpress actually vinyl direct trade. Ethical slow-carb sriracha single-origin coffee hoodie cray. Waistcoat bespoke kale chips, plaid craft beer four dollar toast taxidermy chambray keytar.";
            }
            else if ("east".equals(splitString[1])) {
                roomDesc = "Location:\nEastern Bathroom\n\nDescription:\n";
                roomDesc += "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit\u200B\u200B, morbo vel maleficia? De apocalypsi gorger omero undead survivor dictum mauris. Hi mindless mortuis soulless creaturas, imo evil stalking monstra adventus resi dentevil vultus comedat cerebella viventium.";
            }
            else if ("south".equals(splitString[1])) {
                roomDesc = "Location:\nSouthern Bathroom\n\nDescription:\n";
                roomDesc += "Bacon ipsum dolor amet short ribs brisket venison rump drumstick pig sausage prosciutto chicken spare ribs salami picanha doner. Kevin capicola sausage, buffalo bresaola venison turkey shoulder picanha ham pork tri-tip meatball meatloaf ribeye. Doner spare ribs andouille bacon sausage. Ground round jerky brisket pastrami shank.";
            }
            else if ("west".equals(splitString[1])) {
                roomDesc = "Location:\nWestern Bathroom\n\nDescription:\n";
                roomDesc += "Space, the final frontier. These are the voyages of the Starship Enterprise. Its five-year mission: to explore strange new worlds, to seek out new life and new civilizations, to boldly go where no man has gone before. Many say exploration is part of our destiny, but it’s actually our duty to future generations and their quest to ensure the survival of the human species.";
            }
            else {
                result = "You can't go that way.";
            }
        }
        else if("get".equals(splitString[0])) {
            if(splitString[1] != null || !splitString[1].isEmpty()){
                inv.add(splitString[1]);
                result = "You got the " + splitString[1];
            }
            else {
                result = "You can't get that.";
            }
        }
        else if("drop".equals(splitString[0])) {
            if(splitString[1] != null || !splitString[1].isEmpty()){
                if (inv.contains(splitString[1])){
                    inv.remove(splitString[1]);
                    result = "You dropped the " + splitString[1];
                }
                else{
                    result = "You can't do that.";
                }
            }
            else {
                result = "You can't get that.";
            }
        }
        else{
            result = "I don't understand " + splitString[0];
        }
        return result;
    }
    public String getRoomDesc() {
        return roomDesc;
    }

}
