package com.lc.utils;

import com.baidu.mapapi.SDKInitializer;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        JPushInterface.setLatestNotificationNumber(this, 1); //设置推送显示的条数
        SDKInitializer.initialize(this);  
    }
}