package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.Item;
import com.runtimeterror.sound.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class SwingUI extends JFrame {

    private SoundManager soundManager = new SoundManager();
    private PlayerMap roomMap = new PlayerMap();
    private SwingController controller;
    private final int FRAME_X_SIZE = 560;
    private final int FRAME_Y_SIZE = 900;
    private JTextArea roomInfoTA;
    private JTextArea inventoryInfoTA;
    private JTextField playerInputTF;
    private JLabel playerStateLbl;
    private JLabel gameTimerLbl;
    private JLabel playerHealthLbl;
    private JLabel saveGameMsgLbl;
    private JLabel playerMessageLbl;
    private JLabel monsterLabel;
    private PlayerInventory playerInventory;
    private JLabel imageTitleContainer;
    private JLabel roomImageContainer;
    private ImageIcon imageTitle;
    private JButton mapCommandBtn;
    private JButton inventoryBtn;
    private JButton volumeControlsBtn;
    private JButton submitCommandBtn;
    private int gameTime;
    private Timer timer;

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
        setupRoomItemPic(controller);
        setupPlayerMessageLbl();
        setupInventoryInfoTA(controller);
        setupPlayerStateLbl();
        setupSaveGameMsgLbl();
        setupPlayerInputTF();
        setupSubmitCommandBtn();
        setupVolumeControlsBtn();
        setupMapButton();
        setupPlayerHealth();
        setupGameTimer();
        setupTimer();
        setupInventoryButton();

        soundManager.playBGM("Game/Sounds/BGM.wav");

        playRoomSounds(roomInfoTA.getText(), playerMessageLbl.getText());
        playerInventory = new PlayerInventory();
    }

    private void processSubmitInput(String inputText) {
        controller.processInput(inputText);
        String result = controller.getMessageLabel();
        if (controller.isCloseToDying()) {
            playerMessageLbl.setText("You are dangerously closed to dying!");
        } else {
            playerMessageLbl.setText(result);
        }
        String roomData = controller.getRoomDesc();
        roomInfoTA.setText(roomData);
        String invData = controller.getInventory();
        inventoryInfoTA.setText(invData);
        playerHealthLbl.setText("Health: " + controller.getPlayerHealth());
        changeHealthColors();
        roomImageContainer.setIcon(getResizedRoomImage(controller.getRoomImagePath()));
        setupRoomItemPic(controller);
        roomMap.getMapLocationLbl().setIcon(getResizedMap(controller.getRoomMapPath()));
        if (controller.getStatus()) {
            playerStateLbl.setText("Status: Hidden");
        } else {
            playerStateLbl.setText("Status: Visible");
        }
        boolean hasMap = controller.hasMap();
        mapCommandBtn.setVisible(hasMap);
        boolean hasItems = controller.hasItems();
        inventoryBtn.setVisible(hasItems);
        if (!hasItems) {
            playerInventory.setVisible(false);
        }
        if (!hasMap) {
            roomMap.setVisible(false);
        }
        playerInventory.updateUsableInventory();
        handleMonsterData();
        System.out.println(controller.getPlayerItems());
        playerInputTF.setText("");
        playRoomSounds(roomData, result);
        System.out.println(controller.isMonsterNear());
        if (controller.isGameOver()) {
            System.out.println("Handle game over case here...");
            endGame(controller.isKilledByMonster());
        }
        controller.resetRound();
    }

    private void setupVolumeControlsBtn() {
        volumeControlsBtn = new JButton();
        volumeControlsBtn.setBounds(45, 810, 50, 50);
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
        submitCommandBtn.setBounds(455, 825, 75, 25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);
    }

    private void setupPlayerInputTF() {
        playerInputTF = new JTextField();
        playerInputTF.setBounds(30, 750, 500, 25);
        playerInputTF.addActionListener(new HandleEnterPressOnPlayerInputTF());
        add(playerInputTF);
    }

    private void setupMapButton() {
        mapCommandBtn = new JButton();
        mapCommandBtn.setBounds(300, 825, 75, 25);
        mapCommandBtn.setText("Map");
        mapCommandBtn.addActionListener(new HandlePlayerMapBtnClick());
        mapCommandBtn.setVisible(false);
        add(mapCommandBtn);
    }

    private void setupInventoryButton() {
        inventoryBtn = new JButton();
        inventoryBtn.setBounds(205, 825, 90, 25);
        inventoryBtn.setText("Inventory");
        inventoryBtn.addActionListener(new HandlePlayerInventoryBtnClick());
        inventoryBtn.setVisible(false);
        add(inventoryBtn);
    }

    private void setupSaveGameMsgLbl() {
        saveGameMsgLbl = new JLabel("use save/load commands to save/load game", SwingConstants.LEFT);
        saveGameMsgLbl.setBounds(30, 780, 500, 25);
        JLabel label = new JLabel("I'm bold");
        Font font = new Font("Courier", Font.BOLD, 12);
        saveGameMsgLbl.setFont(font);
        Font f = label.getFont();
        saveGameMsgLbl.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        add(saveGameMsgLbl);
    }

    private void setupPlayerStateLbl() {
        playerStateLbl = new JLabel("Status: Visible", SwingConstants.LEFT);
        playerStateLbl.setBounds(30, 730, 500, 20);
        add(playerStateLbl);
    }

    private void setupPlayerHealth() {
        //playerHealthLbl = new JLabel("Health: " + controller.getPlayerHealth(), SwingConstants.CENTER);
        playerHealthLbl = new JLabel();
        playerHealthLbl.setBounds(250, 730, 500, 20);
        playerHealthLbl.setHorizontalTextPosition(SwingConstants.CENTER);
        playerHealthLbl.setText("Health: " + controller.getPlayerHealth());
        playerHealthLbl.setForeground(Color.green);
        add(playerHealthLbl);
    }

    private void setupGameTimer() {
        gameTimerLbl = new JLabel();
        gameTimerLbl.setBounds(450, 730, 500, 20);
        gameTimerLbl.setForeground(Color.black);
        add(gameTimerLbl);
    }

    private void setupInventoryInfoTA(SwingController controller) {
        inventoryInfoTA = new JTextArea(5, 40);
        inventoryInfoTA.setBounds(30, 645, 500, 75);
        inventoryInfoTA.setEditable(false);
        inventoryInfoTA.setLineWrap(true);
        inventoryInfoTA.setWrapStyleWord(true);
        inventoryInfoTA.setText(controller.getInventory());
        add(inventoryInfoTA);
    }

    private void setupPlayerMessageLbl() {
        playerMessageLbl = new JLabel("", SwingConstants.CENTER);
        playerMessageLbl.setBounds(30, 615, 500, 25);
        add(playerMessageLbl);
    }

    private void setupRoomInfoTA(SwingController controller) {
        roomInfoTA = new JTextArea(25, 40);
        roomInfoTA.setBounds(30, 315, 500, 300);
        roomInfoTA.setEditable(false);
        roomInfoTA.setLineWrap(true);
        roomInfoTA.setWrapStyleWord(true);
        roomInfoTA.setText(controller.getRoomDesc());
        add(roomInfoTA);
    }

    private void setupImageContainer() {
//        Image roomImage = null;
//        try {
//            roomImage = ImageIO.read(new File("Game/Icons/masterbathroom.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        roomImageContainer = new JLabel(getResizedRoomImage("Game/Icons/masterbathroom.jpg"));
        roomImageContainer.setBounds(30, 50, 500, 260);
        add(roomImageContainer);
    }

    private void setupImageTitle(Image imgTitle) {
        imageTitle = new ImageIcon(imgTitle);
        imageTitleContainer = new JLabel(imageTitle, SwingConstants.CENTER);
        imageTitleContainer.setBounds(30, 10, 500, 40);
        add(imageTitleContainer);
    }

    private void setupMonster() {
        monsterLabel = new JLabel("", SwingConstants.CENTER);
        monsterLabel.setBounds(30, 20, 500, 25);
        monsterLabel.setForeground(Color.RED);
        monsterLabel.setVisible(false);
        add(monsterLabel);
    }

    private void setupTimer() {
        gameTime = controller.getTimeToEndGame();
        timer = new Timer(1000, new GameTimer());
        timer.start();
    }

    private void changeHealthColors() {
        if (controller.getPlayerHealth() <= 5) {
            playerHealthLbl.setForeground(Color.red);
        } else if (controller.getPlayerHealth() <= 10) {
            playerHealthLbl.setForeground(Color.orange);
        } else {
            playerHealthLbl.setForeground(Color.green);
        }
    }

    private void setupRoomItemPic(SwingController controller) {
        roomImageContainer.removeAll();
        for (Item item : controller.getRoomItems()) {
            if (item.getItemImagePath() != null) {
                JLabel roomItemLbl = new JLabel();
                Image scaledImage = null;
                Image usableInventory = null;
                try {
                    usableInventory = ImageIO.read(new File(item.getItemImagePath()));
                    scaledImage = usableInventory.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                roomItemLbl.setIcon(new ImageIcon(scaledImage));
                roomItemLbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        processSubmitInput("get " + item.getName());
                        System.out.println(item.getName());
                    }
                });
                Random random = new Random();
                int x = random.nextInt(425)+10;
                int y = random.nextInt(190)+10;
                roomItemLbl.setBounds(x, y, 50, 50);
                roomImageContainer.add(roomItemLbl);
            }
        }
        revalidate();
        repaint();
    }

    private void setupRoomItemPic(SwingController controller) {
        roomImageContainer.removeAll();
        for (Item item : controller.getRoomItems()) {
            if (item.getItemImagePath() != null) {
                JLabel roomItemLbl = new JLabel();
                Image scaledImage = null;
                Image usableInventory = null;
                try {
                    usableInventory = ImageIO.read(new File(item.getItemImagePath()));
                    scaledImage = usableInventory.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                roomItemLbl.setIcon(new ImageIcon(scaledImage));
                roomItemLbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        processSubmitInput("get " + item.getName());
                        System.out.println(item.getName());
                    }
                });
                Random random = new Random();
                int x = random.nextInt(425)+10;
                int y = random.nextInt(190)+10;
                roomItemLbl.setBounds(x, y, 50, 50);
                roomImageContainer.add(roomItemLbl);
            }
        }
        revalidate();
        repaint();
    }

    private void endGame(boolean isKilled) {
        timer.stop();
        Image img = null;
        String message = "";
        String iconImage = "Game/Icons/";
        if (isKilled) {
            iconImage += "skull.png";
            message = "You are now dead";
            playerMessageLbl.setText(message);
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
        if (n == 1) {
            System.exit(0);
        } else {
            resetGame();
        }
    }

    private void resetGame() {
        controller.startNewGame();
        playerMessageLbl.setText("Game restarted");
        mapCommandBtn.setVisible(false);
        roomMap.setVisible(false);
        inventoryBtn.setVisible(false);
        playerInventory.setVisible(false);
        playerInventory.updateUsableInventory();
        setupRoomItemPic(controller);
        String roomData = controller.getRoomDesc();
        roomInfoTA.setText(roomData);
        String invData = controller.getInventory();
        inventoryInfoTA.setText(invData);
        playerStateLbl.setText("Status: Visible");
        playerInputTF.setText("");
        playRoomSounds(roomData, "");
        setupTimer();
        handleMonsterData();
        playerHealthLbl.setForeground(Color.green);
        playerHealthLbl.setText("Health: " +controller.getPlayerHealth());
        imageTitleContainer.setVisible(true);
        monsterLabel.setVisible(false);
        soundManager.stopExtraSFX();
        roomImageContainer.setIcon(getResizedRoomImage(controller.getRoomImagePath()));
    }

    private ImageIcon getResizedRoomImage(String imagePath){
        Image img = null;
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(500, 260, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private ImageIcon getResizedMap(String imagePath){
        Image img = null;
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(560, 350, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
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

    private void handleMonsterData() {
        monsterLabel.setText(controller.getMonsterLabel());
        if (controller.isMonsterSameRoom()) {
//            monsterLabel.setText("The Monster is here!");
            imageTitleContainer.setVisible(false);
            monsterLabel.setVisible(true);
            soundManager.playExtraSFX("Game/Sounds/breathing.wav", true);
            soundManager.playHeartSFX("Game/Sounds/heartbeatFast.wav", true);
        } else if (controller.isMonsterNear()) {
            monsterLabel.setText("The Monster is nearby!");
            imageTitleContainer.setVisible(false);
            monsterLabel.setVisible(false);
            soundManager.playExtraSFX("Game/Sounds/footsteps.wav", true);
            soundManager.playHeartSFX("Game/Sounds/heartbeatMed.wav", true);

        } else {
            imageTitleContainer.setVisible(true);
            monsterLabel.setVisible(false);
            soundManager.stopExtraSFX();
            soundManager.stopHeartSFX();
        }
    }

    private class HandleSubmitBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSubmitInput(playerInputTF.getText().toLowerCase());
        }
    }

    private class HandlePlayerMapBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            roomMap.setVisible(true);
        }
    }

    private class HandlePlayerInventoryBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerInventory.setVisible(true);
        }
    }

    private class HandleEnterPressOnPlayerInputTF implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processSubmitInput(playerInputTF.getText().toLowerCase());
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

    private class GameTimer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameTime <= 0) {
                endGame(true);
            } else {
                changeTimerColor();
                gameTimerLbl.setHorizontalTextPosition(SwingConstants.RIGHT);
                gameTimerLbl.setText("Timer: " + computeTime());
            }
        }

        private String computeTime() {
            gameTime -= 1;
            int gameModulo = gameTime % 3600;
            int minutes = gameModulo / 60;
            int seconds = gameModulo % 60;
            String minuteString = (minutes < 10 ? "0" : "") + minutes;
            String secondsString = (seconds < 10 ? "0" : "") + seconds;
            return minuteString + ":" + secondsString;
        }

        private void changeTimerColor() {
            if (gameTime <= 60) {
                gameTimerLbl.setForeground(Color.red);
            } else if(gameTime <= 120) {
                gameTimerLbl.setForeground(Color.orange);
            } else {
                gameTimerLbl.setForeground(Color.black);
            }
        }
    }

    private class PlayerInventory extends JFrame {
        PlayerInventory() {
            setLayout(new GridLayout(controller.getPlayerItems().size(), 3));
            setSize(600, 600);
            setResizable(false);
            setTitle("Usable Inventory");
            setLocation(500, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        void updateUsableInventory() {
            getContentPane().removeAll();
            for (Item item : controller.getPlayerItems()) {
                if (item.getItemImagePath() != null) {
                    JLabel playerUsableInventoryLbl = new JLabel();
                    Image scaledImage = null;
                    Image usableInventory = null;
                    try {
                        usableInventory = ImageIO.read(new File(item.getItemImagePath()));
                        scaledImage = usableInventory.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playerUsableInventoryLbl.setIcon(new ImageIcon(scaledImage));
                    playerUsableInventoryLbl.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            processSubmitInput("use " + item.getName());
                            System.out.println(item.getName());
                        }
                    });
                    add(playerUsableInventoryLbl);
                }
            }
            revalidate();
            repaint();
        }
    }
}