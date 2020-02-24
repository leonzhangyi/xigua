package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;

public interface WelcomeContract {
    interface Presenter extends BasePresenter {
        void getMyYm();

    }

    interface View extends BaseView<Presenter> {
        void doFinishInit();

        void doErrCode(int code);
    }

}
