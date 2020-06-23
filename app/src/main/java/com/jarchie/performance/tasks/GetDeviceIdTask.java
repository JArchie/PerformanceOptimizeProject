package com.jarchie.performance.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.jarchie.performance.app.BaseApp;
import com.jarchie.performance.launchstarter.task.Task;


public class GetDeviceIdTask extends Task {
    private String mDeviceId;

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        // 真正自己的代码
        TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(
                Context.TELEPHONY_SERVICE);
        mDeviceId = tManager.getDeviceId();
        BaseApp app = (BaseApp) mContext;
        app.setDeviceId(mDeviceId);
    }
}
