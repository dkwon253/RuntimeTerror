package com.runtimeterror.textparser;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;


public class Parser {

    //private static final Verbs[] verbsList = {HIDE,GET,GO,USE,LOOK,LOAD,SAVE,WAIT,HELP};

    public static InputData parseInput(String input) {
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}","");
        String[] inputList = StringUtils.split(temp, " ", 2);

        try(InputStream inputStream = Parser.class.getResourceAsStream("verb.json")){
            Object obj = new JSONParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            JSONObject verbs = (JSONObject) obj;
            Iterator<String> keys = verbs.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                //System.out.println(verbs.get(key).getClass());
                if (verbs.get(key).toString().contains(inputList[0])){
                    return new InputData(key, inputList[1]);
                }
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        };
        return null;
    }
//
//    public static void main(String[] args) {
//        System.out.println(parseInput("travel east").getVerb());
//    }

}
