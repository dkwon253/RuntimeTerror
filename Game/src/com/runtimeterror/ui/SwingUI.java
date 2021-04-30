package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;
import com.runtimeterror.sound.SoundManagerV2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class SwingUI extends JFrame{
    private SoundManagerV2 soundManagerV2 = new SoundManagerV2();
    private String currentRoomName = "";

    private final int FRAME_X_SIZE = 480;
    private final int FRAME_Y_SIZE = 640;

    private SwingController controller;
    private JTextArea roomInfoTA;
    private JTextArea inventoryInfoTA;
    private JTextField playerInputTF;
    private JLabel playerStateLbl;
    private JLabel saveGameMsgLbl;
    private JLabel playerMessageLbl;
    private JButton submitCommandBtn;
    private JLabel monsterInRoomLbl;
    private JLabel monsterNearByLbl;
    private JButton volumeControlsBtn;
    private ImageIcon imageTitle;
    private JLabel imageTitleContainer;


    // CTOR
    public SwingUI(String title, SwingController controller){
        super(title);
        this.controller = controller;

        setLocation(100,100);
        setSize(FRAME_X_SIZE,FRAME_Y_SIZE);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Image imgTitle = null;
        try {
            imgTitle = ImageIO.read(new File("Game/Icons/titleImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageTitle = new ImageIcon(imgTitle);
        imageTitleContainer = new JLabel(imageTitle, SwingConstants.CENTER);
        imageTitleContainer.setBounds(0,10,500,40);
        add(imageTitleContainer);

        monsterInRoomLbl = new JLabel("The monster is Here!!!",SwingConstants.CENTER);
        monsterNearByLbl = new JLabel("The monster is close...",SwingConstants.CENTER);
        monsterInRoomLbl.setBounds(25,20,430,25);
        monsterNearByLbl.setBounds(monsterInRoomLbl.getBounds());
        monsterInRoomLbl.setForeground(Color.RED);
        monsterNearByLbl.setForeground(Color.BLACK);
        monsterInRoomLbl.setVisible(false);
        monsterNearByLbl.setVisible(false);
        add(monsterInRoomLbl);
        add(monsterNearByLbl);


        roomInfoTA = new JTextArea(25,40);
        roomInfoTA.setBounds(25,50,430,300);
        roomInfoTA.setEditable(false);
        roomInfoTA.setLineWrap(true);
        roomInfoTA.setWrapStyleWord(true);
        roomInfoTA.setText(controller.getRoomDesc());
        add(roomInfoTA);

        playerMessageLbl = new JLabel("", SwingConstants.CENTER);
        playerMessageLbl.setBounds(25,360,430,25);
        add(playerMessageLbl);

        inventoryInfoTA = new JTextArea(5,40);
        inventoryInfoTA.setBounds(25,400,430,75);
        inventoryInfoTA.setEditable(false);
        inventoryInfoTA.setLineWrap(true);
        inventoryInfoTA.setWrapStyleWord(true);
        inventoryInfoTA.setText(controller.getInventory());
        add(inventoryInfoTA);

        playerStateLbl = new JLabel("Status: Visible", SwingConstants.LEFT);
        playerStateLbl.setBounds(25,475,430,25);
        add(playerStateLbl);

        saveGameMsgLbl = new JLabel("use save/load commands to save/load game", SwingConstants.LEFT);
        saveGameMsgLbl.setBounds(25,520,430,25);
        JLabel label = new JLabel("I'm bold");
        Font font = new Font("Courier", Font.BOLD,12);
        saveGameMsgLbl.setFont(font);
        Font f = label.getFont();
        saveGameMsgLbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        add(saveGameMsgLbl);

        playerInputTF = new JTextField();
        playerInputTF.setBounds(25,500,430,25);
        playerInputTF.addActionListener(new HandleEnterPressOnPlayerInputTF());
        add(playerInputTF);

        submitCommandBtn = new JButton();
        submitCommandBtn.setBounds(380,525,75,25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);

        volumeControlsBtn = new JButton();
        volumeControlsBtn.setBounds(25,550,50,50);
        volumeControlsBtn.addActionListener(new HandleVolumeControlsBtnClick());
        Image img = null;
        try {
            img = ImageIO.read(new File("Game/Icons/soundIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        volumeControlsBtn.setIcon(new ImageIcon(img));
        add(volumeControlsBtn);

        soundManagerV2.playBGM("Game/Sounds/BGM.wav");
        playRoomSounds(roomInfoTA.getText(),playerMessageLbl.getText());
    }

    private void processSubmitInput(){
        String inputText = playerInputTF.getText().toLowerCase();
        String result = controller.processInput(inputText);
        playerMessageLbl.setText(result);
        String roomData = controller.getRoomDesc();
        roomInfoTA.setText(roomData);
        String invData = controller.getInventory();
        inventoryInfoTA.setText(invData);
        if (controller.getStatus()){
            playerStateLbl.setText("Status: Hidden");
        }
        else{
            playerStateLbl.setText("Status: Visible");
        }
        handleMonsterData(controller.getMonsterData());
        playerInputTF.setText("");
        playRoomSounds(roomData,result);
    }

    private class HandleSubmitBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSubmitInput();
        }
    }

    private class HandleEnterPressOnPlayerInputTF implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSubmitInput();
        }
    }

    private class HandleVolumeControlsBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Vol button clicked.");
            SoundControls sc = new SoundControls("Volume",soundManagerV2,getLocation());
            sc.setVisible(true);
        }
    }

    private void playRoomSounds(String roomText, String messageText){
        String[] splitString = roomText.split("\n");
        //System.out.println(splitString[1]);
        if (!currentRoomName.equals(splitString)) {
            soundManagerV2.stopRoomSFX();
            if ("Master Bathroom".equals(splitString[1]) || "Bathroom Two".equals(splitString[1]) || "Bathroom Three".equals(splitString[1])) {
                soundManagerV2.playRoomSFX("Game/Sounds/bathroom.wav", true);
            }
            if ("Courtyard".equals(splitString[1])) {
                soundManagerV2.playRoomSFX("Game/Sounds/wind.wav", true);
            }
            if ("Theater".equals(splitString[1])) {
                soundManagerV2.playRoomSFX("Game/Sounds/static.wav", true);
            }
        }
    }

    private void handleMonsterData(int monsterInfo){
        if (monsterInfo == -1){
            imageTitleContainer.setVisible(true);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(false);
            soundManagerV2.stopExtraSFX();
        }
        else if (monsterInfo == 1){
            imageTitleContainer.setVisible(false);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(true);
            soundManagerV2.playExtraSFX("Game/Sounds/footsteps.wav",true);
        }
        else{
            imageTitleContainer.setVisible(false);
            monsterInRoomLbl.setVisible(true);
            monsterNearByLbl.setVisible(false);
            soundManagerV2.playExtraSFX("Game/Sounds/breathing.wav",true);
        }
    }
}