package com.cc.rd.mvp.model.user;

import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.request.user.TelphoneCodeRequest;
import com.cc.rd.mvp.contract.login.SendCodeContract;
import com.cc.rd.net.RetrofitClient;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public class SendCodeModel implements SendCodeContract.Model {

    @Override
    public Flowable<Result> sendNewCode(String telphone) {
        return RetrofitClient.getInstance().getApi().sendNewCode(telphone);
    }

    @Override
    public Flowable<Result> sendOldCode(String telphone) {
        return RetrofitClient.getInstance().getApi().sendOldCode(telphone);
    }

    @Override
    public Flowable<Result> checkSms(TelphoneCodeRequest telphoneCodeRequest) {
        return RetrofitClient.getInstance().getApi().checkSms(telphoneCodeRequest);
    }
}
