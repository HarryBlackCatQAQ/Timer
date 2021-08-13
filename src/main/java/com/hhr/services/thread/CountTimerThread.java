package com.hhr.services.thread;

import com.hhr.controller.MainController;
import com.hhr.model.javaFx.MyTrayNotification;
import com.hhr.util.DateUtil;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:46
 * @Version 1.0
 */
@Getter
@Setter
public class CountTimerThread extends TimerThread{
    private int countHour;
    private int countMinute;
    private int countSecond;

    private MainController mainController;
    private Timer timer;

    private boolean isDelete;

    public CountTimerThread(int hour, int minute, int second, MainController mainController, Timer timer){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.countHour = this.countMinute = this.countSecond = 0;
        this.mainController = mainController;
        this.timer = timer;
        this.isDelete = false;
    }

    public CountTimerThread(int hour, int minute, int second,int countHour,int countMinute,int countSecond
    ,MainController mainController,Timer timer){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.countHour = countHour;
        this.countMinute = countMinute;
        this.countSecond = countSecond;
        this.mainController = mainController;
        this.timer = timer;
        this.isDelete = false;
    }


    @Override
    public void run() {
        if(this.isDelete){
            return;
        }

        countSecond++;

        if(countSecond == 60){
            countMinute++;
            countSecond = 0;
        }

        if(countMinute == 60){
            countHour++;
            countMinute = 0;
        }

        MyTrayNotification myTrayNotification = null;

        if(lastReminderJudge(0,30,0)){
            playMediaPlayerLastBySeconds(mainController.getPromptMusicMediaPlayer(),5);
//            playTrayNotification("提醒","还剩下最后30分钟!");
            myTrayNotification = new MyTrayNotification("提醒","还剩下最后30分钟!");
            myTrayNotification.playTrayNotification();
        }

        if(lastReminderJudge(0,5,0)){
            playMediaPlayerLastBySeconds(mainController.getPromptMusicMediaPlayer(),5);
            myTrayNotification = new MyTrayNotification("提醒","还剩下最后5分钟!");
            myTrayNotification.playTrayNotification();
        }

        if(countHour == hour && countMinute == minute && countSecond == second){
            mainController.setComponentDisable(false);
//            countTimerThread = null;
            mainController.setStopFlag(false);
            playMediaPlayerLastBySeconds(mainController.getEndMusicMediaPlayer(),5);
            timer.cancel();
//            playTrayNotification("提醒","时间到了!");
            myTrayNotification = new MyTrayNotification("提醒","时间到了!");
            myTrayNotification.playTrayNotification();
        }

        mainController.setCountdownText(countHour,countMinute,countSecond);
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

    private boolean lastReminderJudge(int hour,int minute,int second){
        DateUtil.Time sub = getSubTime();

        return sub.getHour() == hour && sub.getMinute() == minute && sub.getSecond() == second;
    }

    private DateUtil.Time getSubTime(){
        Date now = new Date();
        DateUtil.Time t1 = new DateUtil.Time(hour,minute,second);
        DateUtil.Time t2 = new DateUtil.Time(countHour,countMinute,countSecond);

        Date d1 = DateUtil.addTime(now,t1);
        Date d2 = DateUtil.addTime(now,t2);

        DateUtil.Time sub = DateUtil.sub(d1,d2);

        return sub;
    }

}
