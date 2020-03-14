package com.water.melon.ui.login;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;

public interface LoginContract {
    interface Present extends BasePresenter {

        void login(String phone,String password);
    }

    interface View extends BaseView<Present> {
        void login(boolean isSuc);
    }
}
