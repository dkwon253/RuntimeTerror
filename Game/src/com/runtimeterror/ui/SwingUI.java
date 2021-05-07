package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;
import com.runtimeterror.sound.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SwingUI extends JFrame {

    private SoundManager soundManager = new SoundManager();
    private PlayerMap player = new PlayerMap();
    private SwingController controller;
    private final int FRAME_X_SIZE = 520;
    private final int FRAME_Y_SIZE = 900;
    private JTextArea roomInfoTA;
    private JTextArea inventoryInfoTA;
    private JTextField playerInputTF;
    private JLabel playerStateLbl;
    private JLabel playerHealthLbl;
    private JLabel saveGameMsgLbl;
    private JLabel playerMessageLbl;
    private JLabel monsterInRoomLbl;
    private JLabel monsterNearByLbl;
    private JLabel imageTitleContainer;
    private JLabel roomImageContainer;
    private ImageIcon imageTitle;
    private JButton mapCommandBtn;
    private JButton volumeControlsBtn;
    private JButton submitCommandBtn;

    // CTOR
    public SwingUI(String title, SwingController controller) {
        super(title);
        this.controller = controller;

        setLocation(100, 100);
        setSize(FRAME_X_SIZE, FRAME_Y_SIZE);
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
        setupMapButton();
        setPlayerHealth();

        soundManager.playBGM("Game/Sounds/BGM.wav");
        playRoomSounds(roomInfoTA.getText(), playerMessageLbl.getText());
    }

    private void setupVolumeControlsBtn() {
        volumeControlsBtn = new JButton();
        volumeControlsBtn.setBounds(25, 800, 50, 50);
        volumeControlsBtn.addActionListener(new HandleVolumeControlsBtnClick());
        Image img = null;
        try {
            img = ImageIO.read(new File("Game/Icons/soundIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        volumeControlsBtn.setIcon(new ImageIcon(img));
        add(volumeControlsBtn);
    }

    private void setupSubmitCommandBtn() {
        submitCommandBtn = new JButton();
        submitCommandBtn.setBounds(380, 800, 75, 25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);
    }

    private void setupPlayerInputTF() {
        playerInputTF = new JTextField();
        playerInputTF.setBounds(25, 755, 430, 25);
        playerInputTF.addActionListener(new HandleEnterPressOnPlayerInputTF());
        add(playerInputTF);
    }

    private void setupMapButton() {
        mapCommandBtn = new JButton();
        mapCommandBtn.setBounds(300, 800, 75, 25);
        mapCommandBtn.setText("Map");
        mapCommandBtn.addActionListener(new HandlePlayerMapBtnClick());
        add(mapCommandBtn);
    }

    private void setupSaveGameMsgLbl() {
        saveGameMsgLbl = new JLabel("use save/load commands to save/load game", SwingConstants.LEFT);
        saveGameMsgLbl.setBounds(25, 780, 430, 25);
        JLabel label = new JLabel("I'm bold");
        Font font = new Font("Courier", Font.BOLD, 12);
        saveGameMsgLbl.setFont(font);
        Font f = label.getFont();
        saveGameMsgLbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        add(saveGameMsgLbl);
    }

    private void setupPlayerStateLbl() {
        playerStateLbl = new JLabel("Status: Visible", SwingConstants.LEFT);
        playerStateLbl.setBounds(25, 730, 430, 23);
        add(playerStateLbl);
    }

    private void setPlayerHealth() {
        playerHealthLbl = new JLabel("Health: " + controller.getPlayerHealth(), SwingConstants.LEFT);
        playerHealthLbl.setBounds(200, 730, 430, 20);
        add(playerHealthLbl);
    }

    private void setupInventoryInfoTA(SwingController controller) {
        inventoryInfoTA = new JTextArea(5, 40);
        inventoryInfoTA.setBounds(25, 645, 430, 75);
        inventoryInfoTA.setEditable(false);
        inventoryInfoTA.setLineWrap(true);
        inventoryInfoTA.setWrapStyleWord(true);
        inventoryInfoTA.setText(controller.getInventory());
        add(inventoryInfoTA);
    }

    private void setupPlayerMessageLbl() {
        playerMessageLbl = new JLabel("", SwingConstants.CENTER);
        playerMessageLbl.setBounds(25, 615, 430, 25);
        add(playerMessageLbl);
    }

    private void setupRoomInfoTA(SwingController controller) {
        roomInfoTA = new JTextArea(25, 40);
        roomInfoTA.setBounds(25, 315, 430, 300);
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
        roomImageContainer.setBounds(0, 50, 500, 260);
        add(roomImageContainer);
    }

    private void setupImageTitle(Image imgTitle) {
        imageTitle = new ImageIcon(imgTitle);
        imageTitleContainer = new JLabel(imageTitle, SwingConstants.CENTER);
        imageTitleContainer.setBounds(0, 10, 500, 40);
        add(imageTitleContainer);
    }

    private void setupMonster() {
        monsterInRoomLbl = new JLabel("The monster is Here!!!", SwingConstants.CENTER);
        monsterNearByLbl = new JLabel("The monster is close...", SwingConstants.CENTER);
        monsterInRoomLbl.setBounds(25, 20, 430, 25);
        monsterNearByLbl.setBounds(monsterInRoomLbl.getBounds());
        monsterInRoomLbl.setForeground(Color.RED);
        monsterNearByLbl.setForeground(Color.BLACK);
        monsterInRoomLbl.setVisible(false);
        monsterNearByLbl.setVisible(false);
        add(monsterInRoomLbl);
        add(monsterNearByLbl);
    }

    private void processSubmitInput() {
        String inputText = playerInputTF.getText().toLowerCase();
        controller.processInput(inputText);
        String result = controller.getMessageLabel();
        playerMessageLbl.setText(result);
        String roomData = controller.getRoomDesc();
        roomInfoTA.setText(roomData);
        String invData = controller.getInventory();
        inventoryInfoTA.setText(invData);
        playerHealthLbl.setText("Health: " + controller.getPlayerHealth());
        roomImageContainer.setIcon(new ImageIcon(controller.getRoomImagePath()));
        player.getMapLocationLbl().setIcon(new ImageIcon(controller.getRoomMapPath()));
        if (controller.getStatus()) {
            playerStateLbl.setText("Status: Hidden");
        } else {
            playerStateLbl.setText("Status: Visible");
        }
        handleMonsterData(controller.getMonsterData());
        playerInputTF.setText("");
        playRoomSounds(roomData, result);
        if (controller.isGameOver()) {
            System.out.println("Handle game over case here...");
            endGame(controller.isKilledByMonster());
        }
        controller.resetRound();
    }

    private void endGame(boolean wasKilled) {
        Image img = null;
        String message = "";
        String iconImage = "Game/Icons/";
        if (wasKilled) {
            iconImage += "skull.png";
            message = "You are now dead";
        } else {
            iconImage += "freedom.png";
        }
        try {
            img = ImageIO.read(new File(iconImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

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
        } else {
            controller.startNewGame();
            playerMessageLbl.setText("Game restarted");
            String roomData = controller.getRoomDesc();
            roomInfoTA.setText(roomData);
            String invData = controller.getInventory();
            inventoryInfoTA.setText(invData);
            playerStateLbl.setText("Status: Visible");
            playerInputTF.setText("");
            playRoomSounds(roomData, "");
            imageTitleContainer.setVisible(true);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(false);
            soundManager.stopExtraSFX();
            roomImageContainer.setIcon(new ImageIcon(controller.getRoomImagePath()));
        }

    }

    private class HandleSubmitBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSubmitInput();
        }
    }

    private class HandlePlayerMapBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (controller.getInventory().contains("map")) {
                System.out.println("PlayerMap has been clicked.");
                player.setVisible(true);
            } else {
                playerMessageLbl.setText("You don't have the capability to do that yet.");
            }
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
            SoundControls sc = new SoundControls("Volume", soundManager, getLocation());
            sc.setVisible(true);
        }
    }

    private void playRoomSounds(String roomText, String messageText) {
        String[] splitString = roomText.split("\n");
        soundManager.stopRoomSFX();
        if ("Master Bathroom".equals(splitString[1]) || "Bathroom Two".equals(splitString[1]) || "Bathroom Three".equals(splitString[1])) {
            soundManager.playRoomSFX("Game/Sounds/bathroom.wav", true);
        }
        if ("Courtyard".equals(splitString[1])) {
            soundManager.playRoomSFX("Game/Sounds/wind.wav", true);
        }
        if ("Theater".equals(splitString[1])) {
            soundManager.playRoomSFX("Game/Sounds/static.wav", true);
        }
    }

    private void handleMonsterData(int monsterInfo) {
        if (monsterInfo == -1) {
            imageTitleContainer.setVisible(true);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(false);
            soundManager.stopExtraSFX();
        } else if (monsterInfo == 1) {
            imageTitleContainer.setVisible(false);
            monsterInRoomLbl.setVisible(false);
            monsterNearByLbl.setVisible(true);
            soundManager.playExtraSFX("Game/Sounds/footsteps.wav", true);
        } else {
            imageTitleContainer.setVisible(false);
            monsterInRoomLbl.setVisible(true);
            monsterNearByLbl.setVisible(false);
            soundManager.playExtraSFX("Game/Sounds/breathing.wav", true);
        }
    }
}