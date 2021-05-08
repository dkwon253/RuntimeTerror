package com.runtimeterror.model;

public class Item implements java.io.Serializable{

    // FIELDS
    private  String name;
    private  String type;
    private  String description;

    //CONSTRUCTOR
    public Item(){}
    public Item(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    //GETTER
    public String getName() {
        return name;
    }

//    public String getType() {
//        return type;
//    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}