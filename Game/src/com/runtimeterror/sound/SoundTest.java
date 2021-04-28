package com.runtimeterror.sound;

public class SoundTest {
    public static void main(String[] args) {
        SoundManager sm = new SoundManager();
        sm.playSound("Game/Sounds/ring08.wav");
        int x = 0;
        do{
            if (x == 15){
                sm.playSound("Game/Sounds/Glass.aiff");
            }
            try {Thread.sleep(50);}
            catch (InterruptedException e) {e.printStackTrace();}
            x++;
        }while (sm.isActive());
    }
}
