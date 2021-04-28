package com.runtimeterror.sound;

import com.runtimeterror.main.Main;

import javax.print.DocFlavor;
import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class SoundManager {
    private Mixer mixer;
    private Clip clip;


    public SoundManager(){
        Mixer.Info[] mixInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixInfo){
            //System.out.println(info.getName() + "---" + info.getDescription());
            if ("Default Audio Device".equals(info.getName())){
                mixer = AudioSystem.getMixer(info);
            }
            if (mixer != null){
                DataLine.Info dataInfo = new DataLine.Info(Clip.class,null);
                try { clip = ((Clip)mixer.getLine(dataInfo));  }
                catch (LineUnavailableException e) {e.printStackTrace();}
            }
        }
     }

     public void playSound(String file){
        try {
            if (clip.isOpen()){
                clip.close();
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            clip.open(audioInputStream);
        }
        catch (LineUnavailableException e) {e.printStackTrace();}
        catch (UnsupportedAudioFileException e) {e.printStackTrace();}
        catch (Exception e) {e.printStackTrace();}
        clip.start();
     }

    public boolean isActive() {
        return clip.isActive();
    }
}
