package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.GameClient;
import com.runtimeterror.ui.SwingUI;

public class Main {
    public static void main(String[] args) {
        GameInterface gi = new GameClient();
        SwingController controller = new SwingController(gi);
        SwingUI ui = new SwingUI("Runtime Terror", controller);
        ui.setVisible(true);
    }
}
