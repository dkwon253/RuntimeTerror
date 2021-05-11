package com.runtimeterror.model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, Result<?>> data = new HashMap<>();
    public Database() {
        try {
            data = LoadRoomData.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Result<?>> getDataAsHashMap() {
        return this.data;
    }
}