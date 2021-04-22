package com.runtimeterror.uitest;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.controller.SwingController;
import com.runtimeterror.ui.SwingUI;

public class TestGameInterface {
    public static void main(String[] args) {
        GameInterface game = new FauxGame();
        SwingController controller = new SwingController(game);
        SwingUI ui = new SwingUI("TestUI",controller);
        ui.setVisible(true);
    }
}
