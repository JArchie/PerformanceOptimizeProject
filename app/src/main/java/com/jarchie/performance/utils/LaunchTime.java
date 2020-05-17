package com.jarchie.performance.utils;

import android.util.Log;

/**
 * 作者: 乔布奇
 * 日期: 2020-03-30 22:36
 * 邮箱: jarchie520@gmail.com
 * 描述: 打点计算启动时间
 */
public class LaunchTime {

    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord(String msg) {
        long cost = System.currentTimeMillis() - sTime;
        Log.i(msg, "--->cost" + cost);
    }

}
