package com.runtimeterror.sound;

public class SoundTestV2 {
    public static void main(String[] args) {
        SoundManagerV2 sm2 = new SoundManagerV2();

        sm2.playBGM("Game/Sounds/BGM.wav");
        sm2.playRoomSFX("Game/Sounds/wind.wav",true);
        int x = 0;
        do{
            if (x == 150){
                System.out.println("turning off BGM");
                sm2.turnOffBGM();
            }
            if (x == 300){
                System.out.println("turning on BGM");
                sm2.turnOnBGM();
            }
            if (x == 400){
                System.out.println("turning off SFX");
                sm2.turnOffSFX();
            }
            if (x == 500){
                System.out.println("turning on SFX");
                sm2.turnOnSFX();
            }
            if (x == 600){
                System.out.println("turning down BGM by 50%");
                sm2.setBGMVolume(0.5f);
            }
            if (x == 700){
                System.out.println("turning down BGM to 75%");
                sm2.setBGMVolume(0.25f);
            }
            if (x == 800){
                System.out.println("turning down SFX to 50%");
                sm2.setSFXVolume(0.5f);
            }
            try {Thread.sleep(50);}
            catch (InterruptedException e) {e.printStackTrace();}
            x++;
        }while (sm2.isActive());
    }
}
