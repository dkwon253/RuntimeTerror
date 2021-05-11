package com.runtimeterror.model;

public class Item implements java.io.Serializable {

    // FIELDS
    private String name;
    private String type;
    private String description;
    private String itemImagePath;

    //CONSTRUCTOR
    public Item() {
    }

    public Item(String name, String type, String description, String imagePath) {
        this.name = name;
        this.type = type;
        this.description = description;
        setItemImagePath(imagePath);
    }

    //GETTER
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setItemImagePath(String path) {
        itemImagePath = path;
    }

    public String getItemImagePath() {
        return itemImagePath;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + itemImagePath + '\'' +
                '}';
    }
}