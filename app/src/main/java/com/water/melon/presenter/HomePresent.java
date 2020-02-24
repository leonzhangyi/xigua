package com.water.melon.presenter;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.presenter.contract.HomeContract;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;

public class HomePresent extends BasePresenterParent implements HomeContract.Present {

    private HomeContract.View mView;

    public HomePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (HomeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getHomeBean() {
        setHomeBeans();
        mView.setHomeBean(homeBeans);
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



}
