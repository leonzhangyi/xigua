package com.water.melon.ui.me.feek;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;

public class FeekContract {
    interface Present extends BasePresenter {
        void subMessage(String msg);
    }

    interface View extends BaseView<Present> {
        void subSucc(boolean isSuc);

    }
}
