package com.water.melon.presenter;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.presenter.contract.MeContract;

public class MePresenter extends BasePresenterParent implements MeContract.Presenter {
    private MeContract.View mView;
    public MePresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }
}
