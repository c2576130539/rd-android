package com.cc.rd.mvp.contract.login;

import com.cc.rd.base.BaseView;
import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.vo.CaptchaVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.bean.request.user.LoginRequest;

import io.reactivex.Flowable;

public interface MainContract {

    interface Model {
        Flowable<JSONResult<UserLoginVo>> login(LoginRequest loginRequest);
        Flowable<JSONResult<CaptchaVO>> getCaptcha();
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(JSONResult<UserLoginVo> bean);

        void onSuccessCaptcha(JSONResult<CaptchaVO> bean);
    }

    interface Presenter {
        /**
         * 登陆
         */
        void login(LoginRequest loginRequest);

        void getCaptcha();
    }
}
