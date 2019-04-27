package com.cc.rd.mvp.contract.login;

import com.cc.rd.base.BaseView;
import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public interface RegisterContract {

    interface Model {
        Flowable<Result> registerUser(UserAddRequest userAddRequest);
        Flowable<Result> sendNewCode(String telphone);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(Result result);
    }

    interface Presenter {

        void sendNewCode(String telphone);

        void registerUser(UserAddRequest userAddRequest);
    }
}
