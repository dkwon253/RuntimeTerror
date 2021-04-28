package com.runtimeterror.sound;

public class SoundTestV2 {
    public static void main(String[] args) {
        SoundManagerV2 sm2 = new SoundManagerV2();

        sm2.playBGM("Game/Sounds/ring08.wav");
        sm2.playRoomSFX("Game/Sounds/Glass.aiff",false);
        do{
            try {Thread.sleep(50);}
            catch (InterruptedException e) {e.printStackTrace();}
        }while (sm2.isActive());
    }
}
