package com.cc.rd.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cc.rd.DemoHelper;
import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.custom.LoginEditText;
import com.cc.rd.mvp.contract.login.RegisterContract;
import com.cc.rd.mvp.presenter.user.RegisterPresenter;
import com.cc.rd.util.ExceptionEngine;
import com.cc.rd.util.ParamUtils;
import com.cc.rd.util.ProgressDialog;
import com.cc.rd.util.Result;
import com.cc.rd.util.SharedPreferencesUtils;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseMvpActivity<RegisterPresenter> implements RegisterContract.View {

    private LoginEditText registerTel;
    private LoginEditText registerPwd;
    private LoginEditText registerCPwd;
    private LoginEditText codeTel;

    private ImageButton registerVPwd;
    private ImageButton registerCVPwd;

    private String telphone;
    private String pwd;

    @BindView(R.id.registerCode)
    Button codeBtn;

    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
    }

    private void init() {
        registerTel = (LoginEditText) findViewById(R.id.registerTel);
        registerPwd = (LoginEditText) findViewById(R.id.registerPwd);
        registerCPwd = (LoginEditText) findViewById(R.id.registerCPwd);
        codeTel = (LoginEditText) findViewById(R.id.codeTel);
        registerVPwd = (ImageButton) findViewById(R.id.registerVPwd);
        registerCVPwd = (ImageButton) findViewById(R.id.registerVCPwd);

        //密码默认初始为不可见
        registerVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
        registerCVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
        //密码是否可见
        registerVPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    if (flag == 0) {
                        flag = 1;
                        registerVPwd.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                        registerPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        registerCVPwd.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                        registerCPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        flag = 0;
                        registerVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                        registerPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        registerCVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                        registerCPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }

                }
                return false;
            }
        });
        registerCVPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    if (flag == 0) {
                        flag = 1;
                        registerVPwd.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                        registerPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        registerCVPwd.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                        registerCPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        flag = 0;
                        registerVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                        registerPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        registerCVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                        registerCPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }

                }
                return false;
            }
        });

    }

    /**
     * 发送短信注册验证码
     */
    @OnClick(R.id.registerCode)
    public void sendCode() {
        if (TextUtils.isEmpty(registerTel.getText())){
            //设置晃动
            registerTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (!ParamUtils.isRightTel(registerTel.getText().toString())) {
            //设置晃动
            registerTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, "无效手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.sendNewCode(registerTel.getText().toString());

        new CountDownTimer(60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!RegisterActivity.this.isFinishing()) {
                    codeBtn.setBackgroundColor(Color.parseColor("#B6B6D8"));
                    codeBtn.setClickable(false);
                    codeBtn.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
                }

            }

            @Override
            public void onFinish() {
                if (!RegisterActivity.this.isFinishing()) {
                    codeBtn.setText(R.string.send_telcode);
                    codeBtn.setClickable(true);
                    codeBtn.setBackgroundColor(Color.parseColor("#5f9cf1"));
                }

            }
        }.start();

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    /**
     * 注册用户
     */
    @OnClick(R.id.registerUser)
    public void registerUser() {
        telphone = registerTel.getText().toString();
        if (TextUtils.isEmpty(registerTel.getText())){
            //设置晃动
            registerTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        pwd = registerPwd.getText().toString();
        String cpwd = registerCPwd.getText().toString();
        if ("".equals(pwd)){
            registerPwd.setShakeAnimation();
            Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(cpwd)){
            registerCPwd.setShakeAnimation();
            Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (!pwd.equals(cpwd)) {
            registerPwd.setShakeAnimation();
            registerCPwd.setShakeAnimation();
            Toast.makeText(this, R.string.passwrod_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(codeTel.getText())) {
            codeTel.setShakeAnimation();
            Toast.makeText(this, R.string.telcode_not_null, Toast.LENGTH_LONG).show();
            return;
        }

        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setConfirmPassword(cpwd);
        userAddRequest.setPassword(pwd);
        userAddRequest.setTelphone(telphone);
        userAddRequest.setCode(codeTel.getText().toString());
        userAddRequest.setAuthenticationSource(0);
        mPresenter.registerUser(userAddRequest);

    }

    /**
     * 注册成功
     * @param result
     */
    @Override
    public void onSuccess(Result result) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(telphone, pwd);
                    DemoHelper.getInstance().setCurrentUserName(telphone);
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();

        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("telphone", telphone);
        i.putExtra("password", pwd);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void showLoading() {
        ProgressDialog.getInstance().show(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance().dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, ExceptionEngine.handleException(throwable).message, Toast.LENGTH_LONG).show();
    }
}
