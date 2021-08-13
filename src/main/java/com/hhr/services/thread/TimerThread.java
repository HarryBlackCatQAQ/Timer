package com.hhr.services.thread;

import lombok.Getter;
import lombok.Setter;

import java.util.TimerTask;

/**
 * @Author: Harry
 * @Date: 2021/8/6 19:12
 * @Version 1.0
 */
@Getter
@Setter
public class TimerThread extends TimerTask {
    public int hour;
    public int minute;
    public int second;

    @Override
    public void run() {

    }
}
