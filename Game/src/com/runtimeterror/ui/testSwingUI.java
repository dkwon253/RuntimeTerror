package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;

public class testSwingUI {
    public static void main(String[] args) {
        SwingController controller = new SwingController();
        SwingUI ui = new SwingUI("TestUI",controller);
        ui.setVisible(true);
    }
}