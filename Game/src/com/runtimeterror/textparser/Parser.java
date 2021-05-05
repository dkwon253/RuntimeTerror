package com.runtimeterror.textparser;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Parser {

    //private static final Verbs[] verbsList = {HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP};

    public static InputData parseInput(String input) {
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}","");
        int i = temp.lastIndexOf(" ");
        List<String> inputList = new ArrayList<>();
        if (i == -1){
            inputList.add(temp);
        }else{
            inputList.add(temp.substring(0, i));
            inputList.add(temp.substring(i+1));
        }
        //pick up bolt cutters

        try(InputStream inputStream = Parser.class.getResourceAsStream("verb.json")){
            Object obj = new JSONParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            JSONObject verbs = (JSONObject) obj;
            Iterator<String> keys = verbs.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                if (verbs.get(key).toString().contains(inputList.get(0))){
                    return new InputData(key, temp.replaceAll(inputList.get(0),"").trim());
                }
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        };
        return null;
    }
//
    public static void main(String[] args) {
        parseInput("pick; up axe.!");
    }

}
