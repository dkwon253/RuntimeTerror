package com.runtimeterror.textparser;
import static com.runtimeterror.textparser.Verbs.*;

public class Parser {

    private static final Verbs[] verbsList = {GET,GO,USE,LOOK,HIDE};

    public static InputData parseInput(String input){
        String temp = input.trim();
        temp = temp.replaceAll("\\p{Punct}","");
        for (Verbs verb : verbsList){
            String verbString = verb.startVerb(temp);
            if (verbString != null){
                return new InputData(verb,verbString,temp.replaceAll(verbString,"").trim());
            }
        }
        return null;
    }
}
