package com.water.melon.ui.login;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;

public interface RegistContract {
    interface Present extends BasePresenter {

        void doRegist(String phone, String password);

    }

    interface View extends BaseView<Present> {
        void regsit(boolean isSuc);

    }
}
