package com.runtimeterror.ui;

import com.runtimeterror.sound.SoundManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoundControls extends JFrame {
    private final int FRAME_X_SIZE = 230;
    private final int FRAME_Y_SIZE = 250;

    private JLabel BGMLbl;
    private JLabel SFXLbl;
    private JLabel BGMOnLbl;
    private JLabel SFXOnLbl;
    private JSlider BGMVolSlider;
    private JSlider SFXVolSlider;
    private JButton cancelBtn;
    private JButton submitBtn;
    private JCheckBox BGMChk;
    private JCheckBox SFXChk;

    private SoundManager sm;

    public SoundControls(String title, SoundManager sm, Point location){
        super(title);
        this.sm = sm;
        setLocation((int)location.getX()+25, (int)location.getY()+25);
        setSize(FRAME_X_SIZE,FRAME_Y_SIZE);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BGMLbl = new JLabel("BGM");
        BGMLbl.setBounds(25,25,50,25);
        add(BGMLbl);

        BGMChk = new JCheckBox("on");
        BGMChk.setBounds(75,25,50,25);
        BGMChk.setSelected(!sm.isBGMOff());
        BGMChk.addChangeListener(new HandleBGMChkChange());
        add(BGMChk);

        SFXLbl = new JLabel("SFX");
        SFXLbl.setBounds(25,75,50,25);
        add(SFXLbl);

        SFXChk = new JCheckBox("on");
        SFXChk.setBounds(75,75,50,25);
        SFXChk.setSelected(!sm.isSFXOff());
        SFXChk.addChangeListener(new HandleSFXChkChange());
        add(SFXChk);

        BGMVolSlider = new JSlider();
        BGMVolSlider.setBounds(10,50,130,25);
        BGMVolSlider.setPaintTicks(true);
        BGMVolSlider.setMajorTickSpacing(1);
        BGMVolSlider.setMaximum(10);
        BGMVolSlider.setMinimum(1);
        BGMVolSlider.setSnapToTicks(true);
        BGMVolSlider.setValue((int)(sm.getBGMVolume() * 10));
        BGMVolSlider.addChangeListener(new HandleBGMVolSliderChange());
        add(BGMVolSlider);

        SFXVolSlider = new JSlider();
        SFXVolSlider.setBounds(10,100,130,25);
        SFXVolSlider.setPaintTicks(true);
        SFXVolSlider.setMajorTickSpacing(1);
        SFXVolSlider.setMaximum(10);
        SFXVolSlider.setMinimum(1);
        SFXVolSlider.setSnapToTicks(true);
        SFXVolSlider.setValue((int)(sm.getSFXVolume() * 10));
        SFXVolSlider.addChangeListener(new HandleSFXVolSliderChange());
        add(SFXVolSlider);

        cancelBtn = new JButton("Back");
        cancelBtn.setBounds(25,150,100,25);
        cancelBtn.addActionListener(new HandleCancelBtnClick());
        add(cancelBtn);
    }

    private class HandleBGMVolSliderChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            sm.setBGMVolume((float)BGMVolSlider.getValue()/10f);
        }
    }

    private class HandleSFXVolSliderChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            sm.setSFXVolume((float)SFXVolSlider.getValue()/10f);
        }
    }

    private class HandleSFXChkChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (SFXChk.isSelected()){
                sm.turnOnSFX();
            }
            else{
                sm.turnOffSFX();
            }
        }
    }

    private class HandleBGMChkChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (BGMChk.isSelected()){
                sm.turnOnBGM();
            }
            else{
                sm.turnOffBGM();
            }
        }
    }

    private class HandleCancelBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
