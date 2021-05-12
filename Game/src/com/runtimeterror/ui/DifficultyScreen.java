package com.runtimeterror.ui;

import com.runtimeterror.controller.GameInterface;
import com.runtimeterror.controller.SwingController;
import com.runtimeterror.model.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultyScreen extends JFrame {

    private JLabel titleLbl;
    private JButton easyBtn,mediumBtn,hardBtn;
    private Container cp = getContentPane();
    private FlowLayout flow = new FlowLayout();

    public DifficultyScreen() {

        setTitle("Runtime Terror");
        setSize(560,900);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(flow);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLbl = new JLabel("Please choose your difficulty: ", SwingConstants.CENTER);
        easyBtn = new JButton("Easy");
        mediumBtn = new JButton("Medium");
        hardBtn = new JButton("Hard");

        cp.setLayout(flow);
        cp.add(titleLbl);
        cp.add(easyBtn);
        cp.add(mediumBtn);
        cp.add(hardBtn);

        easyBtn.addActionListener(new HandleDifficultyBtnClick());
        mediumBtn.addActionListener(new HandleDifficultyBtnClick());
        hardBtn.addActionListener(new HandleDifficultyBtnClick());
    }

    private class HandleDifficultyBtnClick implements ActionListener {
        GameInterface gi = new GameClient();
        SwingController controller = new SwingController(gi);
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(easyBtn)) {
                dispose();
                SwingUI ui = new SwingUI("Runtime Terror", controller);
                ui.setVisible(true);
            } else if(e.getSource().equals(mediumBtn)) {
                dispose();
                SwingUI ui = new SwingUI("Runtime Terror", controller);
                ui.setVisible(true);
            }  else if(e.getSource().equals(hardBtn)) {
                dispose();
                SwingUI ui = new SwingUI("Runtime Terror", controller);
                ui.setVisible(true);
            }
        }
    }
}
