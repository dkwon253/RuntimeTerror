package com.runtimeterror.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;

public class SoundManagerV2 {
    private Clip BGM;
    private Clip roomSFX;
    private Clip extraSFX;

    public SoundManagerV2(){
        try {
            BGM = AudioSystem.getClip();
            roomSFX = AudioSystem.getClip();
            extraSFX = AudioSystem.getClip();
        }
        catch (LineUnavailableException lue) {lue.printStackTrace();}
    }

    public void playBGM(String BGMFile){
        if (BGM.isOpen()) {
            BGM.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(BGMFile));
            BGM.open(audioInputStream);
        }
        catch (UnsupportedAudioFileException e) {e.printStackTrace();}
        catch (Exception e) {e.printStackTrace();}
        BGM.start();
        BGM.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBGM(){
        BGM.stop();
        BGM.close();
    }

    public void playRoomSFX(String SFXFile, boolean loop){
        if (roomSFX.isOpen()) {
            roomSFX.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SFXFile));
            roomSFX.open(audioInputStream);
        }
        catch (UnsupportedAudioFileException e) {e.printStackTrace();}
        catch (Exception e) {e.printStackTrace();}
        roomSFX.start();
        if (loop) {
            roomSFX.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else{
            roomSFX.loop(0);
        }
    }

    public void stopRoomSFX(){
        roomSFX.stop();
        roomSFX.close();
    }

    public void playExtraSFX(String SFXFile, boolean loop){
        if (extraSFX.isOpen()) {
            extraSFX.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SFXFile));
            extraSFX.open(audioInputStream);
        }
        catch (UnsupportedAudioFileException e) {e.printStackTrace();}
        catch (Exception e) {e.printStackTrace();}
        extraSFX.start();
        if (loop) {
            extraSFX.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else{
            extraSFX.loop(0);
        }
    }

    public void stopExtraSFX(){
        extraSFX.stop();
        extraSFX.close();
    }

    public boolean isActive(){
        return (BGM.isActive() || roomSFX.isActive() || extraSFX.isActive());
    }

    public boolean isBGMActive(){
        return BGM.isActive();
    }

    public boolean isRoomSFXActive(){
        return roomSFX.isActive();
    }

    public boolean isExtraSFXActive(){
        return extraSFX.isActive();
    }
}
