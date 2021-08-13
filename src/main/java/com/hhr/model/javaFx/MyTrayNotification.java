package com.hhr.model.javaFx;

import com.hhr.util.ResourcesPathUtil;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:56
 * @Version 1.0
 */
public class MyTrayNotification {
    private String title;
    private String message;

    public MyTrayNotification(String title, String message){
        this.title = title;
        this.message = message;
    }

    /**
     * play Tray Notification
     */
    public void playTrayNotification(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationType notification = NotificationType.INFORMATION;
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification(title, message, notification);
                tray.setAnimationType(AnimationType.POPUP);
                tray.setImage(new Image(ResourcesPathUtil.getPathOfString("/icon/icon.jpg")));
                tray.showAndDismiss(Duration.seconds(7.5));
            }
        });
    }
}
