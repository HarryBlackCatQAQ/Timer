package com.hhr;

import com.hhr.model.javaFx.MyStage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @Author: Harry
 * @Date: 2021/1/9 18:53
 * @Version 1.0
 */


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.close();
        //使用自己的Stage
        primaryStage = MyStage.getInstance();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

