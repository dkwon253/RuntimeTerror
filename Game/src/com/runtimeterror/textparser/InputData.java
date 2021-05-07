package com.runtimeterror.textparser;

public class InputData implements java.io.Serializable {
    //private Verbs verbType;
    private String verb;
    private String noun;

    public InputData(String verb,String noun){
        //this.verbType = verbType;
        this.verb = verb;
        this.noun = noun;
    }

//    public Verbs getVerbType() {
//        return verbType;
//    }

    public String getVerb() {
        return verb;
    }

    public String getNoun() {
        return noun;
    }
}