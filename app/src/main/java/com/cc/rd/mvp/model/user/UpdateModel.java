package com.cc.rd.mvp.model.user;

import com.cc.rd.bean.request.user.UserUpdateRequest;
import com.cc.rd.mvp.contract.login.UpdateContract;
import com.cc.rd.net.RetrofitClient;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public class UpdateModel implements UpdateContract.Model {

    @Override
    public Flowable<Result> changeUserImage(UserUpdateRequest request) {
        return RetrofitClient.getInstance().getApi().changeUserImage(request);
    }
}
