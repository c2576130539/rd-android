package com.cc.rd.mvp.presenter.user;

import com.cc.rd.base.BasePresenter;
import com.cc.rd.bean.request.user.ResetPasswordRequest;
import com.cc.rd.mvp.contract.login.ForgetPwdContract;
import com.cc.rd.mvp.model.user.ForgetPwdModel;
import com.cc.rd.net.RxScheduler;
import com.cc.rd.util.Result;

import io.reactivex.functions.Consumer;

public class ForgetPwdPresenter extends BasePresenter<ForgetPwdContract.View> implements ForgetPwdContract.Presenter {

    private ForgetPwdContract.Model model;

    public ForgetPwdPresenter() {
        model = new ForgetPwdModel();
    }

    @Override
    public void resetPwd(ResetPasswordRequest resetPasswordRequest) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.resetPwd(resetPasswordRequest)
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
