package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.LoadRoomData;
import com.runtimeterror.model.Result;
import com.runtimeterror.ui.SwingUI;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        GameInterface gi = new GameClientNew();
        SwingController controller = new SwingController(gi);
        SwingUI ui = new SwingUI("Runtime Terror", controller);
        ui.setVisible(true);
    }
}
