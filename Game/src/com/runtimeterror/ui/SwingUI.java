package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.LoadRoomData;
import com.runtimeterror.model.Result;
import com.runtimeterror.model.Rooms;
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

    private final int FRAME_X_SIZE = 520;
    private final int FRAME_Y_SIZE = 900;

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
    private JLabel roomImageContainer;


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
        setupImageTitle(imgTitle);
        setupMonster();
        setupImageContainer();
        setupRoomInfoTA(controller);
        setupPlayerMessageLbl();
        setupInventoryInfoTA(controller);
        setupPlayerStateLbl();
        setupSaveGameMsgLbl();
        setupPlayerInputTF();
        setupSubmitCommandBtn();
        setupVolumeControlsBtn();

        soundManagerV2.playBGM("Game/Sounds/BGM.wav");
        playRoomSounds(roomInfoTA.getText(),playerMessageLbl.getText());
    }

    private void setupVolumeControlsBtn() {
        volumeControlsBtn = new JButton();
        volumeControlsBtn.setBounds(25,800,50,50);
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
    }

    private void setupSubmitCommandBtn() {
        submitCommandBtn = new JButton();
        submitCommandBtn.setBounds(380,800,75,25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);
    }

    private void setupPlayerInputTF() {
        playerInputTF = new JTextField();
        playerInputTF.setBounds(25,755,430,25);
        playerInputTF.addActionListener(new HandleEnterPressOnPlayerInputTF());
        add(playerInputTF);
    }

    private void setupSaveGameMsgLbl() {
        saveGameMsgLbl = new JLabel("use save/load commands to save/load game", SwingConstants.LEFT);
        saveGameMsgLbl.setBounds(25,780,430,25);
        JLabel label = new JLabel("I'm bold");
        Font font = new Font("Courier", Font.BOLD,12);
        saveGameMsgLbl.setFont(font);
        Font f = label.getFont();
        saveGameMsgLbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        add(saveGameMsgLbl);
    }

    private void setupPlayerStateLbl() {
        playerStateLbl = new JLabel("Status: Visible", SwingConstants.LEFT);
        playerStateLbl.setBounds(25,730,430,25);
        add(playerStateLbl);
    }

    private void setupInventoryInfoTA(SwingController controller) {
        inventoryInfoTA = new JTextArea(5,40);
        inventoryInfoTA.setBounds(25,645,430,75);
        inventoryInfoTA.setEditable(false);
        inventoryInfoTA.setLineWrap(true);
        inventoryInfoTA.setWrapStyleWord(true);
        inventoryInfoTA.setText(controller.getInventory());
        add(inventoryInfoTA);
    }

    private void setupPlayerMessageLbl() {
        playerMessageLbl = new JLabel("", SwingConstants.CENTER);
        playerMessageLbl.setBounds(25,615,430,25);
        add(playerMessageLbl);
    }

    private void setupRoomInfoTA(SwingController controller) {
        roomInfoTA = new JTextArea(25,40);
        roomInfoTA.setBounds(25,315,430,300);
        roomInfoTA.setEditable(false);
        roomInfoTA.setLineWrap(true);
        roomInfoTA.setWrapStyleWord(true);
        roomInfoTA.setText(controller.getRoomDesc());
        add(roomInfoTA);
    }

    private void setupImageContainer() {
        Image roomImage = null;
        try {
            roomImage = ImageIO.read(new File("Game/Icons/masterbathroom.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        roomImageContainer = new JLabel(new ImageIcon(roomImage), SwingConstants.CENTER);
        roomImageContainer.setBounds(0,50,500,260);
        add(roomImageContainer);
    }

    private void setupImageTitle(Image imgTitle) {
        imageTitle = new ImageIcon(imgTitle);
        imageTitleContainer = new JLabel(imageTitle, SwingConstants.CENTER);
        imageTitleContainer.setBounds(0,10,500,40);
        add(imageTitleContainer);
    }

    private void setupMonster() {
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
    }

    private void processSubmitInput(){
        String inputText = playerInputTF.getText().toLowerCase();
        String result = controller.processInput(inputText);
        playerMessageLbl.setText(result);
        String roomData = controller.getRoomDesc();
        roomInfoTA.setText(roomData);
        String invData = controller.getInventory();
        inventoryInfoTA.setText(invData);
        roomImageContainer.setIcon(new ImageIcon(controller.getRoomImagePath()));
        if (controller.getStatus()){
            playerStateLbl.setText("Status: Hidden");
        }
        else {
            playerStateLbl.setText("Status: Visible");
        }
        handleMonsterData(controller.getMonsterData());
        playerInputTF.setText("");
        playRoomSounds(roomData,result);
        if (controller.isGameOver()){
            System.out.println("Handle game over case here...");
            endGame(controller.isKilledByMonster());
        }
    }

    private void endGame(boolean wasKilled){
        Image img = null;
        String message = "";
        String iconImage = "Game/Icons/";
        if (wasKilled){
            iconImage += "skull.png";
            message = "You are now dead";
        }
        else {
            iconImage += "freedom.png";
        }
        try {
            img = ImageIO.read(new File(iconImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);

        Object[] options = {"Restart", "Exit Game"};
        int n = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(img),
                options,
                options[0]
        );
        System.out.println("you choose: " + n);
        if (n == 1) {
            System.exit(0);
        }
        else{
            controller.startNewGame();
            playerMessageLbl.setText("Game restarted");
            String roomData = controller.getRoomDesc();
            roomInfoTA.setText(roomData);
            String invData = controller.getInventory();
            inventoryInfoTA.setText(invData);
            playerStateLbl.setText("Status: Visible");
            playerInputTF.setText("");
            playRoomSounds(roomData,"");
            imageTitleContainer.setVisible(true);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(false);
            soundManagerV2.stopExtraSFX();
            roomImageContainer.setIcon(new ImageIcon(controller.getRoomImagePath()));
        }

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