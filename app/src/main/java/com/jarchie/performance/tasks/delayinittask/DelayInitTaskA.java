package com.jarchie.performance.tasks.delayinittask;


import com.jarchie.performance.launchstarter.task.MainTask;
import com.jarchie.performance.utils.LogUtils;

public class DelayInitTaskA extends MainTask {

    @Override
    public void run() {
        // 模拟一些操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogUtils.i("DelayInitTaskA finished");
    }
}
