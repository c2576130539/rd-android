package com.cc.rd.ui;

import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.enums.Constant;
import com.cc.rd.enums.GenderEnum;
import com.cc.rd.login.MainActivity;
import com.cc.rd.ui.order.CreateOrderActivity;
import com.cc.rd.util.ErrorCodeEnum;
import com.cc.rd.util.ParamUtils;
import com.cc.rd.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.cc.rd.MyApplication;
import com.cc.rd.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingsFragment extends Fragment{

    private String telphone;
    private String nickname;
    private String gender;
    private String userImage;

    private TextView money;
    private TextView credit;
    private TextView userNickname;
    private TextView userTel;
    private ImageView hBack;
    private CircleImageView hHead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_detail, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        money = (TextView) getView().findViewById(R.id.user_money);
        credit = (TextView) getView().findViewById(R.id.user_credit);
        userNickname = (TextView) getView().findViewById(R.id.user_nickname);
        userTel = (TextView) getView().findViewById(R.id.user_tel);
        hBack = (ImageView) getView().findViewById(R.id.h_back);
        hHead = (CircleImageView) getView().findViewById(R.id.h_head);

        super.onActivityCreated(savedInstanceState);
        new Thread(new Runnable() {
            public void run() {
                getLoginUser();
            }
        }).start();

        dataInit(null);

        Button logoutButton = (Button) getView().findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {
                        
                    }
                    
                    @Override
                    public void onError(int code, String error) {
                        
                    }
                });
            }
        });

        LinearLayout updateUser = (LinearLayout) getView().findViewById(R.id.update_user_linear);
        LinearLayout setting = (LinearLayout) getView().findViewById(R.id.setting_linear);

        updateUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "修改用户信息", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), UpdateUserActivity.class);
                startActivity(i);
            }
        });

        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateOrderActivity.class));
            }
        });

    }

    public void dataInit(UserLoginVo userLoginVo) {

        userImage = SharedPreferencesUtils.getUserImage();
        telphone = SharedPreferencesUtils.getTelphone();
        nickname = SharedPreferencesUtils.getNickName();
        gender = SharedPreferencesUtils.getGender();

        setImageView(userImage);

        if (userLoginVo != null) {
            money.setText(userLoginVo.getMoney());
        }
        if (null != userLoginVo) {
            credit.setText(userLoginVo.getCredit());

            if (GenderEnum.MAN.geteDesc().equals(userLoginVo.getGender())) {
                userNickname.setText(userLoginVo.getNickName() + " ♂");
            } else {
                userNickname.setText(userLoginVo.getNickName() + " ♀");
            }
            userTel.setText(userLoginVo.getTelphone());
        }else {
            if (GenderEnum.MAN.geteDesc().equals(gender)) {
                userNickname.setText(nickname + " ♂");
            } else {
                userNickname.setText(nickname + " ♀");
            }
            userTel.setText(telphone);
        }
    }

    private void getLoginUser() {
        Request request = new Request.Builder()
                .url(Constant.url + "/users")
                .get()
                .addHeader("Authorization", SharedPreferencesUtils.getToken())
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                Log.i("result", htmlStr);
                try {
                    JSONObject object = new JSONObject(htmlStr);
                    int code = object.getInt("code");
                    if (code == 200) {
                        //获取
                        Gson gson = new Gson();
                        UserLoginVo userLoginVo = gson.fromJson(object.getString("result"), UserLoginVo.class);
                        //设值
                        dataInit(userLoginVo);
                        SharedPreferencesUtils.saveUserData(userLoginVo);
                    } else if (code == ErrorCodeEnum.NOT_AUTH.getCode()) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setImageView(String userImage) {
        new Thread(new Runnable() {
            public void run() {
                setUserImageView(userImage);
            }
        }).start();
    }

    private void setUserImageView(String userImage) {

        try {
            URL thumb_u = new URL(Constant.url + "/files/" + userImage);
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            hHead.setImageDrawable(thumb_d);
        }
        catch (Exception e) {
            // handle it
        }
    }

}
