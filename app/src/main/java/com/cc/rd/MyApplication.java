package com.cc.rd;

import android.app.Application;
import android.content.Context;

/**
 * @program: MyApplication
 * @description: 全局
 * @author: cchen
 * @create: 2019-05-07 11:08
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
