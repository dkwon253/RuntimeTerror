package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

class SwingUI extends JFrame{
    private final int FRAME_X_SIZE = 480;
    private final int FRAME_Y_SIZE = 640;

    private SwingController controller;
    private JTextArea roomInfoTA;
    private JTextArea inventoryInfoTA;
    private JTextField playerInputTF;
    private JLabel playerStateLbl;
    private JLabel playerMessageLbl;
    private JButton submitCommandBtn;


    // CTOR
    SwingUI(String title, SwingController controller){
        super(title);
        this.controller = controller;

        setLocation(100,100);
        setSize(FRAME_X_SIZE,FRAME_Y_SIZE);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        roomInfoTA = new JTextArea(25,40);
        roomInfoTA.setBounds(25,50,430,300);
        roomInfoTA.setEditable(false);
        roomInfoTA.setLineWrap(true);
        roomInfoTA.setWrapStyleWord(true);
        roomInfoTA.setText("Location:\nMaster Bathroom\n\nDescription:\nYou wake up in a strange bathroom.  You have no " +
                "idea whose house you are in or how you got here.  You hear water dripping in the shower in the corner " +
                "of the room.  There is a door to the east.");
        add(roomInfoTA);

        playerMessageLbl = new JLabel("Message", SwingConstants.CENTER);
        playerMessageLbl.setBounds(25,360,430,25);
        add(playerMessageLbl);

        inventoryInfoTA = new JTextArea(5,40);
        inventoryInfoTA.setBounds(25,400,430,75);
        inventoryInfoTA.setEditable(false);
        inventoryInfoTA.setText("Inventory:\nKey\tHammer\tCoin\nCrowbar\tTacocat");
        add(inventoryInfoTA);

        playerStateLbl = new JLabel("Status: Visible", SwingConstants.LEFT);
        playerStateLbl.setBounds(25,475,430,25);
        add(playerStateLbl);

        playerInputTF = new JTextField();
        playerInputTF.setBounds(25,500,430,25);
        add(playerInputTF);

        submitCommandBtn = new JButton();
        submitCommandBtn.setBounds(380,525,75,25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);
    }

    private class HandleSubmitBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputText = playerInputTF.getText().toLowerCase();
            String result = controller.processInput(inputText);
            playerMessageLbl.setText(result);
            String roomData = controller.getRoomDesc();
            roomInfoTA.setText(roomData);
        }
    }

}