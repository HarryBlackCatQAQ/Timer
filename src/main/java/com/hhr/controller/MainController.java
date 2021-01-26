package com.hhr.controller;

import com.hhr.util.DateUtil;
import com.hhr.util.ResourcesPathUtil;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: Harry
 * @Date: 2021/1/9 18:53
 * @Version 1.0
 */
public class MainController implements Initializable {

    @FXML
    private Button resetBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button startBtn;

    @FXML
    private Text countdown;

    @FXML
    private JFXComboBox hours;

    @FXML
    private JFXComboBox minutes;

    @FXML
    private JFXComboBox seconds;

    private ChangeListener comboBoxChangeListener;

    @FXML
    private JFXToggleButton countDownToggleBtn;

    @FXML
    private JFXToggleButton countToggleBtn;

    private Timer timer;
    private CountDownTimerThread countDownTimerThread;
    private CountTimerThread countTimerThread;

    /**
     * 提示音播放器
     */
    private MediaPlayer promptMusicMediaPlayer;
    /**
     * 结束音播放器
     */
    private MediaPlayer endMusicMediaPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化stop bottom 为不可用
        stopBtn.setDisable(true);
        //初始化reset bottom 为不可用
        resetBtn.setDisable(true);
        //初始化countDownToggle bottom为被选中
        countDownToggleBtn.setSelected(true);
        //初始化comboBox
        initComboBox();
        //初始化comboBox Listener
        initComboBoxListener();
        //初始化 Toggle bottom and CountToggle bottom 组合的 Listener
        initCountDownToggleBtnAndCountToggleBtnListener();
        //初始化音效
        initMediaPlayer();

    }

    @FXML
    void startBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("startBtnOnMouseClicked");
        int hour = Integer.valueOf(getComboBoxText(hours));
        int minute = Integer.valueOf(getComboBoxText(minutes));
        int second = Integer.valueOf(getComboBoxText(seconds));

        if(hour == 0 && minute == 0 && second == 0){
            return;
        }

        setComponentDisable(true);

        timer = new Timer(true);

        if(countDownToggleBtn.isSelected()){
            if(countDownTimerThread == null){
                countDownTimerThread = new CountDownTimerThread(hour,minute,second);
            }
            timer.schedule(countDownTimerThread,0,1000);
        }
        else{
            if(countTimerThread == null){
                countTimerThread = new CountTimerThread(hour,minute,second);
            }
            timer.schedule(countTimerThread,0,1000);
        }
    }

    @FXML
    void stopBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("stopBtnOnMouseClicked");

        if(countDownToggleBtn.isSelected()){
            countDownTimerThread = new CountDownTimerThread(countDownTimerThread.hour, countDownTimerThread.minute, countDownTimerThread.second);
        }
        else{
            countTimerThread = new CountTimerThread(countTimerThread.hour, countTimerThread.minute, countTimerThread.second,
                    countTimerThread.countHour,countTimerThread.countMinute,countTimerThread.countSecond);
        }
        timer.cancel();
        startBtn.setDisable(false);
        stopBtn.setDisable(true);
    }

    @FXML
    void resetBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("resetBtnOnMouseClicked");

        if(countDownToggleBtn.isSelected()){
            setCountdownText();
        }
        else{
            setCountdownText(0,0,0);
        }
        timer.cancel();
        countDownTimerThread = null;
        countTimerThread = null;
        setComponentDisable(false);
        promptMusicMediaPlayer.stop();
        endMusicMediaPlayer.stop();
    }

    @FXML
    void countToggleBtnOn(ActionEvent event) {
        //System.err.println("countToggleBtnOn");

        countDownToggleBtn.setSelected(!countDownToggleBtn.isSelected());
    }

    @FXML
    void countDownToggleBtnOn(ActionEvent event) {
        //System.err.println("countDownToggleBtnOn");

        countToggleBtn.setSelected(!countToggleBtn.isSelected());
    }

    /**
     *  Initialize ComboBox data
     */
    private void initComboBox(){
       for(int i = 0;i <= 24;i++){
           hours.getItems().add(zeroize(i));
       }
       for(int i = 0;i < 60;i++){
           minutes.getItems().add(zeroize(i));
           seconds.getItems().add(zeroize(i));
       }
    }

    /**
     * zeroize number
     * @param i number
     * @return zeroize result
     */
    private String zeroize(int i){
        if(i < 10){
            return 0 + String.valueOf(i);
        }

        return String.valueOf(i);
    }

    /**
     * Initialize ComboBox Listener
     */
    private void initComboBoxListener(){
        comboBoxChangeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setCountdownText();
            }
        };
        setComboBoxListener();
    }

    private void initCountDownToggleBtnAndCountToggleBtnListener(){
        countDownToggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                setComboBoxListener();
            }
        });
    }

    /**
     * Initialize MediaPlayer
     */
    private void initMediaPlayer(){
        promptMusicMediaPlayer = new MediaPlayer(new Media(ResourcesPathUtil.getPathOfString("/music/promptMusic.mp3")));
        endMusicMediaPlayer  = new MediaPlayer(new Media(ResourcesPathUtil.getPathOfString("/music/endMusic.mp3")));
    }

    /**
     * set ComboBox Listener
     */
    private void setComboBoxListener(){
        //countDownToggle bottom 被选中时添加监听器
        if(countDownToggleBtn.isSelected()){
            hours.getSelectionModel().selectedItemProperty().addListener(comboBoxChangeListener);
            minutes.getSelectionModel().selectedItemProperty().addListener(comboBoxChangeListener);
            seconds.getSelectionModel().selectedItemProperty().addListener(comboBoxChangeListener);
            setCountdownText();
        }
        else{
            hours.getSelectionModel().selectedItemProperty().removeListener(comboBoxChangeListener);
            minutes.getSelectionModel().selectedItemProperty().removeListener(comboBoxChangeListener);
            seconds.getSelectionModel().selectedItemProperty().removeListener(comboBoxChangeListener);
            setCountdownText(0,0,0);
        }
    }

    /**
     *  set Countdown Text
     */
    private void setCountdownText(){
        String hour,minute,second;
        hour = getComboBoxText(hours);
        minute = getComboBoxText(minutes);
        second = getComboBoxText(seconds);
        countdown.setText(hour + ":" + minute + ":" + second);
    }

    /**
     * set Countdown Text
     * @param hour
     * @param minute
     * @param second
     */
    private void setCountdownText(int hour,int minute,int second){
        countdown.setText(zeroize(hour) + ":" + zeroize(minute) + ":" + zeroize(second));
    }

    /**
     * Determines if the comboBox Text is null and get ComboBox Text
     * @param comboBox
     * @return
     */
    private String getComboBoxText(JFXComboBox comboBox){
        String str;
        try {
           str =  comboBox.getSelectionModel().getSelectedItem().toString();
        }catch (java.lang.NullPointerException e){
            return "00";
        }

        return str == null ? "00" : str.replaceAll(" ","");
    }

    private void setComponentDisable(boolean bool){
        startBtn.setDisable(bool);
        stopBtn.setDisable(!bool);
        resetBtn.setDisable(!bool);
        hours.setDisable(bool);
        minutes.setDisable(bool);
        seconds.setDisable(bool);

        countDownToggleBtn.setDisable(bool);

        countToggleBtn.setDisable(bool);
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
    private void playMediaPlayerLastBySeconds(final MediaPlayer mediaPlayer){
        playMediaPlayerLastBySeconds(mediaPlayer,0);
    }

    /**
     * play Tray Notification
     * @param title 标题
     * @param message 内容
     */
    private void playTrayNotification(final String title, final String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationType notification = NotificationType.INFORMATION;
                TrayNotification tray = new TrayNotification(title, message, notification);
                tray.setAnimationType(AnimationType.POPUP);
                tray.setImage(new Image(ResourcesPathUtil.getPathOfString("/icon/icon.jpg")));
                tray.showAndDismiss(Duration.seconds(7.5));
            }
        });
    }

    private class TimerThread extends TimerTask{
        int hour;
        int minute;
        int second;

        @Override
        public void run() {

        }
    }

    /**
     * CountDownTimer Thread Class
     */
    private class CountDownTimerThread extends TimerThread{

        CountDownTimerThread(int hour, int minute, int second){
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        @Override
        public void run() {
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
                playMediaPlayerLastBySeconds(promptMusicMediaPlayer,5);
                playTrayNotification("提醒","还剩下最后30分钟!");
            }

            //Time to end the timer
            if((hour <= 0 && minute <= 0 && second <= 0) || (hour < 0 || minute < 0 || second < 0)){
                setComponentDisable(false);
                countDownTimerThread = null;
                playMediaPlayerLastBySeconds(endMusicMediaPlayer,5);
                timer.cancel();
                playTrayNotification("提醒","时间到了!");
            }
            //Determines hour , minute, second if minus
            if(!(hour < 0 || minute < 0 || second < 0)){
                setCountdownText(hour, minute, second);
            }

        }

    }

    /**
     * CountTimer Thread Class
     */
    private class CountTimerThread extends TimerThread{
        private int countHour;
        private int countMinute;
        private int countSecond;

        CountTimerThread(int hour, int minute, int second){
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.countHour = this.countMinute = this.countSecond = 0;
        }

        CountTimerThread(int hour, int minute, int second,int countHour,int countMinute,int countSecond){
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.countHour = countHour;
            this.countMinute = countMinute;
            this.countSecond = countSecond;
        }


        @Override
        public void run() {
            countSecond++;

            if(countSecond == 60){
                countMinute++;
                countSecond = 0;
            }

            if(countMinute == 60){
                countHour++;
                countMinute = 0;
            }

            if(last30MinutesReminderJudge()){
                playMediaPlayerLastBySeconds(promptMusicMediaPlayer,5);
                playTrayNotification("提醒","还剩下最后30分钟!");
            }

            if(countHour == hour && countMinute == minute && countSecond == second){
                setComponentDisable(false);
                countTimerThread = null;
                playMediaPlayerLastBySeconds(endMusicMediaPlayer,5);
                timer.cancel();
                playTrayNotification("提醒","时间到了!");
            }

            setCountdownText(countHour,countMinute,countSecond);
        }

        private boolean last30MinutesReminderJudge(){
            //小于60分钟不开启提醒
            if(minute < 60){
                return false;
            }

            Date now = new Date();
            DateUtil.Time t1 = new DateUtil.Time(hour,minute,second);
            DateUtil.Time t2 = new DateUtil.Time(countHour,countMinute,countSecond);

            Date d1 = DateUtil.addTime(now,t1);
            Date d2 = DateUtil.addTime(now,t2);

            DateUtil.Time sub = DateUtil.sub(d1,d2);

            return sub.getHour() == 0 && sub.getMinute() == 30 && sub.getSecond() == 0;
        }
    }
}

