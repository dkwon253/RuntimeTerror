package com.runtimeterror.model;
import com.runtimeterror.dao.LeaderboardDetailsRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private Map<String, Result<?>> data = new HashMap<>();
    private final LeaderboardDetailsRepository dynamo = new LeaderboardDetailsRepository();
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

    boolean addLeaderboard(Leaderboard leaderboard) {
        return dynamo.addLeaderboard(leaderboard);
    }

    List<Leaderboard> getTopLeaderboard(int size) {
        return dynamo.getTopLeaderboard(size);
    }
}