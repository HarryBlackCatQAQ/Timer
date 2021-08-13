package com.hhr.services;

import com.hhr.controller.MainController;
import com.hhr.services.thread.CountDownTimerThread;
import com.hhr.services.thread.CountTimerThread;

import java.util.Timer;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:05
 * @Version 1.0
 */

public class MainService{
    private MainController mainController;

    public MainService(MainController mainController) {
        this.mainController = mainController;
    }

    private Timer timer;
    private CountDownTimerThread countDownTimerThread;
    private CountTimerThread countTimerThread;

    public void startBtnOnMouseClicked(int hour,int minute,int second){
        if(mainController.getCountDownToggleBtn().isSelected()){
            if(!mainController.isStopFlag()){
                timer = new Timer(true);
                countDownTimerThread = new CountDownTimerThread(hour,minute,second,mainController,timer);
            }
            timer.schedule(countDownTimerThread,0,1000);
        }
        else{
            if(!mainController.isStopFlag()){
                timer = new Timer(true);
                countTimerThread = new CountTimerThread(hour,minute,second,mainController,timer);
            }
            timer.schedule(countTimerThread,0,1000);
        }
    }

    public void stopBtnOnMouseClicked(){
        mainController.setStopFlag(true);
        timer.cancel();
        timer = new Timer(true);
        if(mainController.getCountDownToggleBtn().isSelected()){
            countDownTimerThread.setDelete(true);
            countDownTimerThread = new CountDownTimerThread(countDownTimerThread.getHour(), countDownTimerThread.getMinute(), countDownTimerThread.getSecond(),
                    mainController,timer);
        }
        else{
            countTimerThread.setDelete(true);
            countTimerThread = new CountTimerThread(countTimerThread.hour, countTimerThread.minute, countTimerThread.second,
                    countTimerThread.getCountHour(),countTimerThread.getCountMinute(),countTimerThread.getCountSecond(),
                    mainController,timer);
        }

    }

    public void resetBtnOnMouseClicked(){
        timer.cancel();
        mainController.setStopFlag(false);
    }
}
