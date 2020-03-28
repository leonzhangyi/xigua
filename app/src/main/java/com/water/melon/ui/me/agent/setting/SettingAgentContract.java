package com.water.melon.ui.me.agent.setting;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.MyAgentBean;

public class SettingAgentContract {
    interface Present extends BasePresenter {

        void setDate(MyAgentBean request);



    }

    interface View extends BaseView<Present> {
        void setDate(MyAgentBean myAgentBean);
        void setSucDate(boolean def);

    }
}
