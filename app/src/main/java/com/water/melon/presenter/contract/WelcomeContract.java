package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AdvBean;

import java.util.List;

public interface WelcomeContract {
    interface Presenter extends BasePresenter {
        void getMyYm();

        void getOpenAdv();

        void doAdvClick(AdvBean advBean);
    }

    interface View extends BaseView<Presenter> {
        void doFinishInit();

        void doErrCode(int code);

        void init1(Boolean haveAdv, AdvBean advBeans);


    }

}
