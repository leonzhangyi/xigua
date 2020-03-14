package com.water.melon.ui.me.agent.myagent;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.MyAgentBean;

import java.util.List;

public class MyAgentContract {
    interface Present extends BasePresenter {
        void getAgentData(int page);
    }

    interface View extends BaseView<Present> {
        void setAgentDate(List<MyAgentBean> myAgentBeans,boolean isDate);
    }
}
