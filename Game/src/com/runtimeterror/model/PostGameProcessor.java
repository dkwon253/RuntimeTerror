package com.runtimeterror.model;

import java.util.Map;


class PostGameProcessor {

    Map<String, Result<?>> start(Map<String, Result<?>> gameMap) {
        getMessageLabel(gameMap);
//                .pipe(this::processResetRound)
        return gameMap;
    }

    public Map<String, Result<?>> getMessageLabel(Map<String, Result<?>> gameMap) {
        String viewLabel = (String) gameMap.get("viewLabel").getResult();
        gameMap.put("messageLabel", new Result<>(viewLabel));
        return gameMap;
    }

    private Map<String, Result<?>> processResetRound(Map<String, Result<?>> gameMap) {
        boolean gameLoaded = (boolean) gameMap.get("gameLoaded").getResult();
        if(gameLoaded) {
            System.out.println(gameMap);
            return gameMap;
        } else {
            LoadRoomData.setGameMapRoundDefaults(gameMap);
        }
        return gameMap;
    }
}