package com.cc.rd.mvp.model.user;

import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.mvp.contract.login.RegisterContract;
import com.cc.rd.net.RetrofitClient;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public class RegisterModel implements RegisterContract.Model {

    @Override
    public Flowable<Result> registerUser(UserAddRequest userAddRequest) {
        return RetrofitClient.getInstance().getApi().registerUser(userAddRequest);
    }

    @Override
    public Flowable<Result> sendNewCode(String telphone) {
        return RetrofitClient.getInstance().getApi().sendNewCode(telphone);
    }
}
