package com.cc.rd.mvp.contract.login;

import com.cc.rd.base.BaseView;
import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.bean.request.user.UserUpdateRequest;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public interface UpdateContract {

    interface Model {
        Flowable<Result> changeUserImage(UserUpdateRequest request);
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
        //修改需求
        void changeUserImage(UserUpdateRequest request);
    }
}
