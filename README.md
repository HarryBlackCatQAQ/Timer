# Timer
使用JavaFX 开发的计时器 用来考试计时 训练计时使用

#演示
![](https://github.com/HarryBlackCatQAQ/Image/blob/main/timer_1.png)

![](https://github.com/HarryBlackCatQAQ/Image/blob/main/timer_2.png)

![](https://github.com/HarryBlackCatQAQ/Image/blob/main/timer_3.png)

#前期设置
用到了 [https://github.com/PlusHaze/TrayNotification](https://github.com/PlusHaze/TrayNotification)

的提醒jar包

# 方法 #
在mvn项目中加入本地jar包，才能启动，输入以下命令即可

    mvn install:install-file
    -Dfile=jar包目录
    -DgroupId=com.github.plushaze
	-DartifactId=traynotification
    -Dversion=1.0.0
    -Dpackaging=jar

jar包在resources的lib文件夹中

可以打包成exe和dmg 在windows和macOS中使用