package com.hhr.controller;

import com.hhr.services.MainService;
import com.hhr.util.ResourcesPathUtil;
import com.hhr.view.MainView;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: Harry
 * @Date: 2021/1/9 18:53
 * @Version 1.0
 */
@Getter
@Setter
public class MainController extends MainView {
    private MainService mainService = new MainService(this);

    private ChangeListener comboBoxChangeListener;

    private boolean stopFlag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
        initService();
    }

    private void initView() {
        //初始化stop bottom 为不可用
        stopBtn.setDisable(true);
        //初始化reset bottom 为不可用
        resetBtn.setDisable(true);
        //初始化countDownToggle bottom为被选中
        countDownToggleBtn.setSelected(true);
        //初始化comboBox
        initComboBox();
    }

    private void initEvent() {
        //初始化comboBox Listener
        initComboBoxListener();
        //初始化 Toggle bottom and CountToggle bottom 组合的 Listener
        initCountDownToggleBtnAndCountToggleBtnListener();
        //初始化音效
        initMediaPlayer();
    }

    private void initService() {
        stopFlag = false;
    }

    @FXML
    private void startBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("startBtnOnMouseClicked");
        int hour = Integer.valueOf(getComboBoxText(hours));
        int minute = Integer.valueOf(getComboBoxText(minutes));
        int second = Integer.valueOf(getComboBoxText(seconds));

        if(hour == 0 && minute == 0 && second == 0){
            return;
        }

        setComponentDisable(true);

        mainService.startBtnOnMouseClicked(hour,minute,second);
    }

    @FXML
    private void stopBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("stopBtnOnMouseClicked");

        mainService.stopBtnOnMouseClicked();

        startBtn.setDisable(false);
        stopBtn.setDisable(true);
    }

    @FXML
    private void resetBtnOnMouseClicked(MouseEvent event) {
//        System.err.println("resetBtnOnMouseClicked");

        if(countDownToggleBtn.isSelected()){
            setCountdownText();
        }
        else{
            setCountdownText(0,0,0);
        }

        mainService.resetBtnOnMouseClicked();

        setComponentDisable(false);
        promptMusicMediaPlayer.stop();
        endMusicMediaPlayer.stop();
    }


    @FXML
    private void countToggleBtnOn(ActionEvent event){
        countDownToggleBtn.setSelected(!countDownToggleBtn.isSelected());
    }

    @FXML
    private void countDownToggleBtnOn(ActionEvent event){
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
    public void setCountdownText(int hour,int minute,int second){
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

    public void setComponentDisable(boolean bool){
        startBtn.setDisable(bool);
        stopBtn.setDisable(!bool);
        resetBtn.setDisable(!bool);
        hours.setDisable(bool);
        minutes.setDisable(bool);
        seconds.setDisable(bool);

        countDownToggleBtn.setDisable(bool);

        countToggleBtn.setDisable(bool);
    }
}

