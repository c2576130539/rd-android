package com.cc.rd.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.bean.request.user.ResetPasswordRequest;
import com.cc.rd.custom.LoginEditText;
import com.cc.rd.mvp.contract.login.ForgetPwdContract;
import com.cc.rd.mvp.presenter.user.ForgetPwdPresenter;
import com.cc.rd.util.ExceptionEngine;
import com.cc.rd.util.ProgressDialog;
import com.cc.rd.util.Result;
import com.uber.autodispose.AutoDisposeConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPwdActivity extends BaseMvpActivity<ForgetPwdPresenter> implements ForgetPwdContract.View {

    @BindView(R.id.forgetPwd)
    LoginEditText forgetPwd;
    @BindView(R.id.forgetCPwd)
    LoginEditText forgetCPWd;

    @BindView(R.id.forgetVPwd)
    ImageButton forgetVPwd;
    @BindView(R.id.forgetVCPwd)
    ImageButton forgetVCPwd;

    private String telphone;
    private String code;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Intent i = getIntent();
        telphone = i.getStringExtra("telphone");
        code = i.getStringExtra("code");
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initView() {
        mPresenter = new ForgetPwdPresenter();
        mPresenter.attachView(this);
    }

    private void init() {
        //密码默认初始为不可见
        forgetVPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
        forgetVCPwd.setImageDrawable(getResources().getDrawable(R.drawable.invisible));

        //密码是否可见
        forgetVPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    forgetPwd.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    return false;
                }else {
                    //再修改为抬起时的正常图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    forgetPwd.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    return false;
                }
            }
        });
        forgetVCPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    forgetPwd.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    return false;
                }else {
                    //再修改为抬起时的正常图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    forgetPwd.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.resetPwd)
    public void resetBtn() {
        pwd = forgetPwd.getText().toString();
        String cpwd = forgetCPWd.getText().toString();
        if ("".equals(pwd)){
            forgetPwd.setShakeAnimation();
            Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(cpwd)){
            forgetCPWd.setShakeAnimation();
            Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (!pwd.equals(cpwd)) {
            forgetPwd.setShakeAnimation();
            forgetCPWd.setShakeAnimation();
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_LONG).show();
            return;
        }

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setConfirmPassword(cpwd);
        resetPasswordRequest.setNewPassword(pwd);
        resetPasswordRequest.setTelphone(telphone);
        resetPasswordRequest.setCode(code);
        mPresenter.resetPwd(resetPasswordRequest);
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

    @Override
    public void onSuccess(Result bean) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("telphone", telphone);
        i.putExtra("password", pwd);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
