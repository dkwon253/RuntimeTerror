package com.runtimeterror.ui;

import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.Item;
import com.runtimeterror.model.Leaderboard;
import com.runtimeterror.model.Rooms;
import com.runtimeterror.sound.SoundManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.StringJoiner;

public class SwingUI extends JFrame {

    private final SoundManager soundManager = new SoundManager();
    private final PlayerMap roomMap = new PlayerMap();
    private PlayerInventory playerInventory;
    private Timer timer;
    private final SwingController controller;
    private final int FRAME_X_SIZE = 560;
    private final int FRAME_Y_SIZE = 900;
    private JTextArea roomInfoTA, inventoryInfoTA, leaderBoard;
    private JTextField playerInputTF;
    private JLabel playerStateLbl, gameTimerLbl, playerHealthLbl, playerMessageLbl, monsterLabel, imageTitleContainer, roomImageContainer, titleNameLbl, subTitleLbl;
    private JButton mapCommandBtn, inventoryBtn;
    private JButton easyBtn, mediumBtn, hardBtn, nextBtn, hallBtn;
    private int gameTime;
    private Image scaledTransparentStairs;
    private Image scaledImage;
    private static final Font titleFont = new Font("Chiller", Font.BOLD, 50);
    private static final Font normalFont = new Font("Chiller", Font.PLAIN, 25);

