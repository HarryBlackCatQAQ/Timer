package com.hhr.services.thread;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:13
 * @Version 1.0
 */

import com.hhr.controller.MainController;
import com.hhr.model.javaFx.MyTrayNotification;
import com.hhr.util.ResourcesPathUtil;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.util.Timer;
import java.util.TimerTask;

/**
 * CountDownTimer Thread Class
 */
@Getter
@Setter
public class CountDownTimerThread extends TimerThread{
    private MainController mainController;
    private Timer timer;
    private boolean isDelete;

    public CountDownTimerThread(int hour, int minute, int second, MainController mainController,Timer timer){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.mainController = mainController;
        this.timer = timer;
        this.isDelete = false;
    }

    @Override
    public void run() {
        if(this.isDelete){
            return;
        }

        second--;
        if(second < 0){
            second = 59;
            minute--;
        }

        if(minute < 0){
            minute = 59;
            hour--;
        }

        //System.err.println(hour + " " + minute + " " + second);

        if(hour == 0 && minute == 30 && second == 0){
            playMediaPlayerLastBySeconds(mainController.getPromptMusicMediaPlayer(),5);
//            playTrayNotification("提醒","还剩下最后30分钟!");
            MyTrayNotification myTrayNotification = new MyTrayNotification("提醒","还剩下最后30分钟!");
            myTrayNotification.playTrayNotification();
        }

        if(hour == 0 && minute == 5 && second == 0){
            playMediaPlayerLastBySeconds(mainController.getPromptMusicMediaPlayer(),5);
            MyTrayNotification myTrayNotification = new MyTrayNotification("提醒","还剩下最后5分钟!");
            myTrayNotification.playTrayNotification();
        }

        //Time to end the timer
        if((hour <= 0 && minute <= 0 && second <= 0) || (hour < 0 || minute < 0 || second < 0)){
            mainController.setComponentDisable(false);
//            countDownTimerThread = null;
            mainController.setStopFlag(false);
            playMediaPlayerLastBySeconds(mainController.getEndMusicMediaPlayer(),5);
            timer.cancel();
//            playTrayNotification("提醒","时间到了!");
            MyTrayNotification myTrayNotification = new MyTrayNotification("提醒","时间到了!");
            myTrayNotification.playTrayNotification();
        }
        //Determines hour , minute, second if minus
        if(!(hour < 0 || minute < 0 || second < 0)){
            mainController.setCountdownText(hour, minute, second);
        }

    }

    /**
     * Let MediaPlayer play for * seconds
     * @param mediaPlayer
     * @param seconds
     */
    private void playMediaPlayerLastBySeconds(final MediaPlayer mediaPlayer, int seconds){
        Timer timer = new Timer(true);
        mediaPlayer.play();

        if(seconds > 0){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                }
            },1000 * seconds);
        }
    }


}
