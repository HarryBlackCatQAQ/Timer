package com.hhr.util;

import java.util.Date;

/**
 * @Author: Harry
 * @Date: 2021/1/13 1:05
 * @Version 1.0
 */
public class DateUtil {

    public static Time sub(Date a, Date b){
        long ms = a.getTime() - b.getTime();

        long s = 1000;
        long mi = s * 60;
        long h = mi * 60;

        long hour = ms / h;
        long minute =  (ms - hour * h) / mi;
        long second =  (ms - hour * h - mi * minute) / s;

        //System.err.println(ms + " " + hour + " " + minute + " " + second);

        return new Time(hour,minute,second);
    }

    public static Date addTime(Date date,Time time){
        return new Date(date.getTime() + time.toLong());
    }

    public static class Time{
        private long hour;
        private long minute;
        private long second;

        public Time(){
            hour = minute = second = 0;
        }

        public Time(long hour, long minute, long second) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        private long toLong(){
            return hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        }

        public long getHour() {
            return hour;
        }

        public long getMinute() {
            return minute;
        }

        public long getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "Time{" +
                    "hour=" + hour +
                    ", minute=" + minute +
                    ", second=" + second +
                    '}';
        }
    }
}
