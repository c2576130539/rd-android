package com.cc.rd.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cc.rd.R;
import com.cc.rd.base.BaseMvpActivity;
import com.cc.rd.mvp.contract.login.UpdateContract;
import com.cc.rd.mvp.presenter.user.UpdatePresenter;
import com.cc.rd.util.Result;

import butterknife.ButterKnife;

public class OnceActivity extends BaseMvpActivity<UpdatePresenter> implements UpdateContract.View  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mPresenter = new UpdatePresenter();
        mPresenter.attachView(this);

    }

    @Override
    public void onSuccess(Result result) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
