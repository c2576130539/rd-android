package com.cc.rd.mvp.contract.login;

import com.cc.rd.base.BaseView;
import com.cc.rd.bean.request.user.ResetPasswordRequest;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;

public interface ForgetPwdContract {

    interface Model {
        Flowable<Result> resetPwd(ResetPasswordRequest resetPasswordRequest);
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
         * 登陆
         */
        void resetPwd(ResetPasswordRequest resetPasswordRequest);
    }
}
