package com.cc.rd.mvp.model.user;

import com.cc.rd.bean.request.user.ResetPasswordRequest;
import com.cc.rd.mvp.contract.login.ForgetPwdContract;
import com.cc.rd.net.RetrofitClient;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public class ForgetPwdModel implements ForgetPwdContract.Model {

    @Override
    public Flowable<Result> resetPwd(ResetPasswordRequest resetPasswordRequest) {
        return RetrofitClient.getInstance().getApi().resetPwd(resetPasswordRequest);
    }
}
