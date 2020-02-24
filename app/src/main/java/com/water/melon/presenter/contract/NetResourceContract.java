package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.TabBean;

import java.util.List;

public interface NetResourceContract {
    interface Present extends BasePresenter {

        void getBigTab();
    }

    interface View extends BaseView<Present> {
        void setBigTab(List<TabBean> data);
    }

}
