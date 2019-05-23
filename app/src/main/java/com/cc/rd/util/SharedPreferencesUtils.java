package com.cc.rd.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cc.rd.MyApplication;
import com.cc.rd.bean.request.user.UserUpdateRequest;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.enums.GenderEnum;

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

    /**
     * 获取用户昵称
     * @return
     */
    public static String getNickName() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String nickName = sp.getString("nickName", null);
        return nickName;
    }

    /**
     * 获取用户头像
     * @return
     */
    public static String getUserImage() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String userImage = sp.getString("user_image", null);
        return userImage;
    }

    /**
     * 获取用户性别
     * @return
     */
    public static String getGender() {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String gender = sp.getString("gender", null);
        return gender;
    }

    public static void saveUserData(UserLoginVo userLoginVo) {
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (ParamUtils.isNotNull(userLoginVo.getToken())) {
            edit.putString("token", userLoginVo.getToken());
        }
        edit.putString("nickname", userLoginVo.getNickName());
        edit.putString("telphone", userLoginVo.getTelphone());
        edit.putString("gender", userLoginVo.getGender());
        edit.putString("user_image", userLoginVo.getUserImage());
        edit.apply();
    }

    public static void saveUserPwd(String pwd) {
        //单独保存密码
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("password", pwd);
        edit.apply();
    }

    public static void saveUpdateUser(UserUpdateRequest request) {
        //单独保存密码
        SharedPreferences sp = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("nickname", request.getNickName());
        edit.putString("gender", GenderEnum.findByCode(request.getGender()) == null ? GenderEnum.GIRL.geteDesc(): GenderEnum.findByCode(request.getGender()).geteDesc());
        edit.putString("user_image", request.getUserImage());
        edit.apply();
    }

}
