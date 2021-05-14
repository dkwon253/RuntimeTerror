package com.runtimeterror.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayerMap extends JFrame {

    private final JLabel mapLocationLbl;

    public PlayerMap() {
        // setting the JFrame up
        setSize(560, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Player Map");
        setLocation(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mapLocationLbl = new JLabel(getResizedMap("Game/maps/masterbathroom.jpg"));
        mapLocationLbl.setBounds(0, 450, 500, 260);
        add(mapLocationLbl);
    }

    private ImageIcon getResizedMap(String imagePath) {
        Image img = null;
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(560, 350, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // getter to post new map based on player movement
    public JLabel getMapLocationLbl() {
        return mapLocationLbl;
    }
}