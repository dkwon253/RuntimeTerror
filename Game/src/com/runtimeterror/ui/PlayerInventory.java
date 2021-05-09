package com.runtimeterror.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class PlayerInventory extends JFrame{

    private final JLabel playerUsableInventoryLbl;

    PlayerInventory() {
        setSize(550, 350);
        setResizable(false);
        setTitle("Usable Inventory");
        setLocation(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Image usableInventory = null;
        try {
            usableInventory = ImageIO.read(new File("Game/Icons/axe.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerUsableInventoryLbl = new JLabel(new ImageIcon(usableInventory), SwingConstants.CENTER);
        playerUsableInventoryLbl.setBounds(100, 450, 500, 260);
        add(playerUsableInventoryLbl);
    }

    private JLabel getPlayerUsableInventoryLbl() {
        return playerUsableInventoryLbl;
    }
}