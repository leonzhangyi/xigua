package com.water.melon.presenter;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.presenter.contract.MeContract;
import com.water.melon.utils.update.CheckAppVersionUtil;

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

    @Override
    public void checkAppVersion() {
        CheckAppVersionUtil.checkApp(true, new CheckAppVersionUtil.CheckAppVersionListen() {
            @Override
            public void onCheckStart() {
            }

            @Override
            public void hasUpdate(boolean has, String msg) {
                mView.checkAppVersion(has, msg);
            }
        });
    }


}
