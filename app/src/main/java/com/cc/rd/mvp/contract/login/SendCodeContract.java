package com.cc.rd.mvp.contract.login;

import com.cc.rd.base.BaseView;
import com.cc.rd.bean.request.user.TelphoneCodeRequest;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public interface SendCodeContract {

    interface Model {
        Flowable<Result> sendNewCode(String telphone);
        Flowable<Result> sendOldCode(String telphone);
        Flowable<Result> checkSms(TelphoneCodeRequest telphoneCodeRequest);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(Result bean);
    }

    interface Presenter {
        /**
         * 给新用户发送验证码
         */
        void sendNewCode(String telphone);

        /**
         * 给老用户发送验证码
         */
        void sendOldCode(String telphone);

        void checkSms(TelphoneCodeRequest telphoneCodeRequest);
    }
}
