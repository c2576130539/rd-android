package com.cc.rd.mvp.model.user;

import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.vo.CaptchaVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.bean.request.user.LoginRequest;
import com.cc.rd.mvp.contract.login.MainContract;
import com.cc.rd.net.RetrofitClient;

import io.reactivex.Flowable;

public class MainModel  implements MainContract.Model {

    @Override
    public Flowable<JSONResult<UserLoginVo>> login(LoginRequest loginRequest) {
        return RetrofitClient.getInstance().getApi().login(loginRequest);
    }

    @Override
    public Flowable<JSONResult<CaptchaVO>> getCaptcha() {
        return RetrofitClient.getInstance().getApi().getCaptcha();
    }
}
