package com.runtimeterror.textparser;

import java.util.Objects;

public class InputData implements java.io.Serializable {
    private String verb;
    private String noun;

    public InputData(String verb,String noun){
        this.verb = verb;
        this.noun = noun;
    }

    public String getVerb() {
        return verb;
    }

    public String getNoun() {
        return noun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputData inputData = (InputData) o;
        return Objects.equals(getVerb(), inputData.getVerb()) && Objects.equals(getNoun(), inputData.getNoun());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVerb(), getNoun());
    }
}