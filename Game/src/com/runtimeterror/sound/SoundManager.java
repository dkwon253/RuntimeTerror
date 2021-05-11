package com.runtimeterror.sound;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private Clip BGM;
    private Clip HBG;
    private Clip roomSFX;
    private Clip extraSFX;
    private Clip heartSFX;
    private boolean BGMOff = false;
    private boolean HBGOff = false;
    private boolean SFXOff = false;
    private boolean SFXLoop = false;
    private boolean extraLoop = false;
    private boolean heartLoop = false;
    private float BMGVol = 0.5f;
    private float HBGVol = 0.5f;
    private float SFXVol = 0.6f;

    public SoundManager() {
        try {
            BGM = AudioSystem.getClip();
            HBG = AudioSystem.getClip();
            roomSFX = AudioSystem.getClip();
            extraSFX = AudioSystem.getClip();
            heartSFX = AudioSystem.getClip();
        } catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }
    }
        //Play background music
    public void playBGM(String BGMFile) {

        if (BGM.isOpen()) {
            BGM.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(BGMFile));
            BGM.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BGM.loop(Clip.LOOP_CONTINUOUSLY);
        if (!BGMOff) {
            BGM.start();
            setBGMVolume(BMGVol);
        }
    }

    public void stopBGM() {
        BGM.stop();
    }

    public void turnOffBGM() {
        stopBGM();
        BGMOff = true;
    }

    public void turnOnBGM() {
        BGM.setMicrosecondPosition(0L);
        BGM.start();
        BGMOff = false;
    }

    //background constant heartbeat
    public void playHBG(String HBGFile) {

        if (HBG.isOpen()) {
            HBG.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(HBGFile));
            HBG.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HBG.loop(Clip.LOOP_CONTINUOUSLY);
        if (!HBGOff) {
            HBG.start();
            heartSFX.stop();
            setBGMVolume(HBGVol);
        }
    }

    public void stopHBG() {
        HBG.stop();
    }

    public void turnOffHBG() {
        stopBGM();
        HBGOff = true;
    }

    public void turnOnHBG() {
        HBG.setMicrosecondPosition(0L);
        HBG.start();
        HBGOff = false;
    }


    //play sound for specific rooms
    public void playRoomSFX(String SFXFile, boolean loop) {
        if (roomSFX.isOpen()) {
            roomSFX.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SFXFile));
            roomSFX.open(audioInputStream);
            SFXLoop = loop;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!SFXOff) {
            roomSFX.start();
            setSFXVolume(SFXVol);
            if (loop) {
                roomSFX.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                roomSFX.loop(0);
            }
            setSFXVolume(SFXVol);
        }
    }

    public void stopRoomSFX() {
        roomSFX.stop();
    }

    public void turnOffSFX() {
        SFXOff = true;
        roomSFX.stop();
        extraSFX.stop();
        heartSFX.stop();

    }

    public void turnOnSFX() {
        SFXOff = false;
        roomSFX.setMicrosecondPosition(0L);
        roomSFX.start();

        if (SFXLoop) {
            roomSFX.loop(Clip.LOOP_CONTINUOUSLY);
        }
        extraSFX.setMicrosecondPosition(0L);
        extraSFX.start();

        if (extraLoop) {
            extraSFX.loop(Clip.LOOP_CONTINUOUSLY);
        }

        heartSFX.setMicrosecondPosition(0L);
        heartSFX.start();


        if (heartLoop) {
            heartSFX.loop(Clip.LOOP_CONTINUOUSLY);
        }
        setSFXVolume(SFXVol);
    }

    public void playExtraSFX(String SFXFile, boolean loop) {
        if (extraSFX.isOpen()) {
            extraSFX.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SFXFile));
            extraSFX.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!SFXOff) {
            extraSFX.start();
            setSFXVolume(SFXVol);
            if (loop) {
                extraSFX.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                extraSFX.loop(0);
            }
            setSFXVolume(SFXVol);
        }
    }

    public void stopExtraSFX() {
        extraSFX.stop();
        extraSFX.close();
    }

    public void playHeartSFX(String SFXFile, boolean loop) {
        if (heartSFX.isOpen()) {
            heartSFX.close();
            HBG.close();
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SFXFile));
            heartSFX.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!SFXOff) {
            heartSFX.start();
            setSFXVolume(SFXVol);
            if (loop) {
                heartSFX.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                heartSFX.loop(0);
            }
            setSFXVolume(SFXVol);
        }
    }

    public void stopHeartSFX() {
        heartSFX.stop();
        heartSFX.close();
    }

    public boolean isActive() {
        return (BGM.isActive() ||  HBG.isActive() || roomSFX.isActive() || extraSFX.isActive() || heartSFX.isActive());
    }

    public boolean isBGMActive() {
        return BGM.isActive();
    }
    public boolean isHBGActive() {
        return HBG.isActive();
    }

    public boolean isRoomSFXActive() {
        return roomSFX.isActive();
    }

    public boolean isExtraSFXActive() {
        return extraSFX.isActive();
    }

    public boolean isHeartSFXActive() {
        return heartSFX.isActive();
    }

    public float getBGMVolume() {
        FloatControl volume = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, volume.getValue() / 20f);
    }

    public void setBGMVolume(float newVol) {
        if (newVol >= 0.0f && newVol <= 1.0f) {
            BMGVol = newVol;
            FloatControl volume = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(20f * (float) Math.log10(newVol));
        }
    }

    public float getSFXVolume() {
        FloatControl volume = (FloatControl) roomSFX.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, volume.getValue() / 20f);
    }

    public void setSFXVolume(float newVol) {
        if (newVol >= 0.0f && newVol <= 1.0f) {
            SFXVol = newVol;
            FloatControl volume1 = (FloatControl) roomSFX.getControl(FloatControl.Type.MASTER_GAIN);
            volume1.setValue(20f * (float) Math.log10(newVol));
            if (extraSFX.isOpen()) {
                FloatControl volume2 = (FloatControl) extraSFX.getControl(FloatControl.Type.MASTER_GAIN);
                volume2.setValue(20f * (float) Math.log10(newVol));
            }
            else if (heartSFX.isOpen()) {
                FloatControl volume3 = (FloatControl) heartSFX.getControl(FloatControl.Type.MASTER_GAIN);
                volume3.setValue(20f * (float) Math.log10(newVol));
            }
        }
    }

    public boolean isBGMOff() {
        return BGMOff;
    }
    public boolean isHBGOff() {
        return HBGOff;
    }

    public boolean isSFXOff() {
        return SFXOff;
    }
}