    public SwingUI(String title, SwingController controller) {
        super(title);
        this.controller = controller;
        setSize(FRAME_X_SIZE, FRAME_Y_SIZE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // calls the welcome screen
        welcomeScreen();
    }

    private void welcomeScreen() {
        getContentPane().setBackground(Color.black);
        showLeaderBoard();
        titleNameLbl = new JLabel("Runtime Terror", SwingConstants.CENTER);
        titleNameLbl.setBounds(150, 350, 250, 40);
        titleNameLbl.setForeground(Color.red);
        titleNameLbl.setFont(titleFont);
        add(titleNameLbl);

        subTitleLbl = new JLabel("Will your name be among the hall of survivors...", SwingConstants.CENTER);
        subTitleLbl.setBounds(0, 500, 600, 40);
        subTitleLbl.setForeground(Color.red);
        subTitleLbl.setFont(normalFont);
        add(subTitleLbl);

        nextBtn = new JButton("Start");
        nextBtn.setBounds(150, 600, 100, 50);
        nextBtn.setBackground(Color.black);
        nextBtn.setForeground(Color.red);
        nextBtn.setBorder(new RoundedBorder(15));
        nextBtn.addActionListener(new HandleWelcomeBtnClick());
        add(nextBtn);

        hallBtn = new JButton("The Hall");
        hallBtn.setBounds(260, 600, 100, 50);
        hallBtn.setBackground(Color.black);
        hallBtn.setForeground(Color.red);
        hallBtn.setBorder(new RoundedBorder(15));
        hallBtn.addActionListener(new HandleHallBtnClick());
        add(hallBtn);
    }

    private void difficultyPage() {
        getContentPane().removeAll();
        getContentPane().setBackground(Color.black);
        setLayout(null);

        JLabel titleLbl = new JLabel("Please choose your difficulty: ", SwingConstants.CENTER);
        titleLbl.setOpaque(true);
        titleLbl.setBackground(Color.black);
        titleLbl.setForeground(Color.red);
        titleLbl.setBounds(0,350,200,200);

        easyBtn = new JButton("Easy");
        mediumBtn = new JButton("Medium");
        hardBtn = new JButton("Hard");

        easyBtn.setBounds(0, 350, 100, 100);
        easyBtn.setBackground(Color.darkGray);
        easyBtn.setForeground(Color.red);
        easyBtn.setBorder(new RoundedBorder(15));

        mediumBtn.setBounds(100, 350, 30, 30);
        mediumBtn.setBackground(Color.black);
        mediumBtn.setForeground(Color.red);
        mediumBtn.setOpaque(true);
        mediumBtn.setBorder(new RoundedBorder(15));

        hardBtn.setBounds(200, 350, 100, 100);
        hardBtn.setBackground(Color.black);
        hardBtn.setForeground(Color.red);
        hardBtn.setOpaque(true);
        hardBtn.setBorder(new RoundedBorder(15));

        add(titleLbl);
        add(easyBtn);
        add(mediumBtn);
        add(hardBtn);

        easyBtn.addActionListener(new HandleDifficultyBtnClick());
        mediumBtn.addActionListener(new HandleDifficultyBtnClick());
        hardBtn.addActionListener(new HandleDifficultyBtnClick());

        revalidate();
        repaint();
    }

    // Takes user to leader board
    private void showLeaderBoard() {
        List<Leaderboard> lb = controller.getLeaderboard(10);

        StringJoiner stringJoiner = new StringJoiner(" \n");
        leaderBoard = new JTextArea(25, 40);
        leaderBoard.setBounds(30, 315, 500, 300);
        leaderBoard.setBackground(Color.black);
        leaderBoard.setForeground(Color.red);
        leaderBoard.setEditable(false);
        leaderBoard.setLineWrap(true);
        leaderBoard.setWrapStyleWord(true);
        for (Leaderboard user : lb) {
            stringJoiner.add(user.getUserName() + " " + user.getRuntime());

            System.out.println(user.getRuntime());
        }
        leaderBoard.setText(stringJoiner.toString());

    }

    private void startGame(SwingController controller) {
        getContentPane().removeAll();
        getContentPane().setBackground(Color.black);
        setLayout(null);
        Image imgTitle = null;
        try {
            //imgTitle = ImageIO.read(new File("Game/Icons/titleImage.png"));
            imgTitle = ImageIO.read(new File("Game/Icons/darkTitle.png"));
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
        revalidate();
        repaint();
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
        playerInputTF.setText("");
        playRoomSounds(roomData, result);
        if (controller.isGameOver()) {
            endGame(controller.isKilledByMonster());
        }
        controller.resetRound();
    }

    private void setupVolumeControlsBtn() {
        JButton volumeControlsBtn = new JButton();
        volumeControlsBtn.setBounds(45, 810, 50, 50);
        volumeControlsBtn.setBackground(Color.black);
        volumeControlsBtn.setOpaque(true);
        volumeControlsBtn.setBorderPainted(false);
        volumeControlsBtn.addActionListener(new HandleVolumeControlsBtnClick());
        Image img = null;
        try {
            img = ImageIO.read(new File("Game/Icons/soundIcon3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        volumeControlsBtn.setIcon(new ImageIcon(img));
        add(volumeControlsBtn);
    }

    private void setupSubmitCommandBtn() {
        JButton submitCommandBtn = new JButton();
        submitCommandBtn.setBounds(455, 825, 75, 25);
        submitCommandBtn.setText("Do it");
        submitCommandBtn.setBackground(Color.black);
        submitCommandBtn.setForeground(Color.red);
        submitCommandBtn.setOpaque(true);
        submitCommandBtn.setBorder(new LineBorder(Color.darkGray));
        submitCommandBtn.addActionListener(new HandleSubmitBtnClick());
        add(submitCommandBtn);
    }

    private void setupPlayerInputTF() {
        playerInputTF = new JTextField();
        playerInputTF.setBounds(30, 750, 500, 25);
        playerInputTF.setBackground(Color.black);
        playerInputTF.setForeground(Color.white);
        playerInputTF.setBorder(new LineBorder(Color.darkGray));
        playerInputTF.addActionListener(new HandleEnterPressOnPlayerInputTF());
        add(playerInputTF);
    }

    private void setupMapButton() {
        mapCommandBtn = new JButton();
        mapCommandBtn.setBounds(300, 825, 75, 25);
        mapCommandBtn.setText("Map");
        mapCommandBtn.setBackground(Color.black);
        mapCommandBtn.setForeground(Color.red);
        mapCommandBtn.setOpaque(true);
        mapCommandBtn.setBorder(new LineBorder(Color.darkGray));
        mapCommandBtn.addActionListener(new HandlePlayerMapBtnClick());
        mapCommandBtn.setVisible(false);
        add(mapCommandBtn);
    }

    private void setupInventoryButton() {
        inventoryBtn = new JButton();
        inventoryBtn.setBounds(205, 825, 90, 25);
        inventoryBtn.setText("Inventory");
        inventoryBtn.setBackground(Color.black);
        inventoryBtn.setForeground(Color.red);
        inventoryBtn.setOpaque(true);
        inventoryBtn.setBorder(new LineBorder(Color.darkGray));
        inventoryBtn.addActionListener(new HandlePlayerInventoryBtnClick());
        inventoryBtn.setVisible(false);
        add(inventoryBtn);
    }

    private void setupSaveGameMsgLbl() {
        JLabel saveGameMsgLbl = new JLabel("use save/load commands to save/load game", SwingConstants.LEFT);
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
        playerStateLbl.setForeground(Color.red);
        add(playerStateLbl);
    }

    private void setupPlayerHealth() {
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
        inventoryInfoTA.setBackground(Color.black);
        inventoryInfoTA.setForeground(Color.red);
        inventoryInfoTA.setOpaque(true);
        inventoryInfoTA.setBorder(new LineBorder(Color.darkGray));
        inventoryInfoTA.setText(controller.getInventory());
        add(inventoryInfoTA);
    }

    private void setupPlayerMessageLbl() {
        playerMessageLbl = new JLabel("", SwingConstants.CENTER);
        playerMessageLbl.setBounds(30, 615, 500, 25);
        playerMessageLbl.setForeground(Color.red);
        add(playerMessageLbl);
    }

    private void setupRoomInfoTA(SwingController controller) {
        roomInfoTA = new JTextArea(25, 40);
        roomInfoTA.setBounds(30, 315, 500, 300);
        roomInfoTA.setEditable(false);
        roomInfoTA.setLineWrap(true);
        roomInfoTA.setWrapStyleWord(true);
        roomInfoTA.setBackground(Color.black);
        roomInfoTA.setForeground(Color.red);
        roomInfoTA.setOpaque(true);
        roomInfoTA.setBorder(new LineBorder(Color.darkGray));
        roomInfoTA.setText(controller.getRoomDesc());
        add(roomInfoTA);
    }

    private void setupImageContainer() {
        roomImageContainer = new JLabel(getResizedRoomImage("Game/Icons/masterbathroom.jpg"));
        roomImageContainer.setBounds(30, 50, 500, 260);
        add(roomImageContainer);
    }

    private void setupImageTitle(Image imgTitle) {
        ImageIcon imageTitle = new ImageIcon(imgTitle);
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

    private void setupRoomItemPic(SwingController controller) {
        roomImageContainer.removeAll();

        for (Item item : controller.getRoomItems()) {
            if (item.getItemImagePath() != null) {
                JLabel roomItemLbl = new JLabel();
                Image scaledImage = null;
                Image itemImg = null;
                try {
                    itemImg = ImageIO.read(new File(item.getItemImagePath()));
                    scaledImage = itemImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                roomItemLbl.setIcon(new ImageIcon(scaledImage));
                roomItemLbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        processSubmitInput("get " + item.getName());
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Border border = BorderFactory.createLineBorder(Color.YELLOW, 1);
                        roomItemLbl.setBorder(border);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        roomItemLbl.setBorder(null);
                    }
                });
                Random random = new Random();
                int x = random.nextInt(425) + 10;
                int y = random.nextInt(190) + 10;
                roomItemLbl.setBounds(x, y, 50, 50);
                roomImageContainer.add(roomItemLbl);
            }
        }

        Map<String, Rooms> map = controller.getAvailableRooms();
        for (Map.Entry<String, Rooms> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            setDirectionLabel(entry, "north", 225, 0);
            setDirectionLabel(entry, "south", 225, 210);
            setDirectionLabel(entry, "east", 450, 105);
            setDirectionLabel(entry, "west", 0, 105);
        }

        if (controller.hasStairs()) {
            JLabel stairsLbl = new JLabel();
            Image stairs = null;
            Image transparentStairs = null;
            String filePath = "Game/Icons/stairs.png";
            try {
                stairs = ImageIO.read(new File(filePath));
                transparentStairs = getTransparentImg(ImageIO.read(new File(filePath)), .5f);
                scaledImage = stairs.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                scaledTransparentStairs = transparentStairs.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stairsLbl.setIcon(new ImageIcon(scaledTransparentStairs));
            stairsLbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    processSubmitInput("take stairs");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    stairsLbl.setIcon(new ImageIcon(scaledImage));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    stairsLbl.setIcon(new ImageIcon(scaledTransparentStairs));
                }
            });
            stairsLbl.setBounds(0, 210, 50, 50);
            roomImageContainer.add(stairsLbl);
        }

        if (controller.hasElevator()) {
            JLabel elevatorLbl = new JLabel();
            Image elevator = null;
            Image transparentElevator = null;
            Image scaledElevatorImage = null;
            Image scaledTransparentElevator = null;
            String filePath = "Game/Icons/elevator.png";
            try {
                elevator = ImageIO.read(new File(filePath));
                transparentElevator = getTransparentImg(ImageIO.read(new File(filePath)), .5f);
                scaledElevatorImage = elevator.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                scaledTransparentElevator = transparentElevator.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            elevatorLbl.setIcon(new ImageIcon(scaledTransparentElevator));
            Image finalScaledElevatorImage = scaledElevatorImage;
            Image finalScaledTransparentElevator = scaledTransparentElevator;
            elevatorLbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    processSubmitInput("take elevator");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    elevatorLbl.setIcon(new ImageIcon(finalScaledElevatorImage));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    elevatorLbl.setIcon(new ImageIcon(finalScaledTransparentElevator));
                }
            });
            elevatorLbl.setBounds(450, 210, 50, 50);
            roomImageContainer.add(elevatorLbl);
        }
        revalidate();
        repaint();
    }

