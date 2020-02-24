package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;

public interface HomeContract {
    interface Present extends BasePresenter{
        void getHomeBean();

    }

    interface View extends BaseView<Present> {
        void setHomeBean(ArrayList<HomeBean> homeBeans );
    }

}
