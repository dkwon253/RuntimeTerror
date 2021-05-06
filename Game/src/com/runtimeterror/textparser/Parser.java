package com.runtimeterror.textparser;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
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
//        System.out.println(findVerb(input));
//        System.out.println(findNoun(input));

        return new InputData(findVerb(input), findNoun(input));
    }

    // pick up bolt cutters

    private static String findVerb(String input){
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}","");
        int i = temp.indexOf(" ");
        List<String> verbList = new ArrayList<>();
        if (i == -1){
            verbList.add(temp);
        }else{
            verbList.add(temp.substring(0, i));
            int j = temp.indexOf(" ", temp.indexOf(" ")+1);
            if (j != -1){
                verbList.add(temp.substring(0, j));
            }
        }
        try(InputStream inputStream = Parser.class.getResourceAsStream("verb.json")){
            Object obj = new JSONParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            JSONObject verbs = (JSONObject) obj;
            for (int index = 0; index<verbList.size(); index++){
                Iterator<String> keys = verbs.keySet().iterator();
                while(keys.hasNext()) {
                    String key = keys.next();
                    List verbsList = convertToArrayList((JSONArray)verbs.get(key));
                    if (verbsList.contains(verbList.get(index))){
                        return key;
                    }
                }
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        };
        return null;
    }

    private static String findNoun(String input){
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}","");
        int i = temp.lastIndexOf(" ");
        System.out.println();
        List<String> nounList = new ArrayList<>();
        if (i == -1){
            nounList.add(temp);
        }else{
            nounList.add(temp.substring(i+1));
            int j = temp.lastIndexOf(" ", temp.lastIndexOf(" ")-1);
            if (j != -1){
                nounList.add(temp.substring(j+1));
            }
        }
        try(InputStream inputStream = Parser.class.getResourceAsStream("items.json")){
            Object obj = new JSONParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            JSONObject items = (JSONObject) obj;
            for (int index = 0; index<nounList.size(); index++){
                Iterator<String> keys = items.keySet().iterator();
                while(keys.hasNext()) {
                    String key = keys.next();
                    List itemsList = convertToArrayList((JSONArray)items.get(key));
                    if (itemsList.contains(nounList.get(index))){
                        return nounList.get(index);
                    }
                }
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        };
        return null;
    }

    private static List<String> convertToArrayList(JSONArray array){
        List<String> data = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            data.add((String) array.get(i));
        }
        return data;
    }

//    public static void main(String[] args) {
//        parseInput("pick up bolt cutters");
//    }

}
