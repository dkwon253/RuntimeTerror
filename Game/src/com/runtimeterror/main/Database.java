package com.runtimeterror.main;

import com.runtimeterror.model.LoadRoomData;
import com.runtimeterror.model.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Database {
    private Map<String, Result<?>> data = new HashMap<>();
    Database() {
        try {
            data = LoadRoomData.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Map<String, Result<?>> getDataAsHashMap() {
        return this.data;
    }
}