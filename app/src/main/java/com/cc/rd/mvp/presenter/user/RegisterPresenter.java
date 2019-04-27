package com.cc.rd.mvp.presenter.user;

import com.cc.rd.base.BasePresenter;
import com.cc.rd.bean.request.user.UserAddRequest;
import com.cc.rd.mvp.contract.login.RegisterContract;
import com.cc.rd.mvp.model.user.RegisterModel;
import com.cc.rd.net.RxScheduler;
import com.cc.rd.util.Result;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private RegisterContract.Model model;

    public RegisterPresenter() {
        model = new RegisterModel();
    }

    @Override
    public void sendNewCode(String telphone) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.sendNewCode(telphone)
                .compose(RxScheduler.<Result>Flo_io_main())
                .as(mView.<Result>bindAutoDispose())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
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
    public void registerUser(UserAddRequest userAddRequest) {
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.registerUser(userAddRequest)
                .compose(RxScheduler.<Result>Flo_io_main())
                .as(mView.<Result>bindAutoDispose())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        mView.onSuccess(result);
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
