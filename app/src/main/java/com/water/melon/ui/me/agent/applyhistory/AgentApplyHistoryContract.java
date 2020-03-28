package com.water.melon.ui.me.agent.applyhistory;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.MyAgentBean;

import java.util.List;

public class AgentApplyHistoryContract {
    interface Present extends BasePresenter {
        void getApplyList();
    }

    interface View extends BaseView<Present> {
        void setDate(List<AgentBean> vipBean);
    }
}
