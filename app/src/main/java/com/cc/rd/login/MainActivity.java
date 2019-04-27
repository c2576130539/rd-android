package com.cc.rd.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.vo.CaptchaVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.bean.request.user.LoginRequest;
import com.cc.rd.mvp.contract.login.MainContract;
import com.cc.rd.custom.LoginEditText;
import com.cc.rd.mvp.presenter.user.MainPresenter;
import com.cc.rd.util.CatpchaUtils;
import com.cc.rd.util.ErrorCodeEnum;
import com.cc.rd.util.ExceptionEngine;
import com.cc.rd.util.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.telphone)
    LoginEditText inputTelphone;
    @BindView(R.id.password)
    LoginEditText inputPassword;
    //密码是否可见
    @BindView(R.id.visablePwd)
    ImageButton imageButton;
    @BindView(R.id.forgetPwd)
    TextView forgetPwd;
    @BindView(R.id.register)
    TextView register;
    //图片验证码
    @BindView(R.id.codeView)
    LinearLayout codeView;
    @BindView(R.id.code)
    LoginEditText code;
    @BindView(R.id.codeImage)
    ImageView codeImage;

    private String captchaCode;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);

    }

    private void init() {

        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
        //密码是否可见
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    return false;
                }else {
                    //再修改为抬起时的正常图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    inputPassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    return false;
                }
            }
        });
        //忘记密码
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgetActivity.class);
                startActivity(i);
            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        //图片验证码更换
        codeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getCaptcha();
            }
        });
    }

    /**
     * 登陆按钮
     */
    @OnClick(R.id.loginBtn)
    public void loginBtn() {
        if (TextUtils.isEmpty(inputTelphone.getText())){
            //设置晃动
            inputTelphone.setShakeAnimation();
            //设置提示
            Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(inputPassword.getText())){
            inputPassword.setShakeAnimation();
            Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        LoginRequest loginRequest = new LoginRequest();
        if (token != null) {
            if (TextUtils.isEmpty(code.getText())) {
                code.setShakeAnimation();
                Toast.makeText(this, R.string.captcha_not_null, Toast.LENGTH_LONG).show();
                return;
            }
            loginRequest.setToken(token);
            loginRequest.setCode(code.getText().toString());
        }
        loginRequest.setTelphone(inputTelphone.getText().toString());
        loginRequest.setPassword(inputPassword.getText().toString());
        mPresenter.login(loginRequest);
    }

    /**
     * 登陆成功
     * @param bean
     */
    @Override
    public void onSuccess(JSONResult<UserLoginVo> bean) {
        UserLoginVo userLoginVo = bean.getResult();
        Toast.makeText(this, userLoginVo.getTelphone(), Toast.LENGTH_LONG).show();
    }

    /**
     * 验证码接收成功
     * @param bean
     */
    @Override
    public void onSuccessCaptcha(JSONResult<CaptchaVO> bean) {
        codeView.setVisibility(View.VISIBLE);
        String imageBase = bean.getResult().getBase64Img();
        codeImage.setImageBitmap(CatpchaUtils.stringtoBitmap(imageBase));
        token = bean.getResult().getToken();
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
        int code = ExceptionEngine.handleException(throwable).code;
        //密码错误则显示验证码
        if (ErrorCodeEnum.USER_PASSWORD_ERROR.getCode() == code) {
            mPresenter.getCaptcha();
        }
        Toast.makeText(this, ExceptionEngine.handleException(throwable).message, Toast.LENGTH_LONG).show();
    }
}
