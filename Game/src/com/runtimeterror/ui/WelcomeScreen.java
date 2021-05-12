package com.runtimeterror.ui;

import org.apache.commons.lang3.builder.Diff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {

    private JLabel titleNameLabel, subTitleLabel;
    private JButton nextButton;
    private static final Font titleFont = new Font("Times New Roman", Font.BOLD, 30);
    private static final Font normalFont = new Font("Times New Roman", Font.PLAIN, 15);
    private HandleBtnClick btnClick = new HandleBtnClick();

    public WelcomeScreen() {
        setTitle("Runtime Terror");
        setSize(560,900);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.black);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupPageTitle();
        setupSubTitle();
        setupStartButton();
    }

    private void setupPageTitle() {
        titleNameLabel = new JLabel("Runtime Terror",SwingConstants.CENTER);
        titleNameLabel.setBounds(150, 350, 250, 40);
        titleNameLabel.setForeground(Color.red);
        titleNameLabel.setFont(titleFont);
        add(titleNameLabel);
    }

    private void setupSubTitle() {
        subTitleLabel = new JLabel("Will your name be among the hall of survivors...", SwingConstants.CENTER);
        subTitleLabel.setBounds(0,500, 600, 40);
        subTitleLabel.setForeground(Color.red);
        subTitleLabel.setFont(normalFont);
        add(subTitleLabel);
    }

    private void setupStartButton() {
        nextButton = new JButton("Start");
        nextButton.setBounds(250, 600, 50, 40);
        nextButton.setBackground(Color.black);
        nextButton.setForeground(Color.black);
        nextButton.addActionListener(new HandleBtnClick());
        add(nextButton);
    }

    private class HandleBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            DifficultyScreen screen = new DifficultyScreen();
            screen.setVisible(true);
        }
    }

    public static void main(String args[]) throws Exception {
        WelcomeScreen ui = new WelcomeScreen();
        ui.setVisible(true);
    }
}