    private void setDirectionLabel(Map.Entry<String, Rooms> entry, String direction, int x, int y) {
        if (entry.getKey().equals(direction) && entry.getValue() != null) {
            JLabel directionLbl = new JLabel();
            Image scaledImage = null;
            Image transparentDirection = null;
            Image directionImg = null;
            Image scaledTransparentDirection = null;
            String filePath = "Game/Icons/" + direction + ".png";
            try {
                directionImg = ImageIO.read(new File(filePath));
                transparentDirection = getTransparentImg(ImageIO.read(new File(filePath)), .5f);
                scaledImage = directionImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                scaledTransparentDirection = transparentDirection.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            directionLbl.setIcon(new ImageIcon(scaledTransparentDirection));
            Image finalScaledImage = scaledImage;
            Image finalScaledTransparentDirection = scaledTransparentDirection;
            directionLbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    processSubmitInput("go " + entry.getKey());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    directionLbl.setIcon(new ImageIcon(finalScaledImage));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    directionLbl.setIcon(new ImageIcon(finalScaledTransparentDirection));
                }
            });
            directionLbl.setBounds(x, y, 50, 50);
            roomImageContainer.add(directionLbl);
        }
    }

    private Image getTransparentImg(Image image, float opacity) {
        BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g.setComposite(c);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return img;
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
            message = "You have successfully escaped the mansion. Now go live to the fullest";
            controller.addToLeaderboard("", 90);// need to add the input filed to get this info
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
        playerHealthLbl.setText("Health: " + controller.getPlayerHealth());
        imageTitleContainer.setVisible(true);
        monsterLabel.setVisible(false);
        soundManager.stopExtraSFX();
        roomImageContainer.setIcon(getResizedRoomImage(controller.getRoomImagePath()));
    }

    private ImageIcon getResizedRoomImage(String imagePath) {
        Image img = null;
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(500, 260, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
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
            SoundControls sc = new SoundControls("Volume", soundManager, getLocation());
            sc.setVisible(true);
        }
    }

    private class HandleWelcomeBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            difficultyPage();
        }
    }

    // send the user to leader board on click
    private class HandleHallBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showLeaderBoard();
            add(leaderBoard);

            System.out.println("button press");
        }
    }

    private class HandleDifficultyBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(easyBtn)) {
                controller.setupGameDifficulty("easy");
                startGame(controller);
            } else if (e.getSource().equals(mediumBtn)) {
                controller.setupGameDifficulty("medium");
                startGame(controller);
            } else if (e.getSource().equals(hardBtn)) {
                controller.setupGameDifficulty("hard");
                startGame(controller);
            }
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
            } else if (gameTime <= 120) {
                gameTimerLbl.setForeground(Color.orange);
            } else {
                gameTimerLbl.setForeground(Color.white);
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
                        }
                    });
                    add(playerUsableInventoryLbl);
                }
            }
            revalidate();
            repaint();
        }
    }

    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}