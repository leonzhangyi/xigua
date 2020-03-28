package com.water.melon.ui.me.agent.mymoney.submit;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;

public interface MyMoneySubmitContract {
    interface Present extends BasePresenter {
        void submit(String mobile, String money, String code);
    }

    interface View extends BaseView<Present> {
        void subSuc();
    }
}
