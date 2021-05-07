package com.runtimeterror.model;

import java.util.Map;

import static com.runtimeterror.utils.Pipe.apply;

class PostGameProcessor {

    Map<String, Result<?>> start(Map<String, Result<?>> gameMap) {
        return
        apply(gameMap)
                .pipe(this::getMessageLabel)
//                .pipe(this::processResetRound)
                .result();
    }

    public Map<String, Result<?>> getMessageLabel(Map<String, Result<?>> gameMap) {
        String viewLabel = (String) gameMap.get("viewLabel").getResult();
        System.out.println(viewLabel);
        @SuppressWarnings("unchecked")
        Map<String, String> staticLabels = (Map<String, String>) gameMap.get("staticLabels").getResult();
        String labelText = staticLabels.get(viewLabel);
        gameMap.put("messageLabel", new Result<>(labelText));
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