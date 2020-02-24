package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;

public interface WelfContract {
    interface present extends BasePresenter {
        void setWelfBeans();
    }

    interface View extends BaseView<WelfContract.present> {
        void getwelfBeans(ArrayList<HomeBean> beans);
    }

}
