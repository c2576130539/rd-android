package com.cc.rd;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

/**
 * @program: MyApplication
 * @description: 全局
 * @author: cchen
 * @create: 2019-05-07 11:08
 */

public class MyApplication extends Application {

    private static Context context;

    private static MyApplication instance;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        instance = this;

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        DemoHelper.getInstance().init(getAppContext());

        EaseUI.getInstance().init(MyApplication.context, options);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
