package com.runtimeterror.model;

import java.util.Timer;
import java.util.TimerTask;

class TimerTest {

    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            int counter = 10;
            PostGameProcessor postGameProcessor = new PostGameProcessor();
            @Override
            public void run() {
                if(counter > 0) {
                    System.out.println(counter + " seconds");
                    counter--;
                } else {
                    System.out.println("timer stopped");
                    timer.cancel();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }


}