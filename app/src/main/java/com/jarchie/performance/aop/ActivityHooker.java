package com.jarchie.performance.aop;

import android.os.Bundle;
import android.util.Log;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * 作者: 乔布奇
 * 日期: 2020-06-21 17:00
 * 邮箱: jarchie520@gmail.com
 * 描述: lancet使用
 */
public class ActivityHooker {

    public static ActivityLive mLive;

    static {
        mLive = new ActivityLive();
    }

    //@Insert：使用自己程序中自己的一些类需要添加，值这里就指定onCreate()方法，
    //可配置项mayCreateSuper是当目标函数不存在的时候可以通过它来创建目标方法
    //@TargetClass：框架知道要找的类是哪个，可配置项Scope.ALL:匹配value所指定的所有类的子类
    @Insert(value = "onCreate",mayCreateSuper = true)
    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity", scope = Scope.ALL)
    protected void onCreate(Bundle savedInstanceState) {
        mLive.mOnCreateTime = System.currentTimeMillis();
        Origin.callVoid(); //无返回值的调用
    }


    //注解含义同上面onCreate()
    @Insert(value = "onWindowFocusChanged",mayCreateSuper = true)
    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity", scope = Scope.ALL)
    public void onWindowFocusChanged(boolean hasFocus) {
        mLive.mOnWindowsFocusChangedTime = System.currentTimeMillis();
        Log.i("onWindowFocusChanged","---"+(mLive.mOnWindowsFocusChangedTime - mLive.mOnCreateTime));
        Origin.callVoid();
    }

}
