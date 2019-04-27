package com.cc.rd.mvp.presenter.user;

import com.cc.rd.base.BasePresenter;
import com.cc.rd.bean.request.user.TelphoneCodeRequest;
import com.cc.rd.mvp.contract.login.SendCodeContract;
import com.cc.rd.mvp.model.user.SendCodeModel;
import com.cc.rd.net.RxScheduler;
import com.cc.rd.util.Result;

import io.reactivex.functions.Consumer;


public class SendCodePresenter extends BasePresenter<SendCodeContract.View> implements SendCodeContract.Presenter {

    private SendCodeContract.Model model;

    public SendCodePresenter() {
        model = new SendCodeModel();
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
    public void sendOldCode(String telphone) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.sendOldCode(telphone)
                .compose(RxScheduler.<Result>Flo_io_main())
                .as(mView.<Result>bindAutoDispose())
                .subscribe(new io.reactivex.functions.Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        mView.hideLoading();
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void checkSms(TelphoneCodeRequest telphoneCodeRequest) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.checkSms(telphoneCodeRequest)
                .compose(RxScheduler.<Result>Flo_io_main())
                .as(mView.<Result>bindAutoDispose())
                .subscribe(new io.reactivex.functions.Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        mView.onSuccess(result);
                        mView.hideLoading();
                    }
                }, new io.reactivex.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }
}
