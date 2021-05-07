package com.runtimeterror.model;

public class Result<T> implements java.io.Serializable {
    private final T result;

    public Result(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        if(result != null) {
            return result.toString();
        } else {
            return "null";
        }
    }
}