package com.hhr.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:02
 * @Version 1.0
 */

@Getter
@Setter
public abstract class MainView implements Initializable {
    @FXML
    protected Text countdown;
    @FXML
    protected JFXComboBox hours;
    @FXML
    protected JFXComboBox minutes;
    @FXML
    protected JFXComboBox seconds;
    @FXML
    protected JFXToggleButton countToggleBtn;
    @FXML
    protected JFXToggleButton countDownToggleBtn;
    @FXML
    protected Button startBtn;
    @FXML
    protected Button stopBtn;
    @FXML
    protected Button resetBtn;

    /**
     * 提示音播放器
     */
    protected MediaPlayer promptMusicMediaPlayer;
    /**
     * 结束音播放器
     */
    protected MediaPlayer endMusicMediaPlayer;
}
