package com.cc.rd.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.bean.request.user.TelphoneCodeRequest;
import com.cc.rd.custom.LoginEditText;
import com.cc.rd.mvp.contract.login.SendCodeContract;
import com.cc.rd.mvp.presenter.user.SendCodePresenter;
import com.cc.rd.util.ExceptionEngine;
import com.cc.rd.util.ParamUtils;
import com.cc.rd.util.ProgressDialog;
import com.cc.rd.util.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetActivity extends BaseMvpActivity<SendCodePresenter> implements SendCodeContract.View {

    @BindView(R.id.forgetTel)
    LoginEditText forgetTel;
    @BindView(R.id.forgetCode)
    LoginEditText forgetCode;
    @BindView(R.id.sendForgetCode)
    Button codeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    public void initView() {
        mPresenter = new SendCodePresenter();
        mPresenter.attachView(this);
    }

    /**
     * 发送找回密码短信验证码
     * @param
     */
    @OnClick(R.id.sendForgetCode)
    public void sendCodeBtn() {
        if (TextUtils.isEmpty(forgetTel.getText())){
            //设置晃动
            forgetTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (!ParamUtils.isRightTel(forgetTel.getText().toString())) {
            //设置晃动
            forgetTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, "无效手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.sendOldCode(forgetTel.getText().toString());

        new CountDownTimer(60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!ForgetActivity.this.isFinishing()) {
                    codeBtn.setBackgroundColor(Color.parseColor("#B6B6D8"));
                    codeBtn.setClickable(false);
                    codeBtn.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
                }

            }

            @Override
            public void onFinish() {
                if (!ForgetActivity.this.isFinishing()) {
                    codeBtn.setText(R.string.send_telcode);
                    codeBtn.setClickable(true);
                    codeBtn.setBackgroundColor(Color.parseColor("#5f9cf1"));
                }

            }
        }.start();
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.forgetUser)
    public void forgetUser() {
        if (TextUtils.isEmpty(forgetTel.getText())){
            //设置晃动
            forgetTel.setShakeAnimation();
            //设置提示
            Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(forgetCode.getText())){
            forgetCode.setShakeAnimation();
            Toast.makeText(this, R.string.telcode_not_null, Toast.LENGTH_LONG).show();
            return;
        }
        TelphoneCodeRequest telphoneCodeRequest = new TelphoneCodeRequest();
        telphoneCodeRequest.setTelphone(forgetTel.getText().toString());
        telphoneCodeRequest.setCode(forgetCode.getText().toString());
        mPresenter.checkSms(telphoneCodeRequest);
    }

    @OnClick(R.id.back)
    public void back() {
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

    @Override
    public void onSuccess(Result bean) {
        Intent i = new Intent(this, ResetPwdActivity.class);
        i.putExtra("telphone", forgetTel.getText().toString());
        i.putExtra("code", forgetCode.getText().toString());
        startActivity(i);
    }


}
