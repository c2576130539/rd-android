package com.cc.rd.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cc.rd.MyApplication;
import com.cc.rd.bean.vo.UserLoginVo;

/**
 * @program: SharedPreferencesUtils
 * @description: 本地数据存储
 * @author: cchen
 * @create: 2019-05-06 18:52
 */

public class SharedPreferencesUtils {

    private static Context context = MyApplication.getAppContext();
    /**
     * 获取用户token
     * @return
     */
    public static String getToken() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        return token;
    }

    /**
     * 获取用户电话号码
     * @return
     */
    public static String getTelphone() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String telphone = sp.getString("telphone", null);
        return telphone;
    }

    /**
     * 获取用户密码
     * @return
     */
    public static String getPassword() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String password = sp.getString("password", null);
        return password;
    }

    public static void saveUserData(UserLoginVo userLoginVo) {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("token", userLoginVo.getToken());
        edit.putString("nickName", userLoginVo.getNickName());
        edit.putString("telphone", userLoginVo.getTelphone());
        edit.putString("gender", userLoginVo.getGender());
        edit.putString("userImage", userLoginVo.getUserImage());
        edit.putInt("credit", userLoginVo.getCredit());
        edit.putLong("money", userLoginVo.getMoney());
        edit.apply();
    }

    public static void saveUserPwd(String pwd) {
        //单独保存密码
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("password", pwd);
        edit.apply();
    }

}
