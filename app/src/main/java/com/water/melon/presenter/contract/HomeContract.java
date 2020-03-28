package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;
import java.util.List;

public interface HomeContract {
    interface Present extends BasePresenter {
        void getHomeBean();

        void getHomeAdv();

        void getAppNotice();


        void doAdvClick(AdvBean advBean);
    }

    interface View extends BaseView<Present> {
        void setHomeBean(List<AdvBean> advBeans);

        void setHomeAdv(List<AdvBean> advBeans);

        void getAppNotice(List<AdvBean> advBeans);
    }

}
