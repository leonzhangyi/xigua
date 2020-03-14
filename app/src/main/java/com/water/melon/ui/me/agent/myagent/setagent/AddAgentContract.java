package com.water.melon.ui.me.agent.myagent.setagent;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.MyAgentBean;

public class AddAgentContract {
    interface Present extends BasePresenter {

        void getAgentInfo(String id);

        void addAgent(MyAgentBean request);
    }

    interface View extends BaseView<Present> {
        void setAgentDate(MyAgentBean agentBean);

        void addSucc();


    }
}
