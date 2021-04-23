package com.runtimeterror.textparser;

public class InputData {
    private Verbs verbType;
    private String verb;
    private String noun;

    public InputData(Verbs verbType,String verb,String noun){
        this.verbType = verbType;
        this.verb = verb;
        this.noun = noun;
    }

    public Verbs getVerbType() {
        return verbType;
    }

    public String getVerb() {
        return verb;
    }

    public String getNoun() {
        return noun;
    }
}
