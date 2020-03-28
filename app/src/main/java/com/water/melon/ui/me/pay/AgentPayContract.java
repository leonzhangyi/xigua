package com.water.melon.ui.me.pay;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;

public interface AgentPayContract {
    interface Present extends BasePresenter {
    }

    interface View extends BaseView<Present> {
    }
}
