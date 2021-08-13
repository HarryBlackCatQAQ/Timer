package com.hhr.model.javaFx;

import com.hhr.util.ResourcesPathUtil;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * @Author: Harry
 * @Date: 2021/1/12 22:29
 * @Version 1.0
 */

public class MyStage extends Stage {

    private static MyStage instance;
    private Logger logger = LoggerFactory.getLogger(MyStage.class);

    /**
     * 单例模式
     * @return
     */
    public static MyStage getInstance() {
        if (instance == null) {
            synchronized (MyStage.class) {
                if (instance == null) {
                    instance = new MyStage();
                }
            }
        }
        return instance;
    }

    private MyStage(){
        //设置标题
        this.setTitle("计时器");
        //设置图标
        this.getIcons().add(new Image(ResourcesPathUtil.getPathOfString("/icon/icon.jpg")));
        //设置禁止缩放
        this.setResizable(false);
        //设置托盘
        MySystemTray.getInstance().listen(this);
        //初始化初始界面
        setScene("/fxml/main.fxml");
        //设置界面关闭弹出监听
        setOnCloseRequest();
    }

    /**
     * Change user interface
     * @param fxmlPath interface path
     */
    private void setScene(String fxmlPath){
        try {
            Parent root = FXMLLoader.load(ResourcesPathUtil.getPathOfUrl(fxmlPath));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ResourcesPathUtil.getPathOfUrl("/css/jfoenix-components.css").toExternalForm());
            this.setScene(scene);
        } catch (IOException e) {
            logger.info("fxml path is wrong.");
        }
    }

    /**
     * set OnCloseRequest
     */
    private void setOnCloseRequest(){
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //创建提示窗口
                DialogBuilder dialogBuilder = new DialogBuilder(instance.getScene().getWindow());
                //设置标题
                dialogBuilder.setTitle("提示");
                Label title = dialogBuilder.getTitle();
                title.getStyleClass().add("close-layout-title");
                //设置提示窗口内容区域间距
                VBox vBox = dialogBuilder.getLayoutContentVBox();
                vBox.setSpacing(25);
                //添加提示窗口的组件
                final ToggleGroup group = new ToggleGroup();
                JFXRadioButton closeRadioBtn = new JFXRadioButton("关闭");
                JFXRadioButton minimizeRadioBtn = new JFXRadioButton("最小化");
                //设置最小化按钮默认选中
                minimizeRadioBtn.setSelected(true);
                closeRadioBtn.setUserData("close");
                closeRadioBtn.setToggleGroup(group);
                minimizeRadioBtn.setToggleGroup(group);
                minimizeRadioBtn.setUserData("minimize");
                closeRadioBtn.getStyleClass().add("close-layout-radio");
                minimizeRadioBtn.getStyleClass().add("close-layout-radio");
                //将组件加入到提示窗口
                LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                map.put("radioGroup",group);
                map.put("closeRadioBtn",closeRadioBtn);
                map.put("minimizeRadioBtn",minimizeRadioBtn);
                dialogBuilder.setLayoutContentNodeMap(map);

                //设置提示窗口按钮和其监听事件
                dialogBuilder.setPositiveBtn("确定", new DialogBuilder.OnClickListener() {
                    @Override
                    public void onClick() {
                        String s = (String) group.getSelectedToggle().getUserData();
                        if(s.equals("close")){
                            System.exit(0);
                        }
                        else{
                            getInstance().hide();
                        }
                    }
                });

                //创建提示窗口
                dialogBuilder.create();
            }
        });
    }

}
