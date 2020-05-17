package com.jarchie.performance.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.telephony.TelephonyManager;

import androidx.core.os.TraceCompat;

import com.jarchie.performance.utils.LaunchTime;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者: 乔布奇
 * 日期: 2020-05-17 10:19
 * 邮箱: jarchie520@gmail.com
 * 描述: 应用Application
 */
public class BaseApp extends Application {

    private static Application mApplication;
    private String mDeviceId;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LaunchTime.startRecord();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
//        Debug.startMethodTracing("PerOpt");
        TraceCompat.beginSection("AppOnCreate");
        mApplication = this;
        TelephonyManager tManager = (TelephonyManager) BaseApp.this.getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceId = tManager.getDeviceId();
        //推送
        JPushInterface.init(this);
        JPushInterface.setAlias(this,0,mDeviceId);
//        Debug.stopMethodTracing();
        TraceCompat.endSection();
    }

    public static Application getApplication(){
        return mApplication;
    }

}
