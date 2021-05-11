package com.runtimeterror.textparser;

import com.runtimeterror.model.Result;
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
import java.util.Map;


public class Parser {

    public static Map<String, Result<?>> parseInput(Map<String, Result<?>> gameMap) {
        String input = (String) gameMap.get("input").getResult();
        InputData inputData = new InputData(findVerb(input), findNoun(input, gameMap));
        gameMap.put("inputData", new Result<>(inputData));
        return gameMap;
    }

    // pick up bolt cutters
    public static String findVerb(String input) {
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}", "");
        int i = temp.indexOf(" ");
        List<String> verbList = new ArrayList<>();
        verbList.add(temp);
        if (i == -1) {
            verbList.add(temp);
        } else {
            verbList.add(temp.substring(0, i));
            int j = temp.indexOf(" ", temp.indexOf(" ") + 1);
            if (j != -1) {
                verbList.add(temp.substring(0, j));
            }
        }
        try (InputStream inputStream = Parser.class.getResourceAsStream("verb.json")) {
            Object obj = new JSONParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            JSONObject verbs = (JSONObject) obj;
            for (int index = 0; index < verbList.size(); index++) {
                Iterator<String> keys = verbs.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    List verbsList = convertToArrayList((JSONArray) verbs.get(key));
                    if (verbsList.contains(verbList.get(index))) {
                        return key;
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        ;
        return null;
    }

    public static String findNoun(String input, Map<String, Result<?>> gameMap) {
        @SuppressWarnings("unchecked")
        List<String> listOfItems = (List<String>) gameMap.get("listOfItems").getResult();
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}", "");
        int i = temp.lastIndexOf(" ");
        System.out.println();
        List<String> nounList = new ArrayList<>();
        if (i == -1) {
            nounList.add(temp);
        } else {
            nounList.add(temp.substring(i + 1));
            int j = temp.lastIndexOf(" ", temp.lastIndexOf(" ") - 1);
            if (j != -1) {
                nounList.add(temp.substring(j + 1));
            }
        }
        for (int index = 0; index < nounList.size(); index++) {
            if (listOfItems.contains(nounList.get(index))) {
                return nounList.get(index);
            }

        }
        return null;
    }

    private static List<String> convertToArrayList(JSONArray array) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            data.add((String) array.get(i));
        }
        return data;
    }

}