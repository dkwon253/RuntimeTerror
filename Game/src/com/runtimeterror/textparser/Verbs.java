package com.runtimeterror.textparser;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public enum Verbs {
    GET(Arrays.asList("get","acquire","pick up","grab","obtain","take")),
    GO(Arrays.asList("go","travel","walk","move","migrate","trek","wander")),
    USE(Arrays.asList("use","manipulate")),
    LOOK(Arrays.asList("look at", "look","examine","study","check out","inspect","scan","appraise")),
    HIDE(Arrays.asList("hide","take cover")),
    SAVE(Arrays.asList("save")),
    LOAD(Arrays.asList("load"));


    private List<String> verbList;

    Verbs(List<String> verbList){
        this.verbList = verbList;
    }

    public String startVerb(String s){
        for (String verb : verbList){
            if (s.startsWith(verb)){
                return verb;
            }
        }
        return null;
    }
}
