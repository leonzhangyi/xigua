package com.water.melon.ui.me.agent;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;

public class AgentContract {
    interface Present extends BasePresenter {
        void getAgentInfo();

        void applyAgent(AgentBean agentBean);
    }

    interface View extends BaseView<Present> {

        void setAgentInfo(AgentBean agentInfo);
    }
}
