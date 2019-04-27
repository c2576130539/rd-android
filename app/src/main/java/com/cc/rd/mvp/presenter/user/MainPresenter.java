package com.cc.rd.mvp.presenter.user;

import com.cc.rd.base.BasePresenter;
import com.cc.rd.bean.JSONResult;
import com.cc.rd.bean.vo.CaptchaVO;
import com.cc.rd.bean.vo.UserLoginVo;
import com.cc.rd.bean.request.user.LoginRequest;
import com.cc.rd.mvp.contract.login.MainContract;
import com.cc.rd.mvp.model.user.MainModel;
import com.cc.rd.net.RxScheduler;

import io.reactivex.functions.Consumer;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private MainContract.Model model;

    public MainPresenter() {
        model = new MainModel();
    }

    @Override
    public void login(LoginRequest loginRequest) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.login(loginRequest)
                .compose(RxScheduler.<JSONResult<UserLoginVo>>Flo_io_main())
                .as(mView.<JSONResult<UserLoginVo>>bindAutoDispose())
                .subscribe(new Consumer<JSONResult<UserLoginVo>>() {
                    @Override
                    public void accept(JSONResult<UserLoginVo> bean) throws Exception {
                        mView.onSuccess(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getCaptcha() {
        if (!isViewAttached()) {
            return;
        }
        mView.hideLoading();
        model.getCaptcha()
                .compose(RxScheduler.<JSONResult<CaptchaVO>>Flo_io_main())
                .as(mView.<JSONResult<CaptchaVO>>bindAutoDispose())
                .subscribe(new Consumer<JSONResult<CaptchaVO>>() {
                    @Override
                    public void accept(JSONResult<CaptchaVO> captchaVO) throws Exception {
                        mView.onSuccessCaptcha(captchaVO);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }
}
