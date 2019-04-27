package com.cc.rd.mvp.presenter.user;

import com.cc.rd.base.BasePresenter;
import com.cc.rd.bean.request.user.UserUpdateRequest;
import com.cc.rd.mvp.contract.login.UpdateContract;
import com.cc.rd.mvp.model.user.UpdateModel;
import com.cc.rd.net.RxScheduler;
import com.cc.rd.util.Result;

import io.reactivex.functions.Consumer;

public class UpdatePresenter extends BasePresenter<UpdateContract.View> implements UpdateContract.Presenter {

    private UpdateContract.Model model;

    public UpdatePresenter() {
        model = new UpdateModel();
    }

    @Override
    public void changeUserImage(UserUpdateRequest request) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.changeUserImage(request)
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
}
