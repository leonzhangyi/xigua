package com.water.melon.presenter;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.presenter.contract.WelfContract;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;

public class WelPresent extends BasePresenterParent implements WelfContract.present {
    private WelfContract.View mView;

    public WelPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (WelfContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }


    private ArrayList<HomeBean> homeBeans = new ArrayList<>();

    private void setHomeBeans() {
        for (int i = 0; i < 12; i++) {
            HomeBean homeBean = new HomeBean();
            homeBean.setName("测试"+i);
            homeBean.setIcon("");
            homeBeans.add(homeBean);
        }
    }

    @Override
    public void setWelfBeans() {
        setHomeBeans();
        mView.getwelfBeans(homeBeans);
    }
}
