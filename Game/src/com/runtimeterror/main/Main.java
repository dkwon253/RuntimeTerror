package com.runtimeterror.main;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.GameClientNew;
import com.runtimeterror.ui.SwingUI;

public class Main {
    public static void main(String[] args) {
        GameInterface gi = new GameClientNew();
        SwingController controller = new SwingController(gi);
        SwingUI ui = new SwingUI("Runtime Terror", controller);
        ui.setVisible(true);
    }
}
